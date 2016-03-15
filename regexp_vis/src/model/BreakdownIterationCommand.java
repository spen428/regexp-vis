package model;

import java.util.List;

/**
 * Command which breaks down an iteration expression (e.g. "a+" or "a*") into a
 * set of simpler transitions. For the regular expression to NFA conversion
 * process.
 */
public class BreakdownIterationCommand extends BreakdownCommand {
    public enum IsolationLevel {
        /**
         * Don't isolate the translation at all
         */
        NONE,
        /**
         * Isolate the translation from the start state
         */
        START_ISOLATE,
        /**
         * Isolate the translation from the end state
         */
        END_ISOLATE,
        /**
         * Isolate the translation from both the start and end states
         */
        FULLY_ISOLATE
    }

    private final IsolationLevel mIsolationLevel;

    /**
     * Calculates the best isolation level a transition can have, often we can
     * performing "fully safe" translations
     * @param automaton The automaton of transition "t"
     * @param t The transition in question
     * @return The optimal isolation level for this transition
     */
    public static IsolationLevel calcBestIsolationLevel(Automaton automaton,
        AutomatonTransition t)
    {
        AutomatonState start = t.getFrom();
        AutomatonState end = t.getTo();
        List<AutomatonTransition> startOutgoing = automaton
                .getStateTransitions(start);
        List<AutomatonTransition> endIncoming = automaton
                .getIngoingTransition(end);
        boolean isolateStart = false;
        boolean isolateEnd = false;
        if (startOutgoing.size() > 1) {
            // Start has other out-going transitions, not including this one.
            // Need to isolate it.
            isolateStart = true;
        }
        if (endIncoming.size() > 1) {
            // End has other in-going transitions, not including this one. Need
            // to isolate it.
            isolateEnd = true;
        }

        if (isolateStart && isolateEnd) {
            return IsolationLevel.FULLY_ISOLATE;
        } else if (isolateStart) {
            return IsolationLevel.START_ISOLATE;
        } else if (isolateEnd) {
            return IsolationLevel.END_ISOLATE;
        } else {
            return IsolationLevel.NONE;
        }
    }

    /**
     * @param optimal The optimal isolation level as returned by
     * calcBestIsolationLevel()
     * @param desired The desired isolation level
     * @return true if the given desired isolation level is compatible with the
     * optimal isolation level
     */
    public static boolean isolationSufficent(IsolationLevel optimal,
        IsolationLevel desired)
    {
        if (optimal == desired) {
            return true;
        }

        switch (optimal) {
        case NONE:
            return true;
        case START_ISOLATE:
            return optimal == IsolationLevel.FULLY_ISOLATE;
        case END_ISOLATE:
            return optimal == IsolationLevel.FULLY_ISOLATE;
        case FULLY_ISOLATE:
            return false;
        default:
            // Unreachable
            return false;
        }
    }

    /**
     * @param automaton The automaton for the transition
     * @param t the transition to break down
     * @param level the desired IsolationLevel for this breakdown operation
     * @throws IllegalArgumentException if the transition expression isn't STAR
     * or PLUS
     * @throws IllegalArgumentException if the desired isolation level would
     * not result in a correct translation
     */
    public BreakdownIterationCommand(Automaton automaton, AutomatonTransition t,
        IsolationLevel level)
    {
        super(automaton, t);
        mIsolationLevel = level;

        BasicRegexp re = (BasicRegexp)t.getData();

        if (re.getOperator() != BasicRegexp.RegexpOperator.STAR &&
            re.getOperator() != BasicRegexp.RegexpOperator.PLUS) {
            throw new IllegalArgumentException(
                "BreakdownIterationCommand must be passed either " +
                "the PLUS or STAR operators, i.e. \"a+\" or \"a*\"");
        }

        if (!isolationSufficent(calcBestIsolationLevel(automaton, t), level)) {
            // Given isolation level insufficient, throw.
            throw new IllegalArgumentException(
                "Bad isolation level passed, see isolationSufficent() method.");
        }

        switch (level) {
        case NONE:
            createUnisolated(re);
            break;
        case START_ISOLATE:
            createStartIsolated(re);
            break;
        case END_ISOLATE:
            createEndIsolated(re);
            break;
        case FULLY_ISOLATE:
            createFullyIsolated(re);
            break;
        default:
            throw new RuntimeException("BUG: Unreachable");
        }
    }

    /**
     * @return The isolation level of this breakdown operation
     * @see IsolationLevel
     */
    public IsolationLevel getIsolationLevel()
    {
        return mIsolationLevel;
    }



    private void createEndIsolated(BasicRegexp re)
    {
        Automaton automaton = getAutomaton();
        AutomatonTransition t = getOriginalTransition();
        AutomatonState start = t.getFrom();
        AutomatonState end = t.getTo();

        mCommands.add(new RemoveTransitionCommand(automaton, t));
        AutomatonState endIsolated = automaton.createNewState();

        mCommands.add(new AddStateCommand(automaton, endIsolated));
        AutomatonTransition endTrans, epsilonSkipTrans = null,
                epsilonBackwardTrans;

        endTrans = automaton.createNewTransition(endIsolated, end,
            BasicRegexp.EPSILON_EXPRESSION);
        // Transition which skips over the iteration (only for STAR)
        if (re.getOperator() == BasicRegexp.RegexpOperator.STAR) {
            // Don't create another epsilon transition if one already exists
            if (!TranslationTools.hasCharacterTrans(getAutomaton(), start, end,
                    BasicRegexp.EPSILON_CHAR)) {
                epsilonSkipTrans = automaton.createNewTransition(start, end,
                        BasicRegexp.EPSILON_EXPRESSION);
            }
        }
        // Epsilon transition for iteration
        epsilonBackwardTrans = automaton.createNewTransition(endIsolated,
            start, BasicRegexp.EPSILON_EXPRESSION);

        mCommands.add(new AddTransitionCommand(automaton, endTrans));
        if (epsilonSkipTrans != null) {
            mCommands.add(new AddTransitionCommand(automaton, epsilonSkipTrans));
        }
        mCommands.add(new AddTransitionCommand(automaton, epsilonBackwardTrans));

        // The transition for the subexpression itself
        BasicRegexp operand = re.getOperands().get(0);
        AutomatonTransition subexprTrans = automaton.createNewTransition(
            start, endIsolated, operand);

        mCommands.add(new AddTransitionCommand(automaton, subexprTrans));
    }

    private void createStartIsolated(BasicRegexp re)
    {
        Automaton automaton = getAutomaton();
        AutomatonTransition t = getOriginalTransition();
        AutomatonState start = t.getFrom();
        AutomatonState end = t.getTo();

        mCommands.add(new RemoveTransitionCommand(automaton, t));
        AutomatonState startIsolated = automaton.createNewState();

        mCommands.add(new AddStateCommand(automaton, startIsolated));
        AutomatonTransition startTrans, epsilonSkipTrans = null,
                epsilonBackwardTrans;

        startTrans = automaton.createNewTransition(start, startIsolated,
            BasicRegexp.EPSILON_EXPRESSION);
        // Transition which skips over the iteration (only for STAR)
        if (re.getOperator() == BasicRegexp.RegexpOperator.STAR) {
            // Don't create another epsilon transition if one already exists
            if (!TranslationTools.hasCharacterTrans(getAutomaton(), start, end,
                    BasicRegexp.EPSILON_CHAR)) {
                epsilonSkipTrans = automaton.createNewTransition(start, end,
                        BasicRegexp.EPSILON_EXPRESSION);
            }
        }
        // Epsilon transition for iteration
        epsilonBackwardTrans = automaton.createNewTransition(end,
            startIsolated, BasicRegexp.EPSILON_EXPRESSION);

        mCommands.add(new AddTransitionCommand(automaton, startTrans));
        if (epsilonSkipTrans != null) {
            mCommands.add(new AddTransitionCommand(automaton, epsilonSkipTrans));
        }
        mCommands.add(new AddTransitionCommand(automaton, epsilonBackwardTrans));

        // The transition for the subexpression itself
        BasicRegexp operand = re.getOperands().get(0);
        AutomatonTransition subexprTrans = automaton.createNewTransition(
            startIsolated, end, operand);

        mCommands.add(new AddTransitionCommand(automaton, subexprTrans));
    }

    private void createFullyIsolated(BasicRegexp re)
    {
        Automaton automaton = getAutomaton();
        AutomatonTransition t = getOriginalTransition();
        AutomatonState start = t.getFrom();
        AutomatonState end = t.getTo();

        mCommands.add(new RemoveTransitionCommand(automaton, t));
        AutomatonState startIsolated = automaton.createNewState();
        AutomatonState endIsolated = automaton.createNewState();

        mCommands.add(new AddStateCommand(automaton, startIsolated));
        mCommands.add(new AddStateCommand(automaton, endIsolated));
        AutomatonTransition startTrans, endTrans, epsilonSkipTrans = null,
            epsilonBackwardTrans;

        startTrans = automaton.createNewTransition(start, startIsolated,
            BasicRegexp.EPSILON_EXPRESSION);
        endTrans = automaton.createNewTransition(endIsolated, end,
            BasicRegexp.EPSILON_EXPRESSION);
        // Transition which skips over the iteration (only for STAR)
        if (re.getOperator() == BasicRegexp.RegexpOperator.STAR) {
            // Don't create another epsilon transition if one already exists
            if (!TranslationTools.hasCharacterTrans(getAutomaton(), start, end,
                    BasicRegexp.EPSILON_CHAR)) {
                epsilonSkipTrans = automaton.createNewTransition(start, end,
                        BasicRegexp.EPSILON_EXPRESSION);
            }
        }
        // Epsilon transition for iteration
        epsilonBackwardTrans = automaton.createNewTransition(endIsolated,
            startIsolated, BasicRegexp.EPSILON_EXPRESSION);

        mCommands.add(new AddTransitionCommand(automaton, startTrans));
        mCommands.add(new AddTransitionCommand(automaton, endTrans));
        if (epsilonSkipTrans != null) {
            mCommands.add(new AddTransitionCommand(automaton, epsilonSkipTrans));
        }
        mCommands.add(new AddTransitionCommand(automaton, epsilonBackwardTrans));

        // The transition for the subexpression itself
        BasicRegexp operand = re.getOperands().get(0);
        AutomatonTransition subexprTrans = automaton.createNewTransition(
            startIsolated, endIsolated, operand);

        mCommands.add(new AddTransitionCommand(automaton, subexprTrans));
    }

    private void createUnisolated(BasicRegexp re)
    {
        Automaton automaton = getAutomaton();
        AutomatonTransition t = getOriginalTransition();
        AutomatonState start = t.getFrom();
        AutomatonState end = t.getTo();

        mCommands.add(new RemoveTransitionCommand(automaton, t));

        AutomatonTransition epsilonSkipTrans = null;

        // Epsilon transition for iteration, forwards for STAR, backwards for
        // PLUS
        if (re.getOperator() == BasicRegexp.RegexpOperator.STAR) {
            // Don't create another epsilon transition if one already exists
            if (!TranslationTools.hasCharacterTrans(getAutomaton(), start, end,
                    BasicRegexp.EPSILON_CHAR)) {
                epsilonSkipTrans = automaton.createNewTransition(start, end,
                        BasicRegexp.EPSILON_EXPRESSION);
            }
        } else {
            // Don't create another epsilon transition if one already exists
            if (!TranslationTools.hasCharacterTrans(getAutomaton(), end, start,
                    BasicRegexp.EPSILON_CHAR)) {
                epsilonSkipTrans = automaton.createNewTransition(end, start,
                        BasicRegexp.EPSILON_EXPRESSION);
            }
        }

        if (epsilonSkipTrans != null) {
            mCommands.add(new AddTransitionCommand(automaton, epsilonSkipTrans));
        }

        // The transition for the subexpression itself
        BasicRegexp operand = re.getOperands().get(0);
        AutomatonTransition subexprTrans;
        if (re.getOperator() == BasicRegexp.RegexpOperator.STAR) {
            subexprTrans = automaton.createNewTransition(end, start, operand);
        } else {
            subexprTrans = automaton.createNewTransition(start, end, operand);
        }

        mCommands.add(new AddTransitionCommand(automaton, subexprTrans));
    }
}
