package controller;

import java.util.List;

import model.AutomatonTransition;
import model.ConjoinParallelTransitionsCommand;
import view.GraphCanvasFX;

public class ConjoinParallelTransitionsUICommand extends CompositeUICommand {

    private final ConjoinParallelTransitionsCommand ccmd;

    public ConjoinParallelTransitionsUICommand(GraphCanvasFX graph,
            ConjoinParallelTransitionsCommand cmd) {
        super(graph, cmd);
        this.ccmd = cmd;
    }

    @Override
    public String getDescription() {
        List<AutomatonTransition> oldTransitions = ccmd
                .getParallelTransitions();

        String fromStr = StringUtils.transitionListToEnglish(oldTransitions);

        String toStr = ccmd.getNewTransition().getData().toString();
        return String
                .format("Conjoined transitions %s into %s", fromStr, toStr);
    }
}
