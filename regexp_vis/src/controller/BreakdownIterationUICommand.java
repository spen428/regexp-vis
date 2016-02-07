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
        boolean positionedFirst = false;
        for (Command c : cmd.getCommands()) {
            if (c instanceof AddStateCommand) {
                /* new state should be intelligently positioned */
                GraphNode from = graph.lookupNode(
                        cmd.getOriginalTransition().getFrom().getId());
                GraphNode to = graph.lookupNode(
                        cmd.getOriginalTransition().getTo().getId());

                double dx = from.getX() - to.getX();
                double dy = from.getY() - to.getY();

                if (oneNewState) {
                    /*
                     * Breaking down this iteration creates one new node,
                     * position to form an equilateral triangle
                     */
                    double newX = from.getX() + (dx / 2);
                    double newY = from.getY() - (dy / 2);

                    this.commands.add(new AddStateUICommand(graph,
                            (AddStateCommand) c, newX, newY));
                } else if (twoNewStates) {
                    /*
                     * Breaking down this iteration creates two new nodes,
                     * position to form a square
                     */
                    if (!positionedFirst) {
                        /* Place first new node on same x-coord as `to` */
                        double newX = to.getX();
                        double newY = to.getY() + (2 * dy);
                        this.commands.add(new AddStateUICommand(graph,
                                (AddStateCommand) c, newX, newY));
                        positionedFirst = true;
                    } else {
                        /* Place second new node on same y-coord as `from` */
                        double newX = from.getX() - (2 * dx);
                        double newY = from.getY();
                        this.commands.add(new AddStateUICommand(graph,
                                (AddStateCommand) c, newX, newY));
                    }
                }
            } else {
                this.commands.add(UICommand.fromCommand(graph, c));
            }
        }
    }

}
