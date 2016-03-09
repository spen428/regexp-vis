package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import model.Command;
import model.CompositeCommand;
import view.GraphCanvasFX;

public abstract class CompositeUICommand extends UICommand {

    protected final ArrayList<UICommand> commands;

    public CompositeUICommand(GraphCanvasFX graph, CompositeCommand cmd) {
        super(graph, cmd);
        this.commands = new ArrayList<>();
        for (Command c : cmd.getCommands()) {
            this.commands.add(UICommand.fromCommand(graph, c));
        }
    }

    @Override
    public void undo() {
        ListIterator<UICommand> it = this.commands.listIterator(this.commands
                .size());
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
