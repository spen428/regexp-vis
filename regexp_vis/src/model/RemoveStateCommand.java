package model;

import java.util.*;

class RemoveStateCommand extends Command {
    private AutomatonState mState;
    private LinkedList<AutomatonTransition> mTransitions;

    public RemoveStateCommand(Automaton automaton, AutomatonState state)
    {
        super(automaton);
        mState = state;
    }

    public AutomatonState getState()
    {
        return mState;
    }

    public void redo()
    {
        Automaton a = getAutomaton();
        mTransitions = a.removeState(mState);
    }

    public void undo()
    {
        Automaton a = getAutomaton();
        a.addStateWithTransitions(mState, mTransitions);
    }
}
