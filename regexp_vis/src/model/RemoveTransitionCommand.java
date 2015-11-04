package model;

class RemoveTransitionCommand extends Command {
    private AutomatonTransition mTransition;

    public RemoveTransitionCommand(Automaton automaton, AutomatonTransition transition)
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
