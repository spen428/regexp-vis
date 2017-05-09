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

import java.util.LinkedList;

/**
 * Command to remove a state and its outgoing transitions from an automaton
 */
public class RemoveStateCommand extends Command {
    private final AutomatonState mState;
    private LinkedList<AutomatonTransition> mTransitions;

    public RemoveStateCommand(Automaton automaton, AutomatonState state)
    {
        super(automaton);
        mState = state;
    }

    /**
     * @return The state which is to be removed
     */
    public AutomatonState getState()
    {
        return mState;
    }

    @Override
    public void redo()
    {
        mTransitions = getAutomaton().removeState(mState);
    }

    @Override
    public void undo()
    {
        getAutomaton().addStateWithTransitions(mState, mTransitions);
    }

    public LinkedList<AutomatonTransition> getTransitions()
    {
        return mTransitions;
    }
}
