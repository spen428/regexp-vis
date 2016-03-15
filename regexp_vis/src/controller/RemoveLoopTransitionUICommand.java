package controller;

import model.AutomatonTransition;
import model.RemoveLoopTransitionCommand;
import view.GraphCanvasFX;

public class RemoveLoopTransitionUICommand extends CompositeUICommand {

    private final RemoveLoopTransitionCommand ccmd;

    public RemoveLoopTransitionUICommand(GraphCanvasFX graph,
            RemoveLoopTransitionCommand cmd) {
        super(graph, cmd);
        this.ccmd = cmd;
    }

    @Override
    public String getDescription() {
        AutomatonTransition t = ccmd.getLoopTransition();
        String transStr = t.getData().toString();
        String stateStr = t.getFrom().toString();

        return String.format("Removed loop transition %s from state %s",
                transStr, stateStr);
    }

}
