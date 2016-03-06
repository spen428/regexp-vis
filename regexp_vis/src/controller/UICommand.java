package controller;

import java.util.ArrayList;

import javafx.geometry.Point2D;
import model.AddStateCommand;
import model.AddTransitionCommand;
import model.Automaton;
import model.AutomatonTransition;
import model.BreakdownChoiceCommand;
import model.BreakdownCommand;
import model.BreakdownIterationCommand;
import model.BreakdownOptionCommand;
import model.BreakdownSequenceCommand;
import model.Command;
import model.RemoveStateCommand;
import model.RemoveTransitionCommand;
import model.SetIsFinalCommand;
import view.GraphCanvasFX;

/**
 * Extends {@link Command} to facilitate UI-side command history.
 * <p>
 * Calling {@link #undo()} or {@link #redo()} will apply changes to both the
 * {@link GraphCanvasFX} and the {@link Automaton}.
 *
 * @author sp611
 *
 */
public abstract class UICommand extends Command {
    protected final GraphCanvasFX graph;
    protected final Command cmd;
    protected Point2D location = new Point2D(Math.random() * 800,
            Math.random() * 600);

    public UICommand(GraphCanvasFX graph, Command cmd) {
        super(cmd.getAutomaton());
        this.graph = graph;
        this.cmd = cmd;
    }

    /**
     * Convert a {@link Command} into a {@link UICommand}
     *
     * @param graph
     *            the {@link GraphCanvasFX}
     * @param cmd
     *            the {@link Command}
     *
     * @return a new {@link UICommand} that is equivalent to the given
     *         {@link Command}
     */
    public static UICommand fromCommand(GraphCanvasFX graph, Command cmd) {
        if (cmd instanceof AddStateCommand) {
            return new AddStateUICommand(graph, (AddStateCommand) cmd, 0, 0);
        } else if (cmd instanceof AddTransitionCommand) {
            return new AddTransitionUICommand(graph,
                    (AddTransitionCommand) cmd);
        } else if (cmd instanceof BreakdownCommand) {
            return new BreakdownUICommand(graph, (BreakdownCommand) cmd);
        } else if (cmd instanceof RemoveStateCommand) {
            return new RemoveStateUICommand(graph, (RemoveStateCommand) cmd);
        } else if (cmd instanceof RemoveTransitionCommand) {
            return new RemoveTransitionUICommand(graph,
                    (RemoveTransitionCommand) cmd);
        } else if (cmd instanceof SetIsFinalCommand) {
            return new SetIsFinalUICommand(graph, (SetIsFinalCommand) cmd);
        } else if (cmd == null) {
            return null;
        } else {
            String msg = String.format(
                    "Conversion from %s to UICommand has "
                            + "not yet been implemented.",
                    cmd.getClass().toString());
            throw new UnsupportedOperationException(msg);
        }
    }

    public abstract String getDescription();

}
