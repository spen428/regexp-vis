package model;

import java.util.LinkedList;

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

    @Override
    public void redo()
    {
        getAutomaton().addStateWithTransitions(mState,
            new LinkedList<AutomatonTransition>());
    }

    @Override
    public void undo()
    {
        getAutomaton().removeState(mState);
    }
}
