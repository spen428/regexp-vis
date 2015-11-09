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

    public void redo()
    {
        getAutomaton().removeTransition(mTransition);
    }

    public void undo()
    {
        getAutomaton().addTransition(mTransition);
    }
}
