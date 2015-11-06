package model;

import java.util.*;

/**
 * Command to add a state to an automaton
 */
public class AddStateCommand extends Command {
    private final AutomatonState mState;

    public AddStateCommand(Automaton automaton, AutomatonState state)
    {
        super(automaton);
        mState = state;
    }

    /**
     * @return The state which is to be added
     */
    public AutomatonState getState()
    {
        return mState;
    }

    public void redo()
    {
        getAutomaton().addStateWithTransitions(mState,
            new LinkedList<AutomatonTransition>());
    }

    public void undo()
    {
        getAutomaton().removeState(mState);
    }
}
