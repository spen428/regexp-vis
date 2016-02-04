package controller;

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
    private double x;
    private double y;

    public AddStateUICommand(GraphCanvasFX graph, AddStateCommand cmd,
            double x, double y)
    {
        super(graph, cmd);
        this.ccmd = cmd;
        this.x = x;
        this.y = y;
    }

    @Override
    public void redo() {
        graph.addNode(ccmd.getState().getId(), x, y);
        ccmd.redo();
    }

    @Override
    public void undo() {
        AutomatonState state = ccmd.getState();
        GraphNode node = graph.lookupNode(state.getId());
        // Update coordinates so we restore the node to the position it was 
        // before in redo()
        x = node.getX();
        y = node.getY();
        graph.removeNode(state.getId());
        ccmd.undo();
    }

}
