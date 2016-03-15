package controller;

import model.RemoveEpsilonTransitionsCommand;
import view.GraphCanvasFX;

public class RemoveEpsilonTransitionsUICommand extends CompositeUICommand {

    private final RemoveEpsilonTransitionsCommand ccmd;

    public RemoveEpsilonTransitionsUICommand(GraphCanvasFX graph,
            RemoveEpsilonTransitionsCommand cmd) {
        super(graph, cmd);
        this.ccmd = cmd;
    }

    @Override
    public String getDescription() {
        return "Removed outgoing epsilon transitions from state "
                + this.ccmd.getTargetState().toString();
    }

}
