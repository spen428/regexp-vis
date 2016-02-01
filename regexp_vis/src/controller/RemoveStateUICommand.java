package controller;

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
    private double x;
    private double y;

    public RemoveStateUICommand(GraphCanvasFX graph, RemoveStateCommand cmd)
    {
        super(graph, cmd);
        this.ccmd = cmd;
    }

    @Override
    public void redo() {
        AutomatonState state = ccmd.getState();
        // Update coordinates so we restore the node to the position it was 
        // before in undo()
        GraphNode node = graph.lookupNode(state.getId());
        x = node.getX();
        y = node.getY();
        graph.removeNode(state.getId());
        cmd.redo();
    }

    @Override
    public void undo() {
        GraphNode nodeFrom = graph.addNode(ccmd.getState().getId(), x, y);

        for (AutomatonTransition t : ccmd.getTransitions()) {
            GraphNode nodeTo = graph.lookupNode(t.getTo().getId());
            graph.addEdge(t.getId(), nodeFrom, nodeTo, t.getData().toString());
        }
        cmd.undo();
    }

}
