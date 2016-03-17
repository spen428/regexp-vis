package controller;

import model.RemoveStateCleanlyCommand;
import view.GraphCanvasFX;

/**
 * Corresponding UICommand for RemoveStateCleanlyCommand, used when there is no
 * other "meaning" to the command (unlike RemoveUnreachableStateUICommand)
 */
public class RemoveStateCleanlyUICommand extends CompositeUICommand {
    private final RemoveStateCleanlyCommand ccmd;

    public RemoveStateCleanlyUICommand(GraphCanvasFX graph,
            RemoveStateCleanlyCommand cmd) {
        super(graph, cmd);
        this.ccmd = cmd;
    }

    @Override
    public String getDescription()
    {
        return "Removed state " + this.ccmd.getState().toString();
    }
}
