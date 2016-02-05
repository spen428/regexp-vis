package controller;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import model.BreakdownCommand;
import model.Command;
import view.GraphCanvasFX;

/**
 * The equivalent of {@link BreakdownCommand} for the UI side of things.
 *
 * @author sp611
 *
 */
public class BreakdownUICommand extends UICommand {

    protected final LinkedList<UICommand> commands;

    public BreakdownUICommand(GraphCanvasFX graph, BreakdownCommand cmd) {
        super(graph, cmd);
        this.commands = new LinkedList<UICommand>();

        /* Convert list of Command to UICommand */
        ListIterator<Command> it = cmd.getCommands().listIterator();
        while (it.hasNext()) {
            this.commands.add(UICommand.fromCommand(graph, it.next()));
        }
    }

    @Override
    public void undo() {
        ListIterator<UICommand> it = commands.listIterator(commands.size());
        while (it.hasPrevious()) {
            UICommand c = it.previous();
            c.undo();
        }
    }

    @Override
    public void redo() {
        for (UICommand c : commands) {
            c.redo();
        }
    }
    
    public List<UICommand> debugGetUICommands()
    {
        return Collections.unmodifiableList(commands);
    }

}
