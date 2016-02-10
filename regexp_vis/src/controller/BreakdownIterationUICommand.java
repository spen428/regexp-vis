package controller;

import model.AddStateCommand;
import model.AutomatonState;
import model.AutomatonTransition;
import model.BreakdownIterationCommand;
import model.Command;
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

        final AutomatonTransition transition = cmd.getOriginalTransition();
        final AutomatonState fromState = transition.getFrom();
        final AutomatonState toState = transition.getTo();
        final int numAddedStates = BreakdownUITools.getNumAddedStates(cmd);
        final boolean fromChoice = BreakdownUITools.wasChoiceTransition(cmd);

        boolean positionedFirst = false;
        for (Command c : cmd.getCommands()) {
            if (c instanceof AddStateCommand) {
                /* new state should be intelligently positioned */
                GraphNode fromNode = graph.lookupNode(fromState.getId());
                GraphNode toNode = graph.lookupNode(toState.getId());
                double dx = fromNode.getX() - toNode.getX();
                double dy = fromNode.getY() - toNode.getY();
                double hyp = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));

                if (fromChoice) {
                    // TODO
                    double newX = Math.random() * 800;
                    double newY = Math.random() * 600;
                    this.commands.add(new AddStateUICommand(graph,
                            (AddStateCommand) c, newX, newY));
                } else {
                    if (numAddedStates == 1) {
                        /*
                         * Breaking down this iteration creates one new node,
                         * position to form an equilateral triangle
                         */
                        double newX = fromNode.getX() + (hyp / 2);
                        double newY = fromNode.getY() - (hyp / 2)
                                * Math.sqrt(3);

                        this.commands.add(new AddStateUICommand(graph,
                                (AddStateCommand) c, newX, newY));
                    } else if (numAddedStates == 2) {
                        /*
                         * Breaking down this iteration creates two new nodes,
                         * position to form a square
                         */
                        if (!positionedFirst) {
                            /* Place first new node on same x-coord as `to` */
                            double newX = toNode.getX();
                            double newY = toNode.getY() + (2 * dy);
                            this.commands.add(new AddStateUICommand(graph,
                                    (AddStateCommand) c, newX, newY));
                            positionedFirst = true;
                        } else {
                            /* Place second new node on same y-coord as `from` */
                            double newX = fromNode.getX() - (2 * dx);
                            double newY = fromNode.getY();
                            this.commands.add(new AddStateUICommand(graph,
                                    (AddStateCommand) c, newX, newY));
                        }
                    }
                }
            } else {
                this.commands.add(UICommand.fromCommand(graph, c));
            }
        }
    }
}
