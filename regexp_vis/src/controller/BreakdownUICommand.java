package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import model.AddTransitionCommand;
import model.AutomatonTransition;
import model.BreakdownChoiceCommand;
import model.BreakdownCommand;
import model.Command;
import view.GraphCanvasFX;

/**
 * The equivalent of {@link BreakdownCommand} for the UI side of things.
 *
 * @author sp611
 *
 */
public abstract class BreakdownUICommand extends UICommand {

    protected final LinkedList<UICommand> commands;
    protected final AutomatonTransition originalTransition;

    public BreakdownUICommand(GraphCanvasFX graph, BreakdownCommand cmd) {
        super(graph, cmd);
        this.commands = new LinkedList<>();
        this.originalTransition = cmd.getOriginalTransition();
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
        if (this instanceof BreakdownChoiceUICommand) {
            return String.format("Broke down choice %s into transitions %s",
                    origStr, tranStr.toString());
        } else if (this instanceof BreakdownSequenceUICommand) {
            return String.format("Broke down sequence %s into transitions %s",
                    origStr, tranStr.toString());
        } else if (this instanceof BreakdownIterationUICommand) {
            return String.format("Broke down iteration %s", origStr);
        } else if (this instanceof BreakdownOptionUICommand) {
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
