package ui;

import model.AutomatonState;
import model.SetIsFinalCommand;

/**
 * Command to change the finality of a state
 * 
 * @author sp611
 */
public class SetIsFinalUICommand extends UICommand {

    private final SetIsFinalCommand ccmd;

    public SetIsFinalUICommand(Graph graph, SetIsFinalCommand cmd) {
        super(graph, cmd);
        this.ccmd = cmd;
    }

    public void redo() {
        if (ccmd.isDiffers()) {
            AutomatonState state = ccmd.getState();
            graph.setFinal(state, !state.isFinal());
            ccmd.redo();
        }
    }

    public void undo() {
        redo();
    }

}
