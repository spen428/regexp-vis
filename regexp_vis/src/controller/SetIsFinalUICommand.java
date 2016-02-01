package controller;

import model.AutomatonState;
import model.SetIsFinalCommand;
import view.GraphCanvasFX;
import view.GraphNode;

/**
 * Command to change the finality of a state
 *
 * @author sp611
 */
public class SetIsFinalUICommand extends UICommand {

    private final SetIsFinalCommand ccmd;

    public SetIsFinalUICommand(GraphCanvasFX graph, SetIsFinalCommand cmd) {
        super(graph, cmd);
        this.ccmd = cmd;
    }

    @Override
    public void redo() {
        if (ccmd.isDiffers()) {
            AutomatonState state = ccmd.getState();
            GraphNode node = graph.lookupNode(state.getId());
            graph.setNodeUseFinalStyle(node, !state.isFinal());
            ccmd.redo();
        }
    }

    @Override
    public void undo() {
        redo();
    }

}
