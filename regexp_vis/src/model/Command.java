package model;

/**
 * The abstract base class encapsulate modifications performed on an Automaton
 * object.
 *
 * This uses the "Command" design pattern, hence the name. The CommandHistory
 * class can record the history of commands to provide undo and redo.
 *
 * @see CommandHistory
 */
public abstract class Command {
    private final Automaton mAutomaton;

    public Command(Automaton automaton) {
        mAutomaton = automaton;
    }

    /**
     * @return The automaton this command is associated with.
     */
    public Automaton getAutomaton() {
        return mAutomaton;
    }

    /**
     * This performs the opposite of the redo() method, should return the state
     * of the Automaton to exactly as it was before redo() was executed for the
     * first time. Calling undo() if the command hasn't been done, or has
     * already been "undone" is undefined.
     */
    public abstract void undo();
    /**
     * This performs the command. Also used to execute the command for the first
     * time (despite the name "redo"). Calling redo() if the command has already
     * been "done" is undefined.
     */
    public abstract void redo();
}
