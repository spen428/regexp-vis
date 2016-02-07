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
        if (this.ccmd.isDiffers()) {
            AutomatonState state = this.ccmd.getState();
            GraphNode node = this.graph.lookupNode(state.getId());
            this.graph.setNodeUseFinalStyle(node, !state.isFinal());
            this.ccmd.redo();
        }
    }

    @Override
    public void undo() {
        redo();
    }

}
