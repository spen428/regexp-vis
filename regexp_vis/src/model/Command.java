package model;

abstract class Command {
    private Automaton mAutomaton;

    public Command(Automaton automaton)
    {
        mAutomaton = automaton;
    }

    public Automaton getAutomaton()
    {
        return mAutomaton;
    }

    abstract void undo();
    abstract void redo();
}
