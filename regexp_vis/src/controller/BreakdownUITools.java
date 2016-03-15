package controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.geometry.Point2D;
import model.AddStateCommand;
import model.Automaton;
import model.AutomatonState;
import model.AutomatonTransition;
import model.BreakdownCommand;
import model.Command;
import model.RemoveStateCommand;
import view.GraphCanvasFX;

/**
 *
 * @author sp611
 *
 */
public class BreakdownUITools {

    private static final Logger LOGGER = Logger.getLogger("controller");

    public static final double MIN_BREAKDOWN_HEIGHT = 4 * GraphCanvasFX.DEFAULT_NODE_RADIUS;

    private static final double VARIATION = 10;

    /**
     * @return The number of occurrences of {@link AddStateCommand} in the given
     *         {@link Command}.
     */
    public static int getNumAddedStates(BreakdownCommand cmd) {
        int count = 0;
        for (Command c : cmd.getCommands()) {
            if (c instanceof AddStateCommand) {
                count++;
            }
        }
        return count;
    }

    /**
     * @return The number of occurrences of {@link RemoveStateCommand} in the
     *         given {@link Command}.
     */
    public static int getNumRemovedStates(BreakdownCommand cmd) {
        int count = 0;
        for (Command c : cmd.getCommands()) {
            if (c instanceof RemoveStateCommand) {
                count++;
            }
        }
        return count;
    }

    /**
     * Checks whether the original transition of the given
     * {@link BreakdownCommand} was one of a number of choices that go from
     * transition.from to transition.to.
     *
     * @param cmd
     *            the command
     * @return true if the transition was part of a choice
     */
    public static boolean wasChoiceTransition(BreakdownCommand cmd) {
        final AutomatonTransition transition = cmd.getOriginalTransition();
        final List<AutomatonTransition> transitions = getTransitionsBetween(
                cmd.getAutomaton(), transition.getFrom(), transition.getTo());
        return (transitions.size() > 1 && transitions.contains(transition));
    }

    /**
     * Returns a list of all {@link AutomatonTransition} that go directly
     * between two given {@link AutomatonState}.
     *
     * @param automaton
     *            the {@link Automaton} that the states and transitions belong
     *            to
     * @param from
     *            the 'from' state
     * @param to
     *            the 'to' state
     * @return the list
     */
    public static List<AutomatonTransition> getTransitionsBetween(
            Automaton automaton, AutomatonState from, AutomatonState to) {
        final List<AutomatonTransition> fromTrs = automaton
                .getStateTransitions(from);
        final List<AutomatonTransition> toTrs = automaton
                .getIngoingTransition(to);
        final List<AutomatonTransition> between = new LinkedList<>();
        for (AutomatonTransition f : fromTrs) {
            if (toTrs.contains(f)) {
                between.add(f);
            }
        }
        return between;
    }

    /**
     * Returns a list of the new {@link AutomatonTransition}s added by this
     * {@link BreakdownUICommand}
     *
     * @param cmd
     *            the {@link BreakdownUICommand}
     * @return a list of {@link AutomatonTransition}
     */
    public static List<AutomatonTransition> getNewTransitions(
            BreakdownUICommand cmd) {
        ArrayList<AutomatonTransition> transitions = new ArrayList<>();
        for (UICommand c : cmd.getCommands()) {
            if (c instanceof AddTransitionUICommand) {
                transitions.add(((AddTransitionUICommand) c).getTransition());
            }
        }
        return transitions;
    }

    /**
     * @return A random {@link Point2D} located between (0,0) and (800,600)
     */
    public static Point2D randomPoint() {
        return new Point2D(Math.random() * 800, Math.random() * 600);
    }

    /**
     * @return A new {@link Point2D} located at (x,y) plus-or-minus
     *         {@value #VARIATION}
     */
    private static Point2D variedPoint(double x, double y) {
        double dx = (Math.random() * 2 * VARIATION) - VARIATION;
        double dy = (Math.random() * 2 * VARIATION) - VARIATION;
        return new Point2D(x + dx, y + dy);
    }

    /**
     * Intelligently place new {@link AutomatonState}s that are created by this
     * {@link BreakdownCommand}, so that the layout requires minimal alteration
     * to be readable
     *
     * @param graph
     *            the {@link GraphCanvasFX} onto which the
     *            {@link AutomatonState}s will be rendered
     * @param cmd
     *            the {@link BreakdownCommand}
     * @return an array of {@link Point2D} containing the intended location for
     *         each of the new {@link AutomatonState}, in the order that their
     *         {@link AddStateCommand}s appear when iterating over the
     *         {@link BreakdownCommand}
     */
    public static Point2D[] placeNodes(GraphCanvasFX graph, BreakdownCommand cmd) {
        final AutomatonTransition transition = cmd.getOriginalTransition();
        final AutomatonState fromState = transition.getFrom();
        final AutomatonState toState = transition.getTo();
        final int numAddedStates = BreakdownUITools.getNumAddedStates(cmd);
        final int transCount = numAddedStates + 1;
        final double fromX = graph.lookupNode(fromState.getId()).getX();
        final double fromY = graph.lookupNode(fromState.getId()).getY();
        final double toX = graph.lookupNode(toState.getId()).getX();
        final double toY = graph.lookupNode(toState.getId()).getY();

        final double midPointX = 0.5 * (fromX + toX);
        final double midPointY = 0.5 * (fromY + toY);

        double dxPerNode = (toX - fromX) / transCount;
        double dyPerNode = (toY - fromY) / transCount;

        Point2D middlePoint = graph.lookupEdge(
                cmd.getOriginalTransition().getId()).getEdgeMiddlePoint();
        Point2D offsetVec = middlePoint.subtract(midPointX, midPointY);
        double mag = offsetVec.magnitude();
        LOGGER.log(Level.FINER, "oldmag = " + mag);
        if (mag != 0 && mag < MIN_BREAKDOWN_HEIGHT) {
            offsetVec = offsetVec.multiply(MIN_BREAKDOWN_HEIGHT / mag);
            LOGGER.log(Level.FINER, "newmag = " + offsetVec.magnitude());
        }

        Point2D[] points = new Point2D[numAddedStates];

        double curX = offsetVec.getX() + fromX;
        double curY = offsetVec.getY() + fromY;

        int i = 0;
        for (Command tmpCmd : cmd.getCommands()) {
            if (tmpCmd instanceof AddStateCommand) {
                curX += dxPerNode;
                curY += dyPerNode;
                points[i] = variedPoint(curX, curY);
                i++;
            }
        }

        return points;
    }

}
