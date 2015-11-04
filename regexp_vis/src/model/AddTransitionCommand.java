package model;

/**
 * Command to add a transition to an automaton
 */
public class AddTransitionCommand extends Command {
    private AutomatonTransition mTransition;

    public AddTransitionCommand(Automaton automaton, AutomatonTransition transition) 
    {
        super(automaton);
        mTransition = transition;
    }

    /**
     * @return The transition which is to be added
     */
    public AutomatonTransition getTransition()
    {
        return mTransition;
    }

    public void redo()
    {
        getAutomaton().addTransition(mTransition);
    }

    public void undo()
    {
        getAutomaton().removeTransition(mTransition);
    }
}
