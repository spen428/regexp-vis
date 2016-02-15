package controller;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import model.BreakdownCommand;
import view.GraphCanvasFX;

/**
 * The equivalent of {@link BreakdownCommand} for the UI side of things.
 *
 * @author sp611
 *
 */
public abstract class BreakdownUICommand extends UICommand {

    protected final LinkedList<UICommand> commands;

    public BreakdownUICommand(GraphCanvasFX graph, BreakdownCommand cmd) {
        super(graph, cmd);
        this.commands = new LinkedList<>();
    }

    @Override
    public void undo() {
        ListIterator<UICommand> it = this.commands
                .listIterator(this.commands.size());
        while (it.hasPrevious()) {
            UICommand c = it.previous();
            c.undo();
        }
    }

    @Override
    public void redo() {
        for (UICommand c : this.commands) {
            c.redo();
        }
    }

    public List<UICommand> getCommands() {
        return Collections.unmodifiableList(this.commands);
    }

}
