package controller;

import model.AddStateCommand;
import model.BreakdownIterationCommand;
import model.Command;
import model.BreakdownIterationCommand.IsolationLevel;
import view.GraphCanvasFX;
import view.GraphNode;

/**
 * 
 * @author sp611
 *
 */
public class BreakdownIterationUICommand extends BreakdownUICommand {

    public BreakdownIterationUICommand(GraphCanvasFX graph,
            BreakdownIterationCommand cmd) {
        super(graph, cmd);

        final boolean oneNewState = (cmd
                .getIsolationLevel() == IsolationLevel.START_ISOLATE
                || cmd.getIsolationLevel() == IsolationLevel.END_ISOLATE);
        final boolean twoNewStates = (cmd
                .getIsolationLevel() == IsolationLevel.FULLY_ISOLATE);
        for (Command c : cmd.getCommands()) {
            if (oneNewState && c instanceof AddStateCommand) {
                GraphNode from = graph.lookupNode(
                        cmd.getOriginalTransition().getFrom().getId());
                GraphNode to = graph.lookupNode(
                        cmd.getOriginalTransition().getTo().getId());

                double dx = from.getX() - to.getX();
                double dy = from.getY() - to.getY();
                double newX = from.getX() + (dx / 2);
                double newY = from.getY() - (dy / 2);

                this.commands.add(new AddStateUICommand(graph,
                        (AddStateCommand) c, newX, newY));
            } else {
                this.commands.add(UICommand.fromCommand(graph, c));
            }
        }
    }

}
