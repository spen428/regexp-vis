package model;

public class AddTransitionCommand extends Command {
    private AutomatonTransition mTransition;

    public AddTransitionCommand(Automaton automaton, AutomatonTransition transition) 
    {
        super(automaton);
        mTransition = transition;
    }

    public AutomatonTransition getTransition()
    {
        return mTransition;
    }

    public void redo()
    {
        // mjn33: WAS: old idea
        // Automaton a = getAutomaton();
        // if (mTransition == null)
        //     mTransition = a.addTransition(from, to, data);
        // else
        //     a.addTransition(mTransition);
        getAutomaton().addTransition(mTransition);
    }

    public void undo()
    {
        getAutomaton().removeTransition(mTransition);
    }
}
