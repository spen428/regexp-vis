package controller;

import java.util.List;

import javafx.geometry.Point2D;
import model.AddStateCommand;
import model.AutomatonState;
import model.Command;
import model.IsolateFinalStateCommand;
import view.GraphCanvasFX;
import view.GraphNode;

public class IsolateFinalStateUICommand extends CompositeUICommand {

    private final IsolateFinalStateCommand ccmd;

    private static final double PLACEMENT_RADIUS_MIN = 50;
    private static final double PLACEMENT_RADIUS_MAX = 80;

    public IsolateFinalStateUICommand(GraphCanvasFX graph,
            IsolateFinalStateCommand cmd)
    {
        super(graph, cmd);
        this.ccmd = cmd;

        super.commands.clear();
        AddStateCommand newStateCmd = cmd.getNewStateCommand();
        for (Command c : cmd.getCommands()) {
            // Place position of a new state a bit more intelligently
            if (c == newStateCmd) {
                List<AutomatonState> oldStates = cmd.getOldFinalStates();

                // Calculate average position of the reachable states
                Point2D location = Point2D.ZERO;
                for (AutomatonState s : oldStates) {
                    GraphNode n = graph.lookupNode(s.getId());
                    location = location.add(n.getX(), n.getY());
                }
                if (oldStates.size() > 1) {
                    // Use the average position of the original final states
                    location = location.multiply(1.0 / oldStates.size());
                } else {
                    // Choose a random position around the current final state
                    double r = PLACEMENT_RADIUS_MIN + Math.random()
                            * (PLACEMENT_RADIUS_MAX - PLACEMENT_RADIUS_MIN);
                    double theta = Math.random() * 2 * Math.PI;
                    double x = r * Math.cos(theta);
                    double y = r * Math.sin(theta);
                    location = location.add(x, y);
                }

                super.commands.add(
                        new AddStateUICommand(graph, newStateCmd, location));
            } else {
                super.commands.add(UICommand.fromCommand(graph, c));
            }
        }
    }

    @Override
    public String getDescription() {
        return "Created new isolated final state "
                + this.ccmd.getNewFinalState().toString();
    }
}
