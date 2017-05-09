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
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class IsolateFinalStateCommand extends CompositeCommand {

    private final List<AutomatonState> mOldFinalStates;
    private final AutomatonState mNewFinalState;
    private final AddStateCommand mNewStateCommand;

    public static IsolateFinalStateCommand create(Automaton automaton)
    {
        boolean needsIsolation = false;
        ArrayList<AutomatonState> finalStates = new ArrayList<>();

        Iterator<Automaton.StateTransitionsPair> it = automaton.graphIterator();
        while (it.hasNext()) {
            Automaton.StateTransitionsPair pair = it.next();
            AutomatonState state = pair.getState();
            if (state.isFinal()) {
                finalStates.add(state);
            }
        }

        if (finalStates.size() > 1) {
            // Multiple final states, need to create an isolated one
            needsIsolation = true;
        } else if (finalStates.size() == 1) {
            // Single final state, only need to do something if it has outgoing
            // transitions
            AutomatonState state = finalStates.get(0);
            if (automaton.hasOutgoingTransition(state)) {
                needsIsolation = true;
            }
        } else {
            // No final states, language of the automaton is the empty set
        }

        if (!needsIsolation) {
            return null;
        }

        return new IsolateFinalStateCommand(automaton, finalStates);
    }

    private IsolateFinalStateCommand(Automaton automaton,
            List<AutomatonState> finalStates)
    {
        super(automaton);
        mOldFinalStates = finalStates;

        // Create the new final state
        mNewFinalState = automaton.createNewState();
        mNewFinalState.setFinal(true);
        mNewStateCommand = new AddStateCommand(automaton, mNewFinalState);
        super.commands.add(mNewStateCommand);

        for (AutomatonState state : finalStates) {
            // Make all the old states not final anymore
            super.commands.add(new SetIsFinalCommand(automaton, state, false));
            // Add an epsilon transition to the new state
            AutomatonTransition newTrans = automaton.createNewTransition(state,
                    mNewFinalState, BasicRegexp.EPSILON_EXPRESSION);
            super.commands.add(new AddTransitionCommand(automaton, newTrans));
        }
    }

    /**
     * @return The command which adds the new (isolated) final state.
     */
    public AddStateCommand getNewStateCommand()
    {
        return mNewStateCommand;
    }

    /**
     * @return The list of the original final states.
     */
    public List<AutomatonState> getOldFinalStates()
    {
        return Collections.unmodifiableList(mOldFinalStates);
    }

    /**
     * @return The new (isolated) final state.
     */
    public AutomatonState getNewFinalState()
    {
        return mNewFinalState;
    }

}
