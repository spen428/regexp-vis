package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import javafx.geometry.Point2D;
import model.AddStateCommand;
import model.AutomatonState;
import model.Command;
import model.RemoveNonDeterminismCommand;
import view.GraphCanvasFX;
import view.GraphNode;

public class RemoveNonDeterminismUICommand extends UICommand {
    private ArrayList<UICommand> commands;
    private final RemoveNonDeterminismCommand ccmd;

    public RemoveNonDeterminismUICommand(GraphCanvasFX graph,
            RemoveNonDeterminismCommand cmd) {
        super(graph, cmd);
        this.ccmd = cmd;
        this.commands = new ArrayList<>();

        AddStateCommand newStateCmd = cmd.getNewStateCommand();
        for (Command c : cmd.getCommands()) {
            // Place position of a new state a bit more intelligently
            if (c == newStateCmd) {
                Set<AutomatonState> reachableSet = cmd.getReachableSet();

                // Calculate average position of the reachable states
                Point2D location = Point2D.ZERO;
                // reachableSet.size() should always be > 1
                int adjustedSize = reachableSet.size();
                for (AutomatonState s : reachableSet) {
                    if (s == cmd.getState()) {
                        // Don't average the over the target state if for
                        // example a loop is part of the non-determinism
                        adjustedSize--;
                        continue;
                    }
                    GraphNode n = graph.lookupNode(s.getId());
                    location = location.add(n.getX(), n.getY());
                }
                location = location.multiply(1.0 / adjustedSize);

                // Average that average with the location of the target state
                GraphNode targetNode = graph.lookupNode(cmd.getState().getId());
                location = location.add(targetNode.getX(), targetNode.getY());
                location = location.multiply(0.5);

                this.commands.add(
                        new AddStateUICommand(graph, newStateCmd, location));
            } else {
                this.commands.add(UICommand.fromCommand(graph, c));
            }
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
        return "Removed non-determinism from state "
                + this.ccmd.getState().toString();
    }
}
