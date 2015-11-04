package model;

import java.util.*;

public class AddStateCommand extends Command {
    AutomatonState mState;

    public AddStateCommand(Automaton automaton, AutomatonState state)
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
        getAutomaton().addStateWithTransitions(mState, new LinkedList<AutomatonTransition>());
    }

    public void undo()
    {
        getAutomaton().removeState(mState);
    }
}
