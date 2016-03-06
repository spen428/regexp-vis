package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public class CompositeCommand extends Command {

    protected final ArrayList<Command> commands;

    public CompositeCommand(Automaton automaton) {
        super(automaton);
        this.commands = new ArrayList<>();
    }

    /**
     * @return the list of commands which this command executes, as an
     *         unmodifiable list
     */
    public List<Command> getCommands() {
        return Collections.unmodifiableList(this.commands);
    }

    @Override
    public void undo() {
        for (Command c : this.commands) {
            c.undo();
        }
    }

    @Override
    public void redo() {
        for (Command c : this.commands) {
            c.redo();
        }
    }

}
