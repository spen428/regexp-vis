package controller;

import javafx.geometry.Point2D;
import model.AddStateCommand;
import model.AutomatonState;
import view.GraphCanvasFX;
import view.GraphNode;

/**
 * Command to add a state to an automaton
 *
 * @author sp611
 */
public class AddStateUICommand extends UICommand {
    private final AddStateCommand ccmd;
    private Point2D location;

    public AddStateUICommand(GraphCanvasFX graph, AddStateCommand cmd, double x,
            double y) {
        super(graph, cmd);
        this.ccmd = cmd;
        this.location = new Point2D(x, y);
    }

    public AddStateUICommand(GraphCanvasFX graph, AddStateCommand cmd,
            Point2D location) {
        super(graph, cmd);
        this.ccmd = cmd;
        this.location = location;
    }

    @Override
    public void redo() {
        graph.addNode(ccmd.getState().getId(), location.getX(),
                location.getY());
        ccmd.redo();
    }

    @Override
    public void undo() {
        AutomatonState state = ccmd.getState();
        GraphNode node = graph.lookupNode(state.getId());
        // Update coordinates so we restore the node to the position it was
        // before in redo()
        this.location = new Point2D(node.getX(), node.getY());
        graph.removeNode(state.getId());
        ccmd.undo();
    }

}
