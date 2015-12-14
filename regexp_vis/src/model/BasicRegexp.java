package model;

import java.util.*;

/**
 * Class representing a "basic" regular expression, with one top level operator.
 * A tree of these can then be used to any regular expression.
 *
 * BasicRegexp is designed to be immutable, similar to the Java String class.
 *
 * @author Matthew Nicholls
 */
public class BasicRegexp implements Cloneable, Comparable<BasicRegexp> {
    public enum RegexpOperator {
        NONE(3),
        STAR(2),
        PLUS(2),
        OPTION(2),
        SEQUENCE(1),
        CHOICE(0);

        // Operator precedence, higher value, higher precedence
        private final int mPrecedence;

        private RegexpOperator(int precedence)
        {
            mPrecedence = precedence;
        }

        public int getPrecedence()
        {
            return mPrecedence;
        }
    }

    /**
     * Character used to represent epsilon / lambda / etc.
     */
    public static final char EPSILON_CHAR = '\0';

    /**
     * Expression used to represent epsilon / lambda / etc. Here so we don't
     * have to keep creating new objects
     */
    public static final BasicRegexp EPSILON_EXPRESSION =
        new BasicRegexp(EPSILON_CHAR);

    final private ArrayList<BasicRegexp> mOperands;
    final private char mChar;
    final private RegexpOperator mOperator;
    // IDEA(mjn33): cache results from .optimise() and .isNullable()?

    /**
     * Construct a BasicRegexp with the specified high-level operator and
     * operands
     *
     * @param operands The operands for this BasicRegexp
     * @param op The operator for this BasicRegexp, <b>cannot</b> be
     * RegexpOperator.NONE
     * @throws IllegalArgumentException if any of the following: "op" is
     * RegexpOperator.NONE, multiple operands are provided for a unary operator,
     * or no operands are passed
     */
    public BasicRegexp(ArrayList<BasicRegexp> operands, RegexpOperator op)
    {
        if (op == RegexpOperator.NONE) {
            throw new IllegalArgumentException(
                "RegexpOperator.NONE only allowed for single character " +
                "expressions");
        }
        if (isUnaryOperator(op) && operands.size() > 1) {
            throw new IllegalArgumentException(
                "Multiple operands passed for a unary operator");
        }
        if (operands.size() == 0) {
            throw new IllegalArgumentException("No operands passed");
        }

        // Optimisation: Check if parentheses have been use where they aren't
        // needed, e.g.
        //   * "abc(de)fgh"    ----> "abcdefgh"
        //   * "a|(b|c|d)|e"   ----> "a|b|c|d|e"
        // Note: some information on parentheses is lost in the parsing
        // process anyway, such as "(a*)(b*)"
        ArrayList<BasicRegexp> optimisedOperands;
        if (op == RegexpOperator.CHOICE || op == RegexpOperator.SEQUENCE) {
            optimisedOperands = new ArrayList<>();
            for (BasicRegexp operand : operands) {
                if (operand.getOperator() == op) {
                    // Insert all sub operands
                    optimisedOperands.addAll(operand.getOperands());
                } else {
                    // Insert this as-is
                    optimisedOperands.add(operand);
                }
            }
        } else {
            optimisedOperands = new ArrayList<>(operands);
        }

        mOperands = optimisedOperands;
        mChar = EPSILON_CHAR;
        mOperator = op;
    }

    /**
     * Construct a BasicRegexp with the specified high-level <b>unary</b>
     * operator and single operand
     *
     * @param operand The operand for this BasicRegexp
     * @param op The operator for this BasicRegexp, <b>cannot</b> be
     * RegexpOperator.NONE, also <b>must</b> be a unary operator
     * @throws IllegalArgumentException if "op" is RegexpOperator.NONE
     */
    public BasicRegexp(BasicRegexp operand, RegexpOperator op)
    {
        if (op == RegexpOperator.NONE) {
            throw new IllegalArgumentException(
                "RegexpOperator.NONE only allowed for single character " +
                "expressions");
        }

        if (!isUnaryOperator(op)) {
            throw new IllegalArgumentException(
                "Non-unary operators require multiple operands");
        }

        mOperands = new ArrayList<>();
        mOperands.add(operand);
        mChar = EPSILON_CHAR;
        mOperator = op;
    }

    /**
     * Construct a single character expression BasicRegexp
     *
     * @param c The operand for this BasicRegexp
     */
    public BasicRegexp(char c)
    {
        mOperands = null;
        mChar = c;
        mOperator = RegexpOperator.NONE;
    }

    public int compareTo(BasicRegexp other)
    {
        // Compare operators first
        int ret = mOperator.compareTo(other.mOperator);
        if (ret != 0) {
            return ret;
        }

        // Single character expressions come before non-single single character
        // expressions
        if (isSingleChar() != other.isSingleChar()) {
            return isSingleChar() ? -1 : 1;
        } else if (isSingleChar()) {
            // Compare chars normally
            return Character.compare(mChar, other.mChar);
        } else {
            // Compare complex expressions
            Iterator<BasicRegexp> thisIt = mOperands.iterator();
            Iterator<BasicRegexp> otherIt = other.mOperands.iterator();
            while (thisIt.hasNext() && otherIt.hasNext()) {
                BasicRegexp thisOperand = thisIt.next();
                BasicRegexp otherOperand = otherIt.next();

                // Return based on the first difference in operands
                ret = thisOperand.compareTo(otherOperand);
                if (ret != 0) {
                    return ret;
                }
            }

            // Shorter expressions come before longer ones
            if (thisIt.hasNext() == otherIt.hasNext()) {
                return 0;
            } else {
                return thisIt.hasNext() ? 1 : -1;
            }
        }
    }

    /**
     * @return true if this is a single character expression, false
     * otherwise
     */
    public boolean isSingleChar()
    {
        return mOperands == null;
    }

    /**
     * @return The high-level operator for this BasicRegexp
     */
    public RegexpOperator getOperator()
    {
        return mOperator;
    }

    /**
     * @return The operands for this BasicRegexp, as an unmodifiable list.
     * @throws RuntimeException if this BasicRegexp is a single character
     * expression
     */
    public List<BasicRegexp> getOperands()
    {
        if (isSingleChar()) {
            throw new RuntimeException(
                "Cannot call getOperands() on a single character expression");
        }

        return Collections.unmodifiableList(mOperands);
    }

    /**
     * @return The character for this single character expression
     * @throws RuntimeException if this BasicRegexp isn't a single character
     * expression
     */
    public char getChar()
    {
        if (!isSingleChar()) {
            throw new RuntimeException(
                "getChar() must only be called on single character " +
                "expressions");
        }

        return mChar;
    }

    /**
     * @return Whether this regular expression is nullable, i.e. its language
     * contains the empty word
     */
    public boolean isNullable()
    {
        switch (mOperator) {
        case NONE:
            // Single character, only nullable if epsilon
            return mChar == EPSILON_CHAR;
        case STAR:
            // Star is always nullable
            return true;
        case PLUS:
            // Plus is only nullable if the operand is nullable
            return mOperands.get(0).isNullable();
        case OPTION:
            // Option is always nullable
            return true;
        case SEQUENCE:
            for (BasicRegexp operand : mOperands) {
                // Sequence is not nullable if any part of the sequence is not
                // nullable
                if (!operand.isNullable()) {
                    return false;
                }
            }
            return true;
        case CHOICE:
            for (BasicRegexp operand : mOperands) {
                // Choice is nullable if any part of the choice is nullable
                if (operand.isNullable()) {
                    return true;
                }
            }
            return false;
        default:
            throw new RuntimeException("BUG: Should be unreachable.");
        }
    }

    /**
     * Performs a deep copy of this BasicRegexp, all sub expressions are copied.
     *
     * @return The cloned object
     */
    @Override
    public BasicRegexp clone()
    {
        ArrayList<BasicRegexp> newOperands = null;
        // Deep copy operands if we have any
        if (mOperands != null) {
            newOperands = new ArrayList<>();
            for (BasicRegexp operand : mOperands) {
                BasicRegexp cloned = operand.clone();
                newOperands.add(cloned);
            }
            return new BasicRegexp(newOperands, mOperator);
        } else {
            return new BasicRegexp(mChar);
        }
    }

    /**
     * Find the matching closing parenthesis for the parenthesis at the start of
     * this string
     *
     * <b>Precondition:</b> it is assumed that "str" starts with an opening
     * parenthesis
     *
     * @param str The string in question
     * @return the index of the matching closing parenthesis, -1 otherwise if
     * there is no matching closing parenthesis in this string
     */
    private static int findMatchingParenIdx(String str)
    {
        assert(str.charAt(0) == '(');
        int curIdx = 1;
        int parenCount = 1; // 1 open parenthesis at start

        while (curIdx < str.length()) {
            switch (str.charAt(curIdx)) {
            case '(':
                parenCount++;
                break;
            case ')':
                parenCount--;
                break;
            default:
                break;
            }
            if (parenCount == 0) {
                return curIdx;
            }
            curIdx++;
        }

        return -1; // No matching parenthesis found
    }

    /**
     * @param op The operator in question
     * @return True if the specified operator is unary, false otherwise
     */
    public static boolean isUnaryOperator(RegexpOperator op)
    {
        // Only CHOICE and SEQUENCE are not unary
        return op == RegexpOperator.STAR ||
               op == RegexpOperator.PLUS ||
               op == RegexpOperator.OPTION ||
               op == RegexpOperator.NONE;
    }

    /**
     * Factored out of parseRegexp, common code for processing unary operators
     * such as PLUS, STAR and OPTION
     *
     * @param sequenceOperands The current working list of operands in sequence
     * @param op The operator that we are processing
     * @see parseRegexp
     * @throws InvalidRegexpException in event of parse error
     */
    private static void processUnaryOp(ArrayList<BasicRegexp> sequenceOperands,
        RegexpOperator op) throws InvalidRegexpException
    {
        if (!sequenceOperands.isEmpty()) {
            BasicRegexp back = sequenceOperands
                    .remove(sequenceOperands.size() - 1);

            // Wrap the expression in another expression
            sequenceOperands.add(new BasicRegexp(back, op));
        } else {
            throw new InvalidRegexpException(
                op.name() + " operator on empty word");
        }
    }

    /**
     * Factored out of parseRegexp, common code for processing the choice
     * operator
     *
     * @param sequenceOperands The current working list of operands in sequence
     * @param choiceOperands The current working list of operands for choice
     * @see parseRegexp
     * @throws InvalidRegexpException in event of parse error
     */
    private static void processChoiceOp(ArrayList<BasicRegexp> sequenceOperands,
        ArrayList<BasicRegexp> choiceOperands) throws InvalidRegexpException
    {
        if (sequenceOperands.size() > 1) {
            // Found multiple operands, they will be in sequence
            // BasicRegexp clones the ArrayList
            BasicRegexp re = new BasicRegexp(sequenceOperands,
                RegexpOperator.SEQUENCE);
            sequenceOperands.clear();
            choiceOperands.add(re);
        } else if (!sequenceOperands.isEmpty()) {
            // One element
            BasicRegexp back = sequenceOperands.remove(0);
            choiceOperands.add(back);
        } else {
            throw new InvalidRegexpException("CHOICE operator on empty word");
        }
    }

    /**
     * Parse the given regular expression, outputting a tree hierarchy of
     * BasicRegexp objects
     *
     * @param str The regular expression to parse
     * @return The root BasicRegexp for the parse tree
     * @throws InvalidRegexpException if this isn't a valid regexp (or at least
     * one this parser doesn't support)
     */
    public static BasicRegexp parseRegexp(String str)
        throws InvalidRegexpException
    {
        ArrayList<BasicRegexp> sequenceOperands = new ArrayList<>();
        ArrayList<BasicRegexp> choiceOperands = new ArrayList<>();
        int idx = 0;

        while (idx < str.length()) {
            switch (str.charAt(idx)) {
            case '(': {
                int parenIdx = findMatchingParenIdx(str.substring(idx));
                if (parenIdx == -1) {
                    throw new InvalidRegexpException(
                        "Unclosed parenthesis found");
                }
                parenIdx += idx; // parenIdx is relative to idx
                BasicRegexp re = parseRegexp(str.substring(idx + 1, parenIdx));
                if (re == null) {
                    // Completely empty sub-expression, e.g. "()"
                    throw new InvalidRegexpException(
                        "Empty parentheses found");
                }
                sequenceOperands.add(re);
                idx = parenIdx;
                break;
            }
            case ')':
                // No last matching opening parenthesis, error
                throw new InvalidRegexpException(
                    "Stray closing parenthesis found");
            case '*':
                processUnaryOp(sequenceOperands, RegexpOperator.STAR);
                break;
            case '+':
                processUnaryOp(sequenceOperands, RegexpOperator.PLUS);
                break;
            case '?':
                processUnaryOp(sequenceOperands, RegexpOperator.OPTION);
                break;
            case '|':
                processChoiceOp(sequenceOperands, choiceOperands);
                break;
            default:
                // Ignore whitespace
                if (!Character.isWhitespace(str.charAt(idx))) {
                    // Normal character
                    // IDEA(mjn33): Parse e.g. '%' as epsilon
                    BasicRegexp re = new BasicRegexp(str.charAt(idx));
                    sequenceOperands.add(re);
                }
            }
            idx++;
        }

        if (!choiceOperands.isEmpty()) {
            // The remaining sequence operands are part of the choice operation
            processChoiceOp(sequenceOperands, choiceOperands);
            return new BasicRegexp(choiceOperands, RegexpOperator.CHOICE);
        } else if (sequenceOperands.size() == 1) {
            // Don't return a sequence of a single expression
            return sequenceOperands.get(0);
        } else if (!sequenceOperands.isEmpty()) {
            return new BasicRegexp(sequenceOperands, RegexpOperator.SEQUENCE);
        } else {
            return null;
        }
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        toStringBuilder(sb);
        return sb.toString();
    }

    private void toStringBuilder(StringBuilder sb)
    {
        switch (mOperator) {
        case NONE:
            sb.append(mChar);
            break;
        case STAR:
        case PLUS:
        case OPTION: {
            // All these operators can be treated the same, just use a
            // different char
            RegexpOperator subExprOp = mOperands.get(0).getOperator();
            // Check if sub-expression operator has lower precedence,
            // in which case we need to put it in parentheses
            if (subExprOp.getPrecedence() < getOperator().getPrecedence()) {
                sb.append("(");
                mOperands.get(0).toStringBuilder(sb);
                sb.append(")");
            } else {
                mOperands.get(0).toStringBuilder(sb);
            }
            switch (mOperator) {
            case STAR:
                sb.append('*');
                break;
            case PLUS:
                sb.append('+');
                break;
            case OPTION:
                sb.append('?');
                break;
            default:
            }
            break;
        }
        case SEQUENCE:
            for (BasicRegexp operand : mOperands) {
                // Check if sub-expression operator has lower precedence,
                // in which case we need to put it in parentheses
                if (operand.getOperator().getPrecedence() <
                    getOperator().getPrecedence()) {
                    sb.append("(");
                    operand.toStringBuilder(sb);
                    sb.append(")");
                } else {
                    operand.toStringBuilder(sb);
                }
            }
            break;
        case CHOICE: {
            Iterator<BasicRegexp> it = mOperands.iterator();
            while (it.hasNext()) {
                BasicRegexp operand = it.next();
                // Nothing is less tightly binding than CHOICE, don't need to
                // put anything in parentheses
                operand.toStringBuilder(sb);
                if (it.hasNext()) {
                    sb.append("|");
                }
            }
            break;
        }
        default:
            throw new RuntimeException("BUG: Should be unreachable.");
        }
    }

    /**
     * optimiseStarOnSC: "Optimise STAR on SEQUENCE or CHOICE". For a STAR on a
     * SEQUENCE or CHOICE, we can make some fancy optimisations such as:
     * <ul>
     *   <li> (a*b*)* &#8594; (a|b)*
     *   <li> ((a|b)*b*)* &#8594; (a|b|b)* (can be optimised yet further)
     *   <li> etc.
     * </ul>
     * @param re The regular expression to optimise
     * @param optimisedOperands List to add the operands for the new optimised
     * expression to
     * @return True if any optimisations were made, false otherwise
     */
    private boolean optimiseStarOnSC(BasicRegexp re,
        ArrayList<BasicRegexp> optimisedOperands)
    {
        if (re.getOperator() == RegexpOperator.SEQUENCE && !re.isNullable()) {
            // Non-nullable SEQUENCE, can't progress further.
            optimisedOperands.add(re);
            return false;
        }
        // This is a CHOICE or nullable SEQUENCE (which we can turn into a
        // CHOICE), try to remove STARs, PLUSes and OPTIONs. Track whether we
        // found something.
        boolean hasOptimisedSubExpr = false;
        for (BasicRegexp operand : re.mOperands) {
            switch (operand.getOperator()) {
            case NONE:
                optimisedOperands.add(operand);
                break;
            case STAR:
            case PLUS:
            case OPTION:
                // Remove STAR, PLUS and OPTION
                BasicRegexp subExpr = operand.mOperands.get(0);
                if (subExpr.isSingleChar()) {
                    optimisedOperands.add(subExpr);
                } else {
                    // Check if by getting unwrapping this, we uncover another
                    // CHOICE or nullable SEQUENCE
                    optimiseStarOnSC(subExpr, optimisedOperands);
                }

                hasOptimisedSubExpr = true;
                break;
            case SEQUENCE:
                hasOptimisedSubExpr = optimiseStarOnSC(operand,
                    optimisedOperands);
                break;
            case CHOICE:
                optimiseStarOnSC(operand, optimisedOperands);
                hasOptimisedSubExpr = true;
                break;
            default:
            }
        }
        return hasOptimisedSubExpr;
    }

    private BasicRegexp optimiseStar()
    {
        BasicRegexp subExpr = mOperands.get(0);
        BasicRegexp subExprOptimised = subExpr.optimise();
        switch (subExprOptimised.getOperator()) {
        case NONE:
            if (subExpr == subExprOptimised) {
                // No optimisations made
                return this;
            } else {
                return new BasicRegexp(subExprOptimised, RegexpOperator.STAR);
            }
        case STAR:
            return subExprOptimised;
        case PLUS:
        case OPTION:
            return new BasicRegexp(subExprOptimised.mOperands.get(0), 
                RegexpOperator.STAR);
        case SEQUENCE:
        case CHOICE: {
            ArrayList<BasicRegexp> optimisedOperands = new ArrayList<>();
            boolean hasOptimisedSubExpr = optimiseStarOnSC(subExprOptimised,
                optimisedOperands);
            if (!hasOptimisedSubExpr && subExpr == subExprOptimised) {
                // No optimisations made
                return this;
            } else if (!hasOptimisedSubExpr) {
                if (subExprOptimised.getOperator() == RegexpOperator.CHOICE) {
                    // Optimisation made on sub expression only
                    return new BasicRegexp(subExprOptimised,
                        RegexpOperator.STAR);
                } else {
                    // Couldn't make any optimisation on SEQUENCE including the
                    // possibility of translating it into a CHOICE, however
                    // other optimisations were made on the SEQUENCE: e.g.
                    // (abc**)* ------> (abc*)* -------> Can't optimise further
                    return new BasicRegexp(subExprOptimised,
                        RegexpOperator.STAR);
                }
            } else {
                // Able to optimise yet further, this optimisation may have
                // opened up yet further optimisations, e.g. we get (a|b|b|c)*
                // which we can translate to (a|b|c)*
                BasicRegexp newExprOptimised = new BasicRegexp(
                    optimisedOperands, RegexpOperator.CHOICE);
                newExprOptimised = newExprOptimised.optimise();
                return new BasicRegexp(newExprOptimised, RegexpOperator.STAR);
            }
        }
        default:
            throw new RuntimeException("BUG: Should be unreachable.");
        }
    }

    private BasicRegexp optimisePlus()
    {
        BasicRegexp subExpr = mOperands.get(0);
        BasicRegexp subExprOptimised = subExpr.optimise();
        switch (subExprOptimised.getOperator()) {
        case NONE:
            if (subExpr == subExprOptimised) {
                // No optimisations made
                return this;
            } else {
                return new BasicRegexp(subExprOptimised, RegexpOperator.PLUS);
            }
        case STAR:
            return subExprOptimised;
        case PLUS:
            return subExprOptimised;
        case OPTION:
            return new BasicRegexp(subExprOptimised.mOperands.get(0), 
                RegexpOperator.STAR);
        case SEQUENCE: {
            // IDEA(mjn33): Improve optimisations, when I have the time. PLUS
            // is a lower priority than STAR
            boolean allNullable = subExprOptimised.isNullable();
            if (!allNullable && subExpr == subExprOptimised) {
                // No optimisations made
                return this;
            } else if (!allNullable) {
                // Optimisation made on sub expression
                return new BasicRegexp(subExprOptimised, mOperator);
            } else {
                return new BasicRegexp(
                    new BasicRegexp(subExprOptimised.mOperands,
                        RegexpOperator.CHOICE), RegexpOperator.PLUS);
            }
        }
        case CHOICE:
            if (subExpr == subExprOptimised) {
                // No optimisations made
                return this;
            } else {
                // Optimisation made on sub expression
                return new BasicRegexp(subExprOptimised, RegexpOperator.STAR);
            }
        default:
            throw new RuntimeException("BUG: Should be unreachable.");
        }
    }

    private BasicRegexp optimiseOption()
    {
        BasicRegexp subExpr = mOperands.get(0);
        BasicRegexp subExprOptimised = subExpr.optimise();

        if (subExprOptimised.isNullable()) {
            return subExprOptimised;
        } else if (subExprOptimised.getOperator() == RegexpOperator.PLUS) {
            // r+? ----> r*
            return new BasicRegexp(subExprOptimised.mOperands.get(0),
                RegexpOperator.STAR);
        } else if (subExprOptimised.getOperator() == RegexpOperator.NONE) {
            if (subExpr == subExprOptimised) {
                // No optimisations made
                return this;
            } else {
                return new BasicRegexp(subExprOptimised, RegexpOperator.OPTION);
            }
        } else {
            return this;
        }
    }

    /**
     * Factored out of optimiseSequence(), tests whether as an optimisation we
     * can merge two adjacent expressions into one (and which one).
     * @param a The first expression
     * @param b The second expression
     * @return -1 if these two expressions cannot be merged, otherwise returns
     * 0 if the first expression should be removed, or 1 if the second
     * expression should be removed
     */
    private int couldMergeSequence(BasicRegexp a, BasicRegexp b)
    {
        int ret = -1;
        if (a.getOperator() == RegexpOperator.STAR &&
            b.getOperator() == RegexpOperator.STAR) {
            // r*r* ------> r*
            ret = 1;
        } else if (a.getOperator() == RegexpOperator.STAR &&
                   b.getOperator() == RegexpOperator.OPTION) {
            // r*r? ------> r*
            ret = 1;
        } else if (a.getOperator() == RegexpOperator.STAR &&
                   b.getOperator() == RegexpOperator.PLUS) {
            // r*r+ ------> r+
            ret = 0;
        } else if (a.getOperator() == RegexpOperator.OPTION &&
                   b.getOperator() == RegexpOperator.STAR) {
            // r?r* ------> r*
            ret = 0;
        } else if (a.getOperator() == RegexpOperator.OPTION &&
                   b.getOperator() == RegexpOperator.PLUS) {
            // r?r+ ------> r+
            ret = 0;
        } else if (a.getOperator() == RegexpOperator.PLUS &&
                   b.getOperator() == RegexpOperator.STAR) {
            // r+r* ------> r+
            ret = 1;
        } else if (a.getOperator() == RegexpOperator.PLUS &&
                   b.getOperator() == RegexpOperator.OPTION) {
            // r+r? ------> r+
            ret = 1;
        }

        if (ret < 0) {
            return ret;
        }

        if (a.mOperands.get(0).compareTo(b.mOperands.get(0)) == 0) {
            return ret;
        }

        return -1;
    }

    private BasicRegexp optimiseSequence()
    {
        // Optimise all sub expressions, track if any were *actually* optimised
        ArrayList<BasicRegexp> optimisedOperands = new ArrayList<>();
        boolean hasOptimisedSubExpr = false;
        for (BasicRegexp operand : mOperands) {
            BasicRegexp optimisedOperand = operand.optimise();
            if (operand != optimisedOperand) {
                hasOptimisedSubExpr = true;
            }
            optimisedOperands.add(optimisedOperand);
        }

        // Loop forwards through pairs
        int i = 0;
        while (i < optimisedOperands.size() - 1) {
            int aIdx = i;
            int bIdx = i + 1;
            BasicRegexp a = optimisedOperands.get(aIdx);
            BasicRegexp b = optimisedOperands.get(bIdx);

            int which = couldMergeSequence(a, b);
            if (which == 0) {
                optimisedOperands.remove(aIdx);
            } else if (which == 1) {
                optimisedOperands.remove(bIdx);
            } else {
                i++;
            }
        }

        // Loop backwards through pairs, need to loop backwards as well to
        // simplify cases such as "a?a?a?a*" to "a*"
        i = 0;
        while (i < optimisedOperands.size() - 1) {
            int aIdx = optimisedOperands.size() - 2 - i;
            int bIdx = optimisedOperands.size() - 1 - i;
            BasicRegexp a = optimisedOperands.get(aIdx);
            BasicRegexp b = optimisedOperands.get(bIdx);

            int which = couldMergeSequence(a, b);
            if (which == 0) {
                optimisedOperands.remove(aIdx);
            } else if (which == 1) {
                optimisedOperands.remove(bIdx);
            } else {
                i++;
            }
        }

        if (optimisedOperands.size() == 1) {
            // We simplified this SEQUENCE down to one expression, just return
            // that expression
            return optimisedOperands.get(0);
        } else if (hasOptimisedSubExpr ||
            optimisedOperands.size() < mOperands.size()) {
            return new BasicRegexp(optimisedOperands, RegexpOperator.SEQUENCE);
        } else {
            return this;
        }
    }

    private BasicRegexp optimiseChoice()
    {
        // Optimise all sub expressions, track if any were *actually* optimised
        ArrayList<BasicRegexp> optimisedOperands = new ArrayList<>();
        boolean hasOptimisedSubExpr = false;
        for (BasicRegexp operand : mOperands) {
            BasicRegexp optimisedOperand = operand.optimise();
            if (operand != optimisedOperand) {
                hasOptimisedSubExpr = true;
            }
            optimisedOperands.add(optimisedOperand);
        }

        // Not particularly elegant or efficient, but it is nice to maintain
        // ordering of operands (could be confusing to users if we start
        // reordering things)
        for (int i = 0; i < optimisedOperands.size(); i++) {
            BasicRegexp iExpr = optimisedOperands.get(i);
            int j = i + 1;
            while (j < optimisedOperands.size()) {
                BasicRegexp jExpr = optimisedOperands.get(j);
                if (iExpr.compareTo(jExpr) == 0) {
                    optimisedOperands.remove(j);
                    // Don't increment j
                } else {
                    j++;
                }
            }
        }

        if (optimisedOperands.size() == 1) {
            // We simplified this CHOICE down to one expression, just return
            // that expression
            return optimisedOperands.get(0);
        } else if (hasOptimisedSubExpr ||
            optimisedOperands.size() < mOperands.size()) {
            return new BasicRegexp(optimisedOperands, RegexpOperator.CHOICE);
        } else {
            return this;
        }
    }

    /**
     * Creates an optimised version of this regular expression, if the
     * expression cannot be optimised further this expression is returned.
     * @return The optimised expression
     */
    public BasicRegexp optimise()
    {
        // Step 1: call .optimise() on sub expressions, if a new expression is
        // returned (i.e. we optimised it somehow) we definitely need to create
        // a new BasicRegexp.
        //
        // Step 2: Check for optimisations (based on Table 1: Optimisations
        // from Stefan's paper):
        //   * (r*)* -----> r*
        //   * (r+)* -----> r*
        //   * (r?)* -----> r*
        //
        //   * (r*)+ -----> r*
        //   * (r?)+ -----> r*
        //   * (r+)+ -----> r+
        //
        //   * p?    -----> p (if p is nullable)
        //
        //   * (pq)* -----> (p|q)* (if p and q are nullable)
        //   * (pq)+ -----> (p|q)+ (if p and q are nullable)
        // Sequence based optimisations (any part which contains):
        //   * r*r*  -----> r*
        //   * r*r?  -----> r*
        //   * r?r*  -----> r*
        //   * r+r*  -----> r+
        //   * r*r+  -----> r+
        // Choice based optimisations (no duplicate sub expressions):
        //   * a|b|a       -----> a|b
        //   * (a*b*c?)*   -----> (a|b|c)*
        //
        // Star-on-sequence-or-choice optimisations:
        //   * (a*b*c*d*)* -----> (a|b|c|d)*
        //
        // Step 3: Reconstruct if necessary, return "this" is no optimisation
        // performed.

        switch (mOperator) {
        case NONE:
            return this;
        case STAR:
            return optimiseStar();
        case PLUS:
            return optimisePlus();
        case OPTION:
            return optimiseOption();
        case SEQUENCE:
            return optimiseSequence();
        case CHOICE:
            return optimiseChoice();
        default:
            throw new RuntimeException("BUG: Should be unreachable.");
        }
    }

    /**
     * For debugging purposes, prints out the tree of BasicRegexp objects,
     * formatted nicely
     *
     * @param indent Current indentation
     * @param re The BasicRegexp to print out
     */
    public static void debugPrintBasicRegexp(int indent, BasicRegexp re)
    {
        String indentStr = "";
        for (int i = 0; i < indent; i++) {
            indentStr += "    ";
        }

        String opStr = re.getOperator().name();
        if (re.isSingleChar()) {
            System.out.println(indentStr + "[BasicRegexp:" + opStr + "] char = "
                + re.mChar);
        } else {
            System.out.println(indentStr + "[BasicRegexp:" + opStr + "] {");
            for (int i = 0; i < re.mOperands.size(); i++) {
                debugPrintBasicRegexp(indent + 1, re.mOperands.get(i));
            }
            System.out.println(indentStr + "}");
        }
    }
}
