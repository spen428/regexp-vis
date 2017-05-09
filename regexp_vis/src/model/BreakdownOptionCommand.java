/*
 * Copyright (c) 2015, 2016 Matthew J. Nicholls, Samuel Pengelly,
 * Parham Ghassemi, William R. Dix
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package model;

/**
 * Command which breaks down an optional expression (e.g. "a?") into a set of
 * simpler transitions. For the regular expression to NFA conversion process.
 */
public class BreakdownOptionCommand extends BreakdownCommand {
    /**
     * @param automaton The automaton for the transition
     * @param t The transition to break down
     * @throws IllegalArgumentException if the transition expression isn't
     * OPTION
     */
    public BreakdownOptionCommand(Automaton automaton, AutomatonTransition t)
    {
        super(automaton, t);

        BasicRegexp re = t.getData();
        AutomatonState from = t.getFrom();
        AutomatonState to = t.getTo();

        if (re.getOperator() != BasicRegexp.RegexpOperator.OPTION) {
            throw new IllegalArgumentException(
                "BreakdownOptionCommand must be passed an option " +
                "operator, i.e. \"a?\"");
        }

        // Same as choice between epsilon or the expression
        super.commands.add(new RemoveTransitionCommand(automaton, t));

        BasicRegexp operand = re.getOperands().get(0);
        AutomatonTransition epsilonForwardTrans = null;
        if (!TranslationTools.hasCharacterTrans(getAutomaton(), from, to,
                BasicRegexp.EPSILON_CHAR)) {
            epsilonForwardTrans = automaton.createNewTransition(
                    from, to, BasicRegexp.EPSILON_EXPRESSION);
        }
        AutomatonTransition subexprTrans = automaton.createNewTransition(from,
            to, operand);

        if (epsilonForwardTrans != null) {
            super.commands.add(new AddTransitionCommand(automaton,
                    epsilonForwardTrans));
        }
        super.commands.add(new AddTransitionCommand(automaton, subexprTrans));
    }
}
