package controller;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import model.AutomatonTransition;
import model.BreakdownCommand;
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
        ListIterator<UICommand> it = this.commands.listIterator(this.commands
                .size());
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
        List<AutomatonTransition> newTransitions = BreakdownUITools
                .getNewTransitions(this);
        String tranStr = StringUtils.transitionListToEnglish(newTransitions);

        /* Format description string */
        String origStr = this.getOriginalTransition().getData().toString();
        if (this instanceof BreakdownChoiceUICommand) {
            return String.format("Broke down choice %s into transitions %s",
                    origStr, tranStr);
        } else if (this instanceof BreakdownSequenceUICommand) {
            return String.format("Broke down sequence %s into transitions %s",
                    origStr, tranStr);
        } else if (this instanceof BreakdownIterationUICommand) {
            return String.format("Broke down iteration %s", origStr);
        } else if (this instanceof BreakdownOptionUICommand) {
            return String.format("Broke down option %s", origStr);
        }

        return "null";
    }

}
