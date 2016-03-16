package controller;

import javafx.geometry.Point2D;
import model.AddStateCommand;
import model.Command;
import model.IsolateInitialStateCommand;
import view.GraphCanvasFX;
import view.GraphNode;

public class IsolateInitialStateUICommand extends CompositeUICommand {

    private final IsolateInitialStateCommand ccmd;

    private static final double PLACEMENT_RADIUS_MIN = 80;
    private static final double PLACEMENT_RADIUS_MAX = 120;

    public IsolateInitialStateUICommand(GraphCanvasFX graph,
            IsolateInitialStateCommand cmd) {
        super(graph, cmd);
        this.ccmd = cmd;

        GraphNode startStateNode = graph.lookupNode(cmd.getAutomaton()
                .getStartState().getId());
        Point2D startStatePos = new Point2D(startStateNode.getX(),
                startStateNode.getY());

        super.commands.clear();
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

                super.commands.add(new AddStateUICommand(graph, newStateCmd,
                        location));
            } else {
                super.commands.add(UICommand.fromCommand(graph, c));
            }
        }
    }

    @Override
    public String getDescription() {
        return "Isolated initial state";
    }
}
