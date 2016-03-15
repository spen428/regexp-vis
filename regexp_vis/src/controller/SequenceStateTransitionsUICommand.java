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
        List<AutomatonTransition> sequencedTrans = ccmd.getSequencedTransitions();
        String sequencedStr = StringUtils.transitionListToEnglish(sequencedTrans);
        String stateStr = ccmd.getState().toString();

        return String.format(
                "Sequenced transitions for state %s into transitions %s",
                stateStr, sequencedStr);
    }
}
