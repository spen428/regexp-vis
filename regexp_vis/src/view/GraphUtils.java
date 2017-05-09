/*
 * Copyright (c) 2015, 2016 Matthew J. Nicholls, Samuel Pengelly,
 * Parham Ghassemi, William R. Dix
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Rotate;

public final class GraphUtils {
    /**
     * Cosine of 45 degrees
     */
    public static final double COS_45_DEG = Math.sqrt(2) * 0.5;

    /**
     * Cosine of 30 degrees
     */
    public static final double COS_30_DEG = Math.sqrt(3) * 0.5;

    /**
     * @param x The vector x component
     * @param y The vector y component
     * @return The length squared for the given vector
     */
    public static double vecLengthSqr(double x, double y)
    {
        return x * x + y * y;
    }

    /**
     * @param x The vector x component
     * @param y The vector y component
     * @return The length for the given vector
     */
    public static double vecLength(double x, double y)
    {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * Determines if vector (x2, y2) is clockwise to (x1, y1).
     *
     * @param x1 The x component of the vector
     * @param y1 The y component of the vector
     * @param x2 The x component of the other vector
     * @param y2 The y component of the other vector
     * @return True if (x2, y2) is clockwise, false otherwise
     */
    public static boolean vecIsClockwise(double x1, double y1, double x2,
            double y2)
    {
        // Very simple method, found here:
        // http://gamedev.stackexchange.com/q/45412
        if (y1 * x2 > x1 * y2) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Calculates the angle (in degrees) of the specified vector on the circle,
     * formed by going anti-clockwise.
     *
     * @param x The vector x component
     * @param y The vector y component
     * @param r The pre-calculated length of the vector
     * @return The calculated angle
     */
    public static double calcAngleOnCircle(double x, double y, double r)
    {
        // Using the absolute value of y, so we always get a positive value for
        // theta
        double theta = Math.toDegrees(Math.asin(Math.abs(y) / r));
        // Determine which quadrant the point is in, so we can return a value in
        // the range [0, 360), arcsin(x) returns values in the range [-90, 90]
        if (y <= 0) {
            if (x >= 0) {
                // North east quadrant
                return theta;
            } else {
                // North west quadrant
                return 180.0 - theta;
            }
        } else {
            if (x >= 0) {
                // South east quadrant
                return 360.0 - theta;
            } else {
                // South west quadrant
                return 180.0 + theta;
            }
        }
    }

    /**
     * Calculates the angle to rotate text given a desired direction, adjusts
     * the angle such that text should always be display left-to-right to
     * improve readability.
     *
     * @param x The x component of the direction vector
     * @param y The y component of the direction vector
     * @param r The length of the vector
     * @return The calculated angle for the text
     */
    public static double calcTextAngle(double x, double y, double r)
    {
        double angle = calcAngleOnCircle(x, y, r);
        if (angle > 90.0 && angle < 180.0) {
            return angle + 180.0;
        } else if (angle >= 180.0 && angle < 270.0) {
            return angle - 180.0;
        } else {
            return angle;
        }
    }

    /**
     * Utility method used to calculate the "arcExtent" parameter of
     * GraphicsContext.strokeArc. Handles cases such as startAngle = 10,
     * endAngle = 350 producing 340 for example, as the result must be in the
     * [-180, 180] degree range.
     *
     * @param startAngle The starting angle for the arc
     * @param endAngle The ending angle for the arc
     * @return The calculated "arcExtent" value
     */
    public static double arcCalcArcExtent(double startAngle, double endAngle)
    {
        double diffAngle = endAngle - startAngle;
        if (diffAngle > 180.0) {
            diffAngle -= 360.0;
        } else if (diffAngle < -180.0) {
            diffAngle += 360.0;
        }
        return diffAngle;
    }

    /**
     * Calculate the intersection points for two circles, one centred at the
     * origin of radius "r1", and the other centred a point (x2, y2) of radius
     * "r2"
     *
     * @param r1 The radius of the circle centred at the origin
     * @param x2 The x coordinate of the centre of the other circle
     * @param y2 The y coordinate of the centre of the other circle
     * @param r2 The x coordinate of the centre of the other circle
     * @return The two calculated points, as an array. In the form {x1, y1, x2,
     * y2}
     */
    public static double[] calcCircleIntersectionPoints(double r1, double x2, double y2, double r2)
    {
        double r1Sqr = r1 * r1;
        double x2Sqr = x2 * x2;
        double y2Sqr = y2 * y2;
        double r2Sqr = r2 * r2;
        double x2_div_y2 = (x2 / y2);
        double k = (r1Sqr + x2Sqr + y2Sqr - r2Sqr) / (2 * y2);
        if (y2 == 0.0) {
            // Avoid divide by zero, simplifies to a linear equation
            double resultX = (r1Sqr + x2Sqr + y2Sqr - r2Sqr) / (2 * x2);
            double resultY = Math.sqrt(r1Sqr - resultX * resultX);
            return new double[] {resultX, resultY, resultX, -resultY};
        }
        // Quadratic equation to solve
        // a, b, c in ax^2 + bx + c = 0
        double a = x2Sqr / y2Sqr + 1;
        double b = -2 * x2_div_y2 * k;
        double c = k * k - r1Sqr;

        double discriminant = b * b - 4 * a * c;
        if (discriminant < 0) {
            return null;
        }
        double discriminantRoot = Math.sqrt(discriminant);

        double result1x = (-b + discriminantRoot) / (2 * a);
        double result2x = (-b - discriminantRoot) / (2 * a);

        double result1y = k - x2_div_y2 * result1x;
        double result2y = k - x2_div_y2 * result2x;

        return new double[] {result1x, result1y, result2x, result2y};
    }

    /**
     * For the results calculated by calcCircleIntersectionPoints(). Returns the
     * point which lies on the arc by using isPointInArcSection().
     *
     * @param points The points to test, as returned by
     * calcCircleIntersectionPoints()
     * @param x1 The x component of a vector specifying either end of the arc
     * @param y1 The y component of a vector specifying either end of the arc
     * @param x2 The x component of the vector which bisects the arc
     * @param y2 The y component of the vector which bisects the arc
     * @return A single point, as an array, or null if neither point is on the
     * arc
     */
    public static double[] filterArcIntersectionPoint(double[] points, double x1, double y1, double x3, double y3)
    {
        if (points == null) {
            return null;
        }

        if (isPointInArcSection(points[0], points[1], x1, y1, x3, y3)) {
            return new double[] {points[0], points[1]};
        } else if (isPointInArcSection(points[2], points[3], x1, y1, x3, y3)) {
            return new double[] {points[2], points[3]};
        } else {
            return null;
        }
    }

    /**
     * @param x The x coordinate of the point to test
     * @param y The y coordinate of the point to test
     * @param x1 The x component of a vector specifying either end of the arc
     * @param y1 The y component of a vector specifying either end of the arc
     * @param x2 The x component of the vector which bisects the arc
     * @param y2 The y component of the vector which bisects the arc
     * @return True if the point is between the angle formed by the arc, false
     * otherwise
     */
    public static boolean isPointInArcSection(double x, double y, double x1, double y1, double x3, double y3)
    {
        double invVecLength = 1 / vecLength(x3, y3);
        x3 *= invVecLength;
        y3 *= invVecLength;

        double cosAlpha = x1 * x3 + y1 * y3;
        cosAlpha /= vecLength(x1, y1);

        double cosBeta = x * x3 + y * y3;
        cosBeta /= vecLength(x, y);

        return cosBeta > cosAlpha;
    }

    /**
     * Calculate two vectors around a given (unit) vector and given angle. The
     * calculated vectors also both unit vectors.
     *
     * @param x0 The vector x component
     * @param y0 The vector y component
     * @param cosAlpha The cosine of the angle between the given vector, and the
     * calculated vectors returned
     * @return The two calculated vectors, as an array. In the form {x1, y1, x2,
     * y2}
     */
    public static double[] vectorsAround(double x0, double y0, double cosAlpha)
    {
        if (cosAlpha == -1) {
            // Only one vector, in the opposite direction
            return new double[] {-x0, -y0};
        }
        if (y0 == 0) {
            // Special case to handle, otherwise we would get a divide by zero
            double resultX = cosAlpha / x0;
            double resultY1 = Math.sqrt(1 - resultX * resultX);
            double resultY2 = -Math.sqrt(1 - resultX * resultX);
            return new double[] {resultX, resultY1, resultX, resultY2};
        }

        // Solve a quadratic to get the solution
        double x0Sqr = x0 * x0;
        double y0Sqr = y0 * y0;
        double cosAlphaSqr = cosAlpha * cosAlpha;
        double a = x0Sqr / y0Sqr + 1;
        double b = -2 * x0 * cosAlpha / y0Sqr;
        double c = cosAlphaSqr / y0Sqr - 1;

        double discriminantSqr = b * b - 4 * a * c;
        double discriminant = Math.sqrt(discriminantSqr);

        double inv2a = 1 / (2 * a);

        double resultX1 = (-b + discriminant) * inv2a;
        double resultX2 = (-b - discriminant) * inv2a;

        double x0_div_y0 = x0 / y0;
        double cosAlpha_div_y0 = cosAlpha / y0;
        double resultY1 = -x0_div_y0 * resultX1 + cosAlpha_div_y0;
        double resultY2 = -x0_div_y0 * resultX2 + cosAlpha_div_y0;

        return new double[] {resultX1, resultY1, resultX2, resultY2};
    }

    /**
     * Set the rotation for a given GraphicsContext. Needed since JavaFX doesn't
     * have a method which allows us to rotate around an arbitrary point, only
     * at (0,0) presumably.
     *
     * @param gc The GraphicsContext in question
     * @param angle The angle to rotate
     * @param px The x coordinate of the point to rotate about
     * @param py The y coordinate of the point to rotate about
     */
    public static void setGcRotation(GraphicsContext gc, double angle, double px, double py)
    {
        // http://stackoverflow.com/a/18262938
        Rotate r = new Rotate(angle, px, py);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }

    /**
     * Fill an arrow head (just a triangle) of given size, length and position
     *
     * @param gc The GraphicsContext to draw to
     * @param arrowBaseX The x coordinate of the midpoint of the baseline of the
     * arrow
     * @param arrowBaseY The y coordinate of the midpoint of the baseline of the
     * arrow
     * @param arrowTipX The x coordinate of the tip of the arrow
     * @param arrowTipY The y coordinate of the tip of the arrow
     * @param width The width of the baseline of the arrow
     */
    public static void fillArrowHead(GraphicsContext gc, double arrowBaseX,
            double arrowBaseY, double arrowTipX, double arrowTipY, double width)
    {
        double gradientVecX = arrowTipX - arrowBaseX;
        double gradientVecY = arrowTipY - arrowBaseY;
        double length = GraphUtils.vecLength(gradientVecX, gradientVecY);
        double invGradientVecLength = 1 / length;
        gradientVecX *= invGradientVecLength;
        gradientVecY *= invGradientVecLength;

        double baselineGradientX = -gradientVecY;
        double baselineGradientY = gradientVecX;

        double baselinePointX1 = arrowBaseX - baselineGradientX * width * 0.5;
        double baselinePointY1 = arrowBaseY - baselineGradientY * width * 0.5;
        double baselinePointX2 = arrowBaseX + baselineGradientX * width * 0.5;
        double baselinePointY2 = arrowBaseY + baselineGradientY * width * 0.5;

        gc.fillPolygon(
            new double[] { arrowTipX, baselinePointX1,  baselinePointX2 },
            new double[] { arrowTipY, baselinePointY1,  baselinePointY2 }, 3);
    }

    /**
     * Utility function to draw a filled circle centred at a given position and
     * radius, a minor wrapper.
     *
     * @param gc The GraphicsContext to draw to
     * @param x The x coordinate of the centre of the circle
     * @param y The y coordinate of the centre of the circle
     * @param r The radius of the circle
     */
    public static void fillCircleCentred(GraphicsContext gc, double x, double y, double r) {
        double cornerX = x - r;
        double cornerY = y - r;
        double d = r * 2;

        gc.fillOval(cornerX, cornerY, d, d);
    }

    /**
     * Utility function to draw an empty circle centred at a given position and
     * radius, a minor wrapper.
     *
     * @param gc The GraphicsContext to draw to
     * @param x The x coordinate of the centre of the circle
     * @param y The y coordinate of the centre of the circle
     * @param r The radius of the circle
     */
    public static void strokeCircleCentred(GraphicsContext gc, double x, double y, double r) {
        double cornerX = x - r;
        double cornerY = y - r;
        double d = r * 2;

        gc.strokeOval(cornerX, cornerY, d, d);
    }
}
