package controller;

import java.util.List;

import model.AutomatonTransition;
import model.SequenceStateTransitionsCommand;
import view.GraphCanvasFX;

public class SequenceStateTransitionsUICommand extends CompositeUICommand {

    private final SequenceStateTransitionsCommand ccmd;

    public SequenceStateTransitionsUICommand(GraphCanvasFX graph,
            SequenceStateTransitionsCommand cmd) {
        super(graph, cmd);
        this.ccmd = cmd;
    }

    @Override
    public String getDescription() {
        List<AutomatonTransition> sequencedTrans = ccmd
                .getSequencedTransitions();
        String sequencedStr = StringUtils
                .transitionListToEnglish(sequencedTrans);
        String stateStr = ccmd.getState().toString();

        return String.format("Concatenated transitions going through "
                + "state %s into transition%s %s", stateStr,
                sequencedTrans.size() != 1 ? "s" : "", sequencedStr);
    }
}
