package controller;

import model.RemoveEquivalentStatesCommand;
import view.GraphCanvasFX;

public class RemoveEquivalentStatesUICommand extends CompositeUICommand {

    private final RemoveEquivalentStatesCommand ccmd;

    public RemoveEquivalentStatesUICommand(GraphCanvasFX graph,
            RemoveEquivalentStatesCommand cmd) {
        super(graph, cmd);
        this.ccmd = cmd;
    }

    @Override
    public String getDescription() {
        return "Removed equivalent states for state "
                + this.ccmd.getTargetState().toString();
    }

}
