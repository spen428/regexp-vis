package controller;

import java.util.LinkedList;
import java.util.List;

import javafx.geometry.Point2D;
import model.AddStateCommand;
import model.Automaton;
import model.AutomatonState;
import model.AutomatonTransition;
import model.BreakdownCommand;
import model.BreakdownIterationCommand;
import model.BreakdownSequenceCommand;
import model.Command;
import model.RemoveStateCommand;
import view.GraphCanvasFX;
import view.GraphNode;

/**
 * 
 * @author sp611
 *
 */
public class BreakdownUITools {

    /**
     * The height of the triangles, rectangles, and curves on which new nodes
     * are placed when a breakdown creates new states.
     */
    public static final double BREAKDOWN_HEIGHT_PX = 4
            * GraphCanvasFX.DEFAULT_NODE_RADIUS;

    /**
     * Calculate the third point of triangle ABC, where points A and B lie on
     * the base of the triangle.
     * 
     * @param ax
     *            x coordinate of A
     * @param ay
     *            y coordinate of A
     * @param bx
     *            x coordinate of B
     * @param by
     *            y coordinate of B
     * @param height
     *            height of triangle ABC, may be negative
     * @return {@link Point2D} C
     */
    public static Point2D computeThirdPoint(double ax, double ay, double bx,
            double by) {
        final int sign = (ay < by) ? 1 : -1; // Dictates where to place point C
        final double dx = Math.abs(ax - bx);
        final double dy = Math.abs(ay - by) * sign;
        final double theta = Math.atan(dy / dx) * sign; // Slope of line AB
        final double mx = (ax + (dx / 2)), my = (ay + (dy / 2)); // Midpoint
        final double w = (BREAKDOWN_HEIGHT_PX * Math.sin(theta));
        final double n = sign * (Math
                .sqrt(Math.pow(BREAKDOWN_HEIGHT_PX, 2) - Math.pow(w, 2)));
        final double qx = (mx - w), qy = my; // Point which forms rat MQC.
        final double cx = qx, cy = (qy + n); // The desired point
        // System.out.printf("A = (%f,%f)%n", ax, ay);
        // System.out.printf("B = (%f,%f)%n", bx, by);
        // System.out.printf("C = (%f,%f)%n", cx, cy);
        // System.out.printf("M = (%f,%f)%n", mx, my);
        // System.out.printf("Q = (%f,%f)%n", qx, qy);
        // System.out.printf("w = %f, n = %f, theta = %f%n", w, n, theta);
        return new Point2D(cx, cy);
    }

    public static Point2D[] computeRectanglePoints(double ax, double ay,
            double bx, double by) {
        // Based on computeThirdPoint() above
        final int sign = (ay < by) ? 1 : -1;
        final double deltax = Math.abs(ax - bx);
        final double deltay = Math.abs(ay - by) * sign;
        final double theta = Math.atan(deltay / deltax) * sign;
        final double w = (BREAKDOWN_HEIGHT_PX * Math.sin(theta));
        final double n = sign * (Math
                .sqrt(Math.pow(BREAKDOWN_HEIGHT_PX, 2) - Math.pow(w, 2)));
        final double cx = (ax - w), cy = (ay + n);
        final double dx = (bx - w), dy = (by + n);
        System.out.printf("A = (%f,%f)%n", ax, ay);
        System.out.printf("B = (%f,%f)%n", bx, by);
        System.out.printf("C = (%f,%f)%n", cx, cy);
        System.out.printf("D = (%f,%f)%n", dx, dy);
        System.out.printf("w = %f, n = %f, theta = %f%n", w, n, theta);
        return new Point2D[] { new Point2D(cx, cy), new Point2D(dx, dy) };
    }

    public static Point2D[] computePoints(double ax, double ay, double bx,
            double by, int numPoints) {
        if (numPoints < 1) {
            throw new IllegalArgumentException();
        } else if (numPoints == 1) {
            return new Point2D[] { computeThirdPoint(ax, ay, bx, by) };
        }
        // TODO
        throw new UnsupportedOperationException();
    }

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

    public static Point2D[] placeNodes(GraphCanvasFX graph,
            BreakdownIterationCommand cmd) {
        final AutomatonTransition transition = cmd.getOriginalTransition();
        final AutomatonState fromState = transition.getFrom();
        final AutomatonState toState = transition.getTo();
        final int numAddedStates = BreakdownUITools.getNumAddedStates(cmd);
        final boolean fromChoice = BreakdownUITools.wasChoiceTransition(cmd);
        final GraphNode fromNode = graph.lookupNode(fromState.getId());
        final GraphNode toNode = graph.lookupNode(toState.getId());

        Point2D[] points = new Point2D[numAddedStates];

        if (fromChoice) {
            // TODO
            for (int i = 0; i < numAddedStates; i++) {
                points[i] = randomPoint();
            }
        } else {
            if (numAddedStates == 1) {
                /* Form a triangle */
                points[0] = computeThirdPoint(fromNode.getX(), fromNode.getY(),
                        toNode.getX(), toNode.getY());
            } else if (numAddedStates == 2) {
                /* Form a rectangle */
                points = computeRectanglePoints(fromNode.getX(),
                        fromNode.getY(), toNode.getX(), toNode.getY());
            } else {
                for (int i = 0; i < numAddedStates; i++) {
                    points[i] = randomPoint();
                }
            }
        }

        return points;
    }

    /**
     * Intelligently place the nodes created by the given
     * {@link BreakdownSequenceCommand} along a line or curve between the two
     * existing nodes from which this {@link BreakdownSequenceCommand} was
     * generated.
     * 
     * @param graph
     *            the {@link GraphCanvasFX}
     * @param cmd
     *            the {@link BreakdownCommand}
     * @return a {@link Point2D} array of the locations to place the new nodes,
     *         in order starting from the "from" end.
     */
    public static Point2D[] placeNodes(GraphCanvasFX graph,
            BreakdownSequenceCommand cmd) {
        /*
         * TODO: Case where transCount is too high, and states are too close
         * together for edges to be rendered must be handled differenty.
         */
        final AutomatonTransition transition = cmd.getOriginalTransition();
        final GraphNode fromNode = graph
                .lookupNode(transition.getFrom().getId());
        final GraphNode toNode = graph.lookupNode(transition.getTo().getId());
        final int transCount = cmd.getNewTransitionsCount();
        final boolean fromChoice = BreakdownUITools.wasChoiceTransition(cmd);

        Point2D[] points = new Point2D[transCount];

        if (fromChoice) {
            /*
             * The nodes should be placed roughly along the curve that
             * previously existed.
             */
            Point2D edgeMid = graph
                    .lookupEdge(cmd.getOriginalTransition().getId())
                    .getEdgeMiddlePoint();
            // TODO
            for (int i = 0; i < transCount; i++) {
                points[i] = randomPoint();
            }
        } else {
            /* Didn't breakdown from a choice, just place along a line */
            double dxPerNode = (toNode.getX() - fromNode.getX()) / transCount;
            double dyPerNode = (toNode.getY() - fromNode.getY()) / transCount;
            double curX = fromNode.getX() + dxPerNode;
            double curY = fromNode.getY() + dyPerNode;

            for (int i = 0; i < transCount; i++) {
                points[i] = new Point2D(curX, curY);
                curX += dxPerNode;
                curY += dyPerNode;
            }
        }
        return points;
    }

    private static Point2D randomPoint() {
        return new Point2D(Math.random() * 800, Math.random() * 600);
    }

}
