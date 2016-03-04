package controller;

import javafx.geometry.Point2D;
import model.AutomatonState;
import model.AutomatonTransition;
import model.RemoveStateCommand;
import view.GraphCanvasFX;
import view.GraphNode;

/**
 * Command to remove a state and its outgoing transitions from an automaton
 *
 * @author sp611
 */
public class RemoveStateUICommand extends UICommand {

    private final RemoveStateCommand ccmd;

    public RemoveStateUICommand(GraphCanvasFX graph, RemoveStateCommand cmd) {
        super(graph, cmd);
        this.ccmd = cmd;
    }

    @Override
    public void redo() {
        AutomatonState state = this.ccmd.getState();
        // Update coordinates so we restore the node to the position it was
        // before in undo()
        GraphNode node = this.graph.lookupNode(state.getId());
        this.location = new Point2D(node.getX(), node.getY());
        this.graph.removeNode(state.getId());
        this.cmd.redo();
    }

    @Override
    public void undo() {
        GraphNode nodeFrom = this.graph.addNode(this.ccmd.getState().getId(),
                this.location.getX(), this.location.getY());
        if (this.ccmd.getState().isFinal()) {
            // Make sure we set this node to use the "final style" if this is
            // a final state
            this.graph.setNodeUseFinalStyle(nodeFrom, true);
        }

        for (AutomatonTransition t : this.ccmd.getTransitions()) {
            GraphNode nodeTo = this.graph.lookupNode(t.getTo().getId());
            this.graph.addEdge(t.getId(), nodeFrom, nodeTo,
                    t.getData().toString());
        }
        this.cmd.undo();
    }

    @Override
    public String getDescription() {
        return "Removed state " + this.ccmd.getState().toString();
    }

}
