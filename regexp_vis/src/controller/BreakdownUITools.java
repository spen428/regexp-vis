package controller;

import java.util.LinkedList;
import java.util.List;

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

}
