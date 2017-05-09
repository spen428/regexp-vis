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
import java.util.List;

/**
 * Command to remove a state and sequence the in-going transitions for that
 * state with the out-going transitions for that state. Part of the NFA to
 * Regexp translation.
 */
public class SequenceStateTransitionsCommand extends CompositeCommand {

    private final AutomatonState mState;
    private final ArrayList<AutomatonTransition> mNewTransitions;

    public SequenceStateTransitionsCommand(Automaton automaton,
            AutomatonState state)
    {
        super(automaton);
        mState = state;
        mNewTransitions = new ArrayList<>();

        List<AutomatonTransition> ingoingTrans = automaton
                .getIngoingTransition(mState);
        List<AutomatonTransition> outgoingTrans = automaton
                .getStateTransitions(mState);

        // For every combination of pairing in-going with out-going transitions
        // for this state: combine the regexps of the transitions using sequence
        // via a transition which bypasses this state.
        for (AutomatonTransition t1 : ingoingTrans) {
            for (AutomatonTransition t2 : outgoingTrans) {
                ArrayList<BasicRegexp> operands = new ArrayList<>();
                operands.add(t1.getData());
                operands.add(t2.getData());
                BasicRegexp newRe = new BasicRegexp(operands,
                        BasicRegexp.RegexpOperator.SEQUENCE);
                // Do very low depth optimisation
                newRe = newRe.optimise(BasicRegexp.OPTIMISE_ALL, 1);
                AutomatonTransition newTrans = automaton
                        .createNewTransition(t1.getFrom(), t2.getTo(), newRe);
                super.commands.add(new AddTransitionCommand(automaton, newTrans));
                mNewTransitions.add(newTrans);
            }
        }

        // Remove all in-going transitions
        for (AutomatonTransition t : ingoingTrans) {
            super.commands.add(new RemoveTransitionCommand(automaton, t));
        }
        // Remove all out-going transitions
        for (AutomatonTransition t : outgoingTrans) {
            super.commands.add(new RemoveTransitionCommand(automaton, t));
        }
        // Remove the state itself
        super.commands.add(new RemoveStateCommand(automaton, mState));
    }

    /**
     * @return The list of new transitions from sequencing the in-going and
     * out-going transitions of the target state.
     */
    public List<AutomatonTransition> getSequencedTransitions()
    {
        return mNewTransitions;
    }

    /**
     * @return The state which we are removing.
     */
    public AutomatonState getState()
    {
        return mState;
    }

}
