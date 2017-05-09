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
import java.util.Collections;
import java.util.List;

/**
 * Command to conjoin parallel transitions which come from one state and go to
 * another (could be the same state, in which case loops are being conjoined).
 * Part of the NFA to Regexp translation.
 */
public class ConjoinParallelTransitionsCommand extends CompositeCommand {

    private final AutomatonState mFrom;
    private final AutomatonState mTo;
    private final ArrayList<AutomatonTransition> mParallelTrans;
    private final AutomatonTransition mNewTransition;

    public ConjoinParallelTransitionsCommand(Automaton automaton,
            AutomatonState from, AutomatonState to)
    {
        super(automaton);
        mFrom = from;
        mTo = to;

        // Create a list of all parallel transitions originating from this
        // state. Also create a list of the BasicRegexp of these operands.
        mParallelTrans = new ArrayList<>();
        ArrayList<BasicRegexp> operands = new ArrayList<>();
        for (AutomatonTransition t : automaton.getStateTransitions(mFrom)) {
            if (t.getTo() == mTo) {
                mParallelTrans.add(t);
                super.commands.add(new RemoveTransitionCommand(automaton, t));
                operands.add(t.getData());
            }
        }

        BasicRegexp newRe = new BasicRegexp(operands,
                BasicRegexp.RegexpOperator.CHOICE);
        // Do very low depth optimisation
        newRe = newRe.optimise(BasicRegexp.OPTIMISE_ALL, 1);
        mNewTransition = automaton.createNewTransition(mFrom, mTo,
                newRe);
        super.commands.add(new AddTransitionCommand(automaton, mNewTransition));
    }

    /**
     * @return The list of parallel transitions we are conjoining.
     */
    public List<AutomatonTransition> getParallelTransitions()
    {
        return Collections.unmodifiableList(mParallelTrans);
    }

    /**
     * @return The transition which is the the result of conjoining
     */
    public AutomatonTransition getNewTransition()
    {
        return mNewTransition;
    }

    /**
     * @return The state of which we are conjoining parallel transitions from.
     */
    public AutomatonState getStateFrom()
    {
        return mFrom;
    }

    /**
     * @return The state of which we are conjoining parallel transitions to.
     */
    public AutomatonState getStateTo()
    {
        return mTo;
    }

}
