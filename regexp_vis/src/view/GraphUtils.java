package view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Rotate;

final class GraphUtils {
    /**
     * Cosine of 45 degrees
     */
    public static final double COS_45_DEG = Math.sqrt(2) * 0.5;

    /**
     * Cosine of 30 degrees
     */
    public static final double COS_30_DEG = Math.sqrt(3) * 0.5;

    public static double vecLengthSqr(double x, double y)
    {
        return x * x + y * y;
    }

    public static double vecLength(double x, double y)
    {
        return Math.sqrt(x * x + y * y);
    }

    public static double calcAngleOnCircle(double x, double y, double r)
    {
        // TODO: document: assumption that parameter "r" is calculated correctly
        //                 assumption circle is centred at (0,0)

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

    public static double[] calcCircleIntersectionPoints(double r1, double x2, double y2, double r2)
    {
        // TODO: document: assume circle 1 is at the origin to simplify 
        // equations
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

    public static double[] filterArcIntersectionPoint(double[] points, double x1, double y1, double x2, double y2, double x3, double y3)
    {
        if (points == null) {
            return null;
        }

        if (isPointInArcSection(points[0], points[1], x1, y1, x2, y2, x3, y3)) {
            return new double[] {points[0], points[1]};
        } else if (isPointInArcSection(points[2], points[3], x1, y1, x2, y2, x3, y3)) {
            return new double[] {points[2], points[3]};
        } else {
            return null;
        }
    }

    public static boolean isPointInArcSection(double x, double y, double x1, double y1, double x2, double y2, double x3, double y3)
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

    public static double[] vectorsAround(double x0, double y0, double cosAlpha)
    {
        if (cosAlpha == -1) {
            return new double[] {-x0, -y0};
        }
        if (y0 == 0) {
            double resultX = cosAlpha / x0;
            double resultY1 = Math.sqrt(1 - resultX * resultX);
            double resultY2 = -Math.sqrt(1 - resultX * resultX);
            return new double[] {resultX, resultY1, resultX, resultY2};
        }

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

    public static void setGcRotation(GraphicsContext gc, double angle, double px, double py) 
    {
        // http://stackoverflow.com/a/18262938
        Rotate r = new Rotate(angle, px, py);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }

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

    public static void fillCircleCentred(GraphicsContext gc, double x, double y, double r) {
        double cornerX = x - r;
        double cornerY = y - r;
        double d = r * 2;

        gc.fillOval(cornerX, cornerY, d, d);
    }

    public static void strokeCircleCentred(GraphicsContext gc, double x, double y, double r) {
        double cornerX = x - r;
        double cornerY = y - r;
        double d = r * 2;

        gc.strokeOval(cornerX, cornerY, d, d);
    }
}
