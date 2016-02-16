package controller;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;
import java.util.AbstractMap.SimpleImmutableEntry;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jcp.xml.dsig.internal.dom.ApacheCanonicalizer;

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
     * @param above
     *            whether to find the y > midY or the y < midY point
     * @return {@link Point2D} C
     */
    private static Point2D computeThirdPoint(double ax, double ay, double bx,
            double by, boolean above) {
        return computeThirdPoint(ax, ay, bx, by, above, BREAKDOWN_HEIGHT_PX);
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
     * @param above
     *            whether to find the y > midY or the y < midY point
     * @param height
     *            the height of the triangle
     * @return {@link Point2D} C
     */
    private static Point2D computeThirdPoint(double ax, double ay, double bx,
            double by, boolean above, double height) {
        final int sign = (ay < by) ? 1 : -1; // Dictates where to place point C
        final double dx = Math.abs(ax - bx);
        final double dy = Math.abs(ay - by) * sign;
        final double theta = Math.atan(dy / dx) * sign; // Slope of line AB
        final double mx = (ax + (dx / 2)), my = (ay + (dy / 2)); // Midpoint
        final double w = (height * Math.sin(theta));
        final double n = sign
                * (Math.sqrt(Math.pow(height, 2) - Math.pow(w, 2)));

        if (above) {
            final double cx = (mx + w);
            final double cy = (my - n);
            return new Point2D(cx, cy);
        } else {
            final double cx = (mx - w);
            final double cy = (my + n);
            return new Point2D(cx, cy);
        }
    }

    private static Point2D[] computeRectanglePoints(double ax, double ay,
            double bx, double by, boolean above) {
        final int sign = (ay < by) ? 1 : -1;
        final double deltaX = Math.abs(ax - bx);
        final double deltaY = Math.abs(ay - by) * sign;
        final double theta = Math.atan(deltaY / deltaX) * sign;
        final double w = (BREAKDOWN_HEIGHT_PX * Math.sin(theta));
        final double n = sign * (Math
                .sqrt(Math.pow(BREAKDOWN_HEIGHT_PX, 2) - Math.pow(w, 2)));

        if (above) {
            final double cx = (ax - w), cy = (ay + n);
            final double dx = (bx - w), dy = (by + n);
            return new Point2D[] { new Point2D(cx, cy), new Point2D(dx, dy) };
        } else {
            final double cx = (ax - w), cy = (ay + n);
            final double dx = (bx - w), dy = (by + n);
            return new Point2D[] { new Point2D(cx, cy), new Point2D(dx, dy) };
        }
    }

    /**
     * Compute a set of n coordinates that lie at equidistant points along the
     * arc of a segment of chord length AB and the given height.
     * 
     * @param ax
     *            x coordinate of A
     * @param ay
     *            y coordinate of A
     * @param bx
     *            x coordinate of B
     * @param by
     *            y coordinate of B
     * @param numPoints
     *            the number of points that lie on the arc
     * @return a {@link Point2D} array of points
     */
    private static Point2D[] computePoints(double ax, double ay, double bx,
            double by, int numPoints) {
        return computePoints(ax, ay, bx, by, numPoints, BREAKDOWN_HEIGHT_PX,
                true);
    }

    /**
     * Compute a set of n coordinates that lie at equidistant points along the
     * arc of a segment of chord length AB and the given height.
     * 
     * @param ax
     *            x coordinate of A
     * @param ay
     *            y coordinate of A
     * @param bx
     *            x coordinate of B
     * @param by
     *            y coordinate of B
     * @param numPoints
     *            the number of points that lie on the arc
     * @param height
     *            the height of the segment
     * @param above
     *            whether the segment arcs above or below the line AB
     * @return a {@link Point2D} array of points
     */
    private static Point2D[] computePoints(final double ax, final double ay,
            final double bx, final double by, final int numPoints,
            final double height, final boolean above) {
        if (numPoints < 1) {
            throw new IllegalArgumentException();
        }

        final double deltaX = bx - ax;
        final double deltaY = by - ay;
        final double mx = (ax + bx) / 2;
        final double my = (ay + by) / 2;
        final double chordLength = Math.sqrt(
                Math.pow(Math.abs(deltaX), 2) + Math.pow(Math.abs(deltaY), 2));
        final double circleRadius = ((4 * Math.pow(height, 2))
                + Math.pow(chordLength, 2)) / (8 * height);
        final double d = (circleRadius - height);
        final double sectorAngle = 2 * Math.acos(d / circleRadius);
        final double deltaTheta = sectorAngle / numPoints;
        final double gradient = Math.atan(deltaY / deltaX);

        final double cx = mx + d * Math.cos(gradient); // wrong
        final double cy = my + d * Math.sin(gradient); // wrong
        final double offsetAngle = gradient + Math.PI / 4; // wrong

        Point2D[] points = new Point2D[numPoints];

        /* Using the parametric equation of a circle, find set of points. */
        double theta = offsetAngle;

        // System.out.printf("A=(%.1f,%.1f), B=(%.1f,%.1f)%n", ax, ay, bx, by);
        // System.out.printf("AB=%.1f r=%.1f%n", chordLength, circleRadius);
        for (int i = 0; i < numPoints; i++) {
            double x = cx + (circleRadius * Math.cos(theta));
            double y = -cy + (circleRadius * Math.sin(theta));
            points[i] = new Point2D(x, y);
            // System.out.printf("Point %d = (%.1f,%.1f)%n", i, x, y);
            theta += deltaTheta;
        }

        return points;
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
        } else if (numPoints == 1) {
            return new Point2D[] { computeThirdPoint(ax, ay, bx, by, above) };
        }

        final int sign = (ay < by) ? 1 : -1;
        final double deltaX = Math.abs(ax - bx);
        final double deltaY = Math.abs(ay - by) * sign;
        final double dxPerNode = (deltaX / numPoints);
        final double dyPerNode = (deltaY / numPoints);

        double prevX = ax;
        double prevY = ay;
        double curX = ax + dxPerNode;
        double curY = ay + dyPerNode;

        Point2D[] points = new Point2D[numPoints];

        for (int i = 0; i < numPoints; i++) {
            points[i] = computeThirdPoint(prevX, prevY, curX, curY, above,
                    height);
            prevX = curX;
            prevY = curY;
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
     * {@link BreakdownIterationCommand} along a line or curve between the two
     * existing nodes from which this {@link BreakdownIterationCommand} was
     * generated.
     * 
     * @param graph
     *            the {@link GraphCanvasFX}
     * @param cmd
     *            the {@link BreakdownIterationCommand}
     * @return a {@link Point2D} array of the locations to place the new nodes,
     *         in order starting from the "from" end.
     */
    public static Point2D[] placeNodes(GraphCanvasFX graph,
            BreakdownIterationCommand cmd) {
        final AutomatonTransition transition = cmd.getOriginalTransition();
        final AutomatonState fromState = transition.getFrom();
        final AutomatonState toState = transition.getTo();
        final int numAddedStates = BreakdownUITools.getNumAddedStates(cmd);
        final boolean fromChoice = BreakdownUITools.wasChoiceTransition(cmd);
        final GraphNode fromNode = graph.lookupNode(fromState.getId());
        final GraphNode toNode = graph.lookupNode(toState.getId());
        final double dx = Math.abs(fromNode.getX() - toNode.getX());
        final double dy = Math.abs(fromNode.getY() - toNode.getY());
        final double len = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));

        Point2D[] points = new Point2D[numAddedStates];
        if (numAddedStates > 0) {
            boolean above = (numAddedStates == 1); // Arbitrary default value
            if (fromChoice) {
                Point2D edgeMid = graph
                        .lookupEdge(cmd.getOriginalTransition().getId())
                        .getEdgeMiddlePoint();
                above = (edgeMid.getY() > dy);
            }
            if (numAddedStates == 2 && len <= BREAKDOWN_HEIGHT_PX * 2) {
                /* A bit too cramped to make an arc */
                points = computeRectanglePoints(fromNode.getX(),
                        fromNode.getY(), toNode.getX(), toNode.getY(), above);
            } else {
                points = computePoints(fromNode.getX(), fromNode.getY(),
                        toNode.getX(), toNode.getY(), numAddedStates,
                        BREAKDOWN_HEIGHT_PX, above);
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
     *            the {@link BreakdownSequenceCommand}
     * @return a {@link Point2D} array of the locations to place the new nodes,
     *         in order starting from the "from" end.
     */
    public static Point2D[] placeNodes(GraphCanvasFX graph,
            BreakdownSequenceCommand cmd) {
        final AutomatonTransition transition = cmd.getOriginalTransition();
        final GraphNode fromNode = graph
                .lookupNode(transition.getFrom().getId());
        final GraphNode toNode = graph.lookupNode(transition.getTo().getId());

        final int transCount = cmd.getNewTransitionsCount();
        final boolean fromChoice = BreakdownUITools.wasChoiceTransition(cmd);
        final double dxPerNode = (toNode.getX() - fromNode.getX()) / transCount;
        final double dyPerNode = (toNode.getY() - fromNode.getY()) / transCount;

        Point2D[] points = new Point2D[transCount];

        if (fromChoice) {
            /*
             * The nodes should be placed roughly along the curve that
             * previously existed.
             */
            Point2D edgeMid = graph
                    .lookupEdge(cmd.getOriginalTransition().getId())
                    .getEdgeMiddlePoint();
            final double dy = Math.abs(fromNode.getY() - toNode.getY());
            final boolean above = (edgeMid.getY() > dy);
            points = computePoints(fromNode.getX(), fromNode.getY(),
                    toNode.getX(), toNode.getY(), transCount - 1,
                    BREAKDOWN_HEIGHT_PX, above);
        } else {
            /* Didn't breakdown from a choice, just place along a line */
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

    public static Point2D randomPoint() {
        return new Point2D(Math.random() * 800, Math.random() * 600);
    }

}
