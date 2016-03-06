package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

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
public class BreakdownUICommand extends UICommand {

    protected final LinkedList<UICommand> commands;
    protected final AutomatonTransition originalTransition;

    public BreakdownUICommand(GraphCanvasFX graph, BreakdownCommand cmd) {
        super(graph, cmd);
        this.commands = new LinkedList<>();
        this.originalTransition = cmd.getOriginalTransition();

        Point2D[] points = BreakdownUITools.placeNodes(graph, cmd);
        int added = 0;

        for (Command c : cmd.getCommands()) {
            if (c instanceof AddStateCommand) {
                Point2D loc = points[added++];
                AddStateUICommand newCommand = new AddStateUICommand(graph,
                        (AddStateCommand) c, loc.getX(), loc.getY());
                this.commands.add(newCommand);
            } else {
                this.commands.add(UICommand.fromCommand(graph, c));
            }
        }
    }

    @Override
    public void undo() {
        ListIterator<UICommand> it = this.commands
                .listIterator(this.commands.size());
        while (it.hasPrevious()) {
            UICommand c = it.previous();
            c.undo();
        }
    }

    @Override
    public void redo() {
        for (UICommand c : this.commands) {
            c.redo();
        }
    }

    public List<UICommand> getCommands() {
        return Collections.unmodifiableList(this.commands);
    }

    public AutomatonTransition getOriginalTransition() {
        return this.originalTransition;
    }

    @Override
    public String getDescription() {
        /* Construct a string listing the new transitions */
        AutomatonTransition[] newTransitions = getNewTransitions(this);
        StringBuilder tranStr = new StringBuilder();
        for (int i = 0; i < newTransitions.length; i++) {
            tranStr.append(newTransitions[i].getData().toString());
            if (newTransitions.length != 2 && i < newTransitions.length - 1) {
                tranStr.append(",");
            }

            if (i == newTransitions.length - 2) {
                /* Penultimate */
                tranStr.append(" and ");
            } else {
                tranStr.append(" ");
            }
        }

        /* Format description string */
        String origStr = this.getOriginalTransition().getData().toString();
        if (this.cmd instanceof BreakdownChoiceCommand) {
            return String.format("Broke down choice %s into transitions %s",
                    origStr, tranStr.toString());
        } else if (this.cmd instanceof BreakdownSequenceCommand) {
            return String.format("Broke down sequence %s into transitions %s",
                    origStr, tranStr.toString());
        } else if (this.cmd instanceof BreakdownIterationCommand) {
            return String.format("Broke down iteration %s", origStr);
        } else if (this.cmd instanceof BreakdownOptionCommand) {
            return String.format("Broke down option %s", origStr);
        }

        return "null";
    }

    private static AutomatonTransition[] getNewTransitions(
            BreakdownUICommand cmd) {
        ArrayList<AutomatonTransition> transitions = new ArrayList<>();
        for (UICommand c : cmd.getCommands()) {
            if (c instanceof AddTransitionUICommand) {
                transitions.add(((AddTransitionUICommand) c).getTransition());
            }
        }
        return transitions.toArray(new AutomatonTransition[0]);
    }

}
