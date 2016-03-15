package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import javafx.geometry.Point2D;
import model.AddStateCommand;
import model.Command;
import model.IsolateInitialStateCommand;
import view.GraphCanvasFX;
import view.GraphNode;

public class IsolateInitialStateUICommand extends UICommand {
    private ArrayList<UICommand> commands;
    private final IsolateInitialStateCommand ccmd;

    private static final double PLACEMENT_RADIUS_MIN = 50;
    private static final double PLACEMENT_RADIUS_MAX = 80;

    public IsolateInitialStateUICommand(GraphCanvasFX graph,
            IsolateInitialStateCommand cmd)
    {
        super(graph, cmd);
        this.ccmd = cmd;
        this.commands = new ArrayList<>();

        GraphNode startStateNode = graph.lookupNode(cmd.getAutomaton()
                .getStartState().getId());
        Point2D startStatePos = new Point2D(startStateNode.getX(),
                startStateNode.getY());

        AddStateCommand newStateCmd = cmd.getNewStateCommand();
        for (Command c : cmd.getCommands()) {
            // Place position of a new state a bit more intelligently
            if (c == newStateCmd) {
                // Choose a random position around the current initial state
                double r = PLACEMENT_RADIUS_MIN + Math.random()
                        * (PLACEMENT_RADIUS_MAX - PLACEMENT_RADIUS_MIN);
                double theta = Math.random() * 2 * Math.PI;
                double x = r * Math.cos(theta);
                double y = r * Math.sin(theta);
                Point2D location = startStatePos.add(x, y);

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
        return "Isolated initial state";
    }
}
