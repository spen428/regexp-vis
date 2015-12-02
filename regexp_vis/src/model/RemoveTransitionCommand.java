package model;

/**
 * Command to remove a transition from an automaton
 */
public class RemoveTransitionCommand extends Command {
    private final AutomatonTransition mTransition;

    public RemoveTransitionCommand(Automaton automaton,
        AutomatonTransition transition)
    {
        super(automaton);
        mTransition = transition;
    }

    @Override
    public void redo()
    {
        getAutomaton().removeTransition(mTransition);
    }

    @Override
    public void undo()
    {
        getAutomaton().addTransition(mTransition);
    }

    public AutomatonTransition getTransition()
    {
        return mTransition;
    }
}
