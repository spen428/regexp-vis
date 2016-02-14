package controller;

import javafx.geometry.Point2D;
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
        final GraphNode fromNode = graph.lookupNode(fromState.getId());
        final GraphNode toNode = graph.lookupNode(toState.getId());

        Point2D[] rekt = null;
        if (numAddedStates == 2) {
            rekt = BreakdownUITools.computeRectanglePoints(fromNode.getX(),
                    fromNode.getY(), toNode.getX(), toNode.getY());
        }

        boolean positionedFirst = false;
        for (Command c : cmd.getCommands()) {
            if (c instanceof AddStateCommand) {
                /* new state should be intelligently positioned */
                if (fromChoice) {
                    // TODO
                    double newX = Math.random() * 800;
                    double newY = Math.random() * 600;
                    this.commands.add(new AddStateUICommand(graph,
                            (AddStateCommand) c, newX, newY));
                } else {
                    if (numAddedStates == 1) {
                        /* Form a triangle */
                        Point2D newPos = BreakdownUITools.computeThirdPoint(
                                fromNode.getX(), fromNode.getY(), toNode.getX(),
                                toNode.getY());
                        this.commands.add(new AddStateUICommand(graph,
                                (AddStateCommand) c, newPos.getX(),
                                newPos.getY()));
                    } else if (numAddedStates == 2) {
                        /* Form a rectangle */
                        if (!positionedFirst) {
                            this.commands.add(new AddStateUICommand(graph,
                                    (AddStateCommand) c, rekt[0].getX(),
                                    rekt[0].getY()));
                            positionedFirst = true;
                        } else {
                            this.commands.add(new AddStateUICommand(graph,
                                    (AddStateCommand) c, rekt[1].getX(),
                                    rekt[1].getY()));
                        }
                    }
                }
            } else {
                this.commands.add(UICommand.fromCommand(graph, c));
            }
        }
    }
}
