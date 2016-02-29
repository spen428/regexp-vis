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

    private static Point2D[] computeRectanglePoints(double ax, double ay,
            double bx, double by, double h, boolean above) {
        final int sign = above ? 1 : -1;
        final double deltaX = bx - ax;
        final double deltaY = by - ay;
        final double theta = Math.atan(deltaY / deltaX);
        final double w = h * Math.sin(theta) * sign;
        final double n = Math.sqrt(h * h - w * w) * sign;
        final double cx = (ax + w);
        final double cy = (ay - n);
        final double dx = (bx + w);
        final double dy = (by - n);
        return new Point2D[] { new Point2D(cx, cy), new Point2D(dx, dy) };
    }

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
     * @param h
     *            the height of the triangle
     * @param above
     *            whether to find the y > midY or the y < midY point
     * @return {@link Point2D} C
     */
    private static Point2D computeThirdPoint(double ax, double ay, double bx,
            double by, double h, boolean above) {
        final int sign = above ? 1 : -1;
        final double dx = bx - ax;
        final double dy = by - ay;
        final double theta = Math.atan(dy / dx); // Slope of line AB
        final double mx = ax + (dx / 2); // Midpoint
        final double my = ay + (dy / 2);
        final double w = h * Math.sin(theta) * sign;
        final double n = Math.sqrt(h * h - w * w) * sign;
        final double cx = (mx + w);
        final double cy = (my - n);
        return new Point2D(cx, cy);
    }

    /**
     * Iterate
     * {@link #computeThirdPoint(double, double, double, double, boolean)} over
     * a set of points.
     */
    private static Point2D[] computeTriangles(double ax, double ay, double bx,
            double by, int numPoints, double height, boolean above) {
        if (numPoints < 1) {
            throw new IllegalArgumentException();
        }

        final double deltaX = bx - ax;
        final double deltaY = by - ay;
        final double dxPerNode = deltaX / (numPoints + 1);
        final double dyPerNode = deltaY / (numPoints + 1);

        double prevX = ax;
        double prevY = ay;
        double curX = ax + 2 * dxPerNode;
        double curY = ay + 2 * dyPerNode;

        Point2D[] points = new Point2D[numPoints];

        for (int i = 0; i < numPoints; i++) {
            points[i] = computeThirdPoint(prevX, prevY, curX, curY, height,
                    above);
            prevX += dxPerNode;
            prevY += dyPerNode;
            curX += dxPerNode;
            curY += dyPerNode;
        }

        return points;
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

    /**
     * Intelligently place the nodes created by the given
     * {@link BreakdownCommand} along a line or curve between the two existing
     * nodes from which this {@link BreakdownCommand} was generated.
     *
     * @param graph
     *            the {@link GraphCanvasFX}
     * @param cmd
     *            the {@link BreakdownCommand}
     * @return a {@link Point2D} array of the locations to place the new nodes,
     *         in order starting from the "from" end.
     */
    public static Point2D[] placeNodesOld(GraphCanvasFX graph,
            BreakdownCommand cmd) {
        final AutomatonTransition transition = cmd.getOriginalTransition();
        final AutomatonState fromState = transition.getFrom();
        final AutomatonState toState = transition.getTo();
        final int numAddedStates = BreakdownUITools.getNumAddedStates(cmd);
        final boolean fromChoice = BreakdownUITools.wasChoiceTransition(cmd);
        final double fromX = graph.lookupNode(fromState.getId()).getX();
        final double fromY = graph.lookupNode(fromState.getId()).getY();
        final double toX = graph.lookupNode(toState.getId()).getX();
        final double toY = graph.lookupNode(toState.getId()).getY();
        final double dx = toX - fromX;
        final double dy = toY - fromY;
        final double len = Math.sqrt(dx * dx + dy * dy);

        Point2D[] points = new Point2D[numAddedStates];
        if (numAddedStates > 0) {
            boolean above = (numAddedStates == 1); // Arbitrary default value
            double height = BREAKDOWN_HEIGHT_PX;

            if (fromChoice) {
                Point2D edgeMid = graph
                        .lookupEdge(cmd.getOriginalTransition().getId())
                        .getEdgeMiddlePoint();

                above = (edgeMid.getY() > dy);
                double mx = fromX + dx / 2;
                double my = fromY + dy / 2;
                double ex = edgeMid.getX() - mx;
                double ey = edgeMid.getY() - my;
                height += Math.sqrt(ex * ex + ey * ey);
            } else if (cmd instanceof BreakdownSequenceCommand) {
                height = 0;
            }

            if (cmd instanceof BreakdownIterationCommand && numAddedStates == 2
                    && len <= BREAKDOWN_HEIGHT_PX * 2) {
                /* A bit too cramped, make a rectangle instead */
                points = computeRectanglePoints(fromX, fromY, toX, toY, height,
                        above);
            } else {
                points = computeTriangles(fromX, fromY, toX, toY,
                        numAddedStates, height, above);
            }
        }
        return points;
    }

    public static Point2D[] placeNodes(GraphCanvasFX canvas, BreakdownCommand cmd) {
        final AutomatonTransition transition = cmd.getOriginalTransition();
        final AutomatonState fromState = transition.getFrom();
        final AutomatonState toState = transition.getTo();
        final int numAddedStates = BreakdownUITools.getNumAddedStates(cmd);
        final int transCount = numAddedStates + 1;
        final double fromX = canvas.lookupNode(fromState.getId()).getX();
        final double fromY = canvas.lookupNode(fromState.getId()).getY();
        final double toX = canvas.lookupNode(toState.getId()).getX();
        final double toY = canvas.lookupNode(toState.getId()).getY();

        final double midPointX = 0.5 * (fromX + toX);
        final double midPointY = 0.5 * (fromY + toY);

        double dxPerNode = (toX - fromX) / transCount;
        double dyPerNode = (toY - fromY) / transCount;

        Point2D middlePoint = canvas
                .lookupEdge(cmd.getOriginalTransition().getId())
                .getEdgeMiddlePoint();
        Point2D offsetVec = middlePoint.subtract(midPointX, midPointY);

        Point2D[] points = new Point2D[numAddedStates];

        double curX = offsetVec.getX() + fromX;
        double curY = offsetVec.getY() + fromY;

        int i = 0;
        for (Command tmpCmd : cmd.getCommands()) {
            if (tmpCmd instanceof AddStateCommand) {
                curX += dxPerNode;
                curY += dyPerNode;
                points[i] = new Point2D(curX, curY);
                i++;
            }
        }

        return points;
    }

    public static Point2D randomPoint() {
        return new Point2D(Math.random() * 800, Math.random() * 600);
    }

}
