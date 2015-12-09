package ui;

import java.util.LinkedList;
import java.util.ListIterator;

import model.BreakdownCommand;
import model.Command;

/**
 * The equivalent of {@link BreakdownCommand} for the UI side of things.
 * 
 * @author sp611
 * 
 */
public class BreakdownUICommand extends UICommand {

    private final LinkedList<UICommand> commands;

    public BreakdownUICommand(Graph graph, BreakdownCommand cmd) {
        super(graph, cmd);
        this.commands = new LinkedList<UICommand>();

        /* Convert list of Command to UICommand */
        ListIterator<Command> it = cmd.getCommands().listIterator();
        while (it.hasNext()) {
            this.commands.push(UICommand.fromCommand(graph, it.next()));
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

}
