package model;

import java.util.*;

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

    public void redo()
    {
        mTransitions = getAutomaton().removeState(mState);
    }

    public void undo()
    {
        getAutomaton().addStateWithTransitions(mState, mTransitions);
    }

    public LinkedList<AutomatonTransition> getTransitions()
    {
        return mTransitions;
    }
}
