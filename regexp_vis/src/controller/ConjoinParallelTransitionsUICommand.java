package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import model.AutomatonTransition;
import model.Command;
import model.ConjoinParallelTransitionsCommand;
import view.GraphCanvasFX;

public class ConjoinParallelTransitionsUICommand extends UICommand {
    private ArrayList<UICommand> commands;
    private final ConjoinParallelTransitionsCommand ccmd;

    public ConjoinParallelTransitionsUICommand(GraphCanvasFX graph,
            ConjoinParallelTransitionsCommand cmd) {
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
        List<AutomatonTransition> oldTransitions = ccmd
                .getParallelTransitions();

        String fromStr = StringUtils.transitionListToEnglish(oldTransitions);

        String toStr = ccmd.getNewTransition().getData().toString();
        return String
                .format("Conjoined transitions %s into %s", fromStr, toStr);
    }
}
