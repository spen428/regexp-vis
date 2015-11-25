package ui;

import model.Automaton;
import model.Command;

/**
 * Extends {@link Command} to facilitate UI-side command history.
 * <p>
 * Calling {@link #undo()} or {@link #redo()} will apply changes to both the
 * {@link Graph} and the {@link Automaton}.
 * 
 * @author sp611
 *
 */
public abstract class UICommand extends Command {

    protected final Graph graph;
    protected final Command cmd;

    public UICommand(Graph graph, Command cmd) {
        super(cmd.getAutomaton());
        this.graph = graph;
        this.cmd = cmd;
    }

}
