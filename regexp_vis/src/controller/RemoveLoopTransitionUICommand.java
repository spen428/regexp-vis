package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import model.AutomatonTransition;
import model.Command;
import model.RemoveLoopTransitionCommand;
import view.GraphCanvasFX;

public class RemoveLoopTransitionUICommand extends UICommand {
    private ArrayList<UICommand> commands;
    private final RemoveLoopTransitionCommand ccmd;

    public RemoveLoopTransitionUICommand(GraphCanvasFX graph,
            RemoveLoopTransitionCommand cmd) {
        super(graph, cmd);
        this.ccmd = cmd;
        this.commands = new ArrayList<>();
        for (Command c : cmd.getCommands()) {
            this.commands.add(UICommand.fromCommand(graph, c));
        }
    }

    // FIXME: duplicate code (e.g. BreakdownUICommand,
    // RemoveEpsilonTransitionsUICommand, RemoveEquivalentStatesUICommand,
    // RemoveNonDeterminismCommand, RemoveUnreachableStatesUICommand), maybe a
    // new base class CompositeUICommand? CompositeUICommand as a field? Or a
    // static utility method in UICommand?
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

    @Override
    public String getDescription() {
        AutomatonTransition t = ccmd.getLoopTransition();
        String transStr = t.getData().toString();
        String stateStr = t.getFrom().toString();

        return String.format("Removed loop transition %s from state %s",
                transStr, stateStr);
    }
}
