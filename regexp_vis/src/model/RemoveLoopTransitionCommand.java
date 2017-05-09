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

import java.util.ArrayList;

/**
 * Command to a single loop for a state, this is done by prepending the loop
 * expression wrapped in a STAR operator. Part of the NFA to Regexp translation.
 *
 * Note this Command assumes the only loop transition is the given one.
 */
public class RemoveLoopTransitionCommand extends CompositeCommand {

    private final AutomatonTransition mTransition;

    public RemoveLoopTransitionCommand(Automaton automaton,
            AutomatonTransition trans)
    {
        super(automaton);
        mTransition = trans;

        AutomatonState state = mTransition.getFrom();
        // Wrap the loop regular expression into a STAR operator
        BasicRegexp loopRe = mTransition.getData();
        loopRe = new BasicRegexp(loopRe, BasicRegexp.RegexpOperator.STAR);

        // Actually remove the loop
        super.commands.add(new RemoveTransitionCommand(automaton, mTransition));

        for (AutomatonTransition t : automaton.getStateTransitions(state)) {
            if (t == mTransition) {
                continue;
            }
            // Form the new regexp by prepending the loop regular expression
            // onto the transition regexp
            ArrayList<BasicRegexp> operands = new ArrayList<>();
            operands.add(loopRe);
            operands.add(t.getData());
            BasicRegexp newRe = new BasicRegexp(operands,
                    BasicRegexp.RegexpOperator.SEQUENCE);
            // Do very low depth optimisation
            newRe = newRe.optimise(BasicRegexp.OPTIMISE_ALL, 1);
            AutomatonTransition newTrans = automaton.createNewTransition(state,
                    t.getTo(), newRe);
            // Remove the old and add the new
            super.commands.add(new RemoveTransitionCommand(automaton, t));
            super.commands.add(new AddTransitionCommand(automaton, newTrans));
        }
    }

    /**
     * @return Returns the loop transition which is removed.
     */
    public AutomatonTransition getLoopTransition()
    {
        return mTransition;
    }

}
