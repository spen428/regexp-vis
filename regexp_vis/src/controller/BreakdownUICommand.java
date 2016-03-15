package controller;

import java.util.List;

import javafx.geometry.Point2D;
import model.AddStateCommand;
import model.AutomatonTransition;
import model.BreakdownChoiceCommand;
import model.BreakdownCommand;
import model.BreakdownIterationCommand;
import model.BreakdownOptionCommand;
import model.BreakdownSequenceCommand;
import model.Command;
import view.GraphCanvasFX;

/**
 * The equivalent of {@link BreakdownCommand} for the UI side of things.
 *
 * @author sp611
 *
 */
public class BreakdownUICommand extends CompositeUICommand {

    protected final AutomatonTransition originalTransition;

    public BreakdownUICommand(GraphCanvasFX graph, BreakdownCommand cmd) {
        super(graph, cmd);
        this.originalTransition = cmd.getOriginalTransition();

        super.commands.clear();
        Point2D[] points = BreakdownUITools.placeNodes(graph, cmd);
        int added = 0;
        for (Command c : cmd.getCommands()) {
            if (c instanceof AddStateCommand) {
                Point2D loc = points[added++];
                AddStateUICommand newCommand = new AddStateUICommand(graph,
                        (AddStateCommand) c, loc.getX(), loc.getY());
                super.commands.add(newCommand);
            } else {
                super.commands.add(UICommand.fromCommand(graph, c));
            }
        }
    }

    public AutomatonTransition getOriginalTransition() {
        return this.originalTransition;
    }

    @Override
    public String getDescription() {
        /* Construct a string listing the new transitions */
        List<AutomatonTransition> newTransitions = BreakdownUITools
                .getNewTransitions(this);
        String tranStr = StringUtils.transitionListToEnglish(newTransitions);
        String origStr = this.getOriginalTransition().getData().toString();

        if (this.cmd instanceof BreakdownChoiceCommand) {
            return String.format("Broke down choice %s into transitions %s",
                    origStr, tranStr);
        } else if (this.cmd instanceof BreakdownSequenceCommand) {
            return String.format("Broke down sequence %s into transitions %s",
                    origStr, tranStr);
        } else if (this.cmd instanceof BreakdownIterationCommand) {
            return String.format("Broke down iteration %s", origStr);
        } else if (this.cmd instanceof BreakdownOptionCommand) {
            return String.format("Broke down option %s", origStr);
        }

        return "null";
    }

}
