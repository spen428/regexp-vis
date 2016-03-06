package test.view;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import javafx.scene.canvas.GraphicsContext;
import view.GraphUtils;

@SuppressWarnings("static-method")
public class GraphUtilsTest {

    @Test
    public void test00() throws Throwable {
        double double0 = GraphUtils.vecLengthSqr(0.0, 0.0);
        assertEquals(0.0, double0, 0.01D);
    }

    @Test
    public void test01() throws Throwable {
        double double0 = GraphUtils.vecLength((-4624.37), (-4624.37));
        assertEquals(6539.846771431269, double0, 0.01D);
    }

    @Test
    public void test02() throws Throwable {
        double double0 = GraphUtils.calcAngleOnCircle(1025.2694390987,
                (-1113.31077555403), (-1113.31077555403));
        assertEquals((-90.0), double0, 0.01D);
    }

    @Test
    public void test03() throws Throwable {
        double double0 = GraphUtils.arcCalcArcExtent(0.0, 360.0);
        assertEquals(0.0, double0, 0.01D);
    }

    @Test
    public void test04() throws Throwable {
        // Undeclared exception!
        try {
            GraphUtils.fillArrowHead((GraphicsContext) null, (-1.0), (-2109.4),
                    (-1.0), (-1.0), (-2109.4));
            fail("Expecting exception: NullPointerException");

        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }

    @Test
    public void test05() throws Throwable {
        double[] doubleArray0 = GraphUtils.vectorsAround(0.7071067811865476,
                0.0, 2673.384);
        assertArrayEquals(new double[] { 3780.735910231234, Double.NaN,
                3780.735910231234, Double.NaN }, doubleArray0, 0.01);
    }

    @Test
    public void test06() throws Throwable {
        double[] doubleArray0 = GraphUtils.vectorsAround(0.0, 0.0,
                (-69.5603967218878));
        assertArrayEquals(
                new double[] { Double.NEGATIVE_INFINITY, Double.NaN,
                        Double.NEGATIVE_INFINITY, Double.NaN },
                doubleArray0, 0.01);
    }

    @Test
    public void test07() throws Throwable {
        boolean boolean0 = GraphUtils.isPointInArcSection(1681.3879471593905,
                1681.3879471593905, 1681.3879471593905, 1681.3879471593905,
                1681.3879471593905, 1681.3879471593905);
        assertFalse(boolean0);
    }

    @Test
    public void test08() throws Throwable {
        double[] doubleArray0 = GraphUtils.calcCircleIntersectionPoints(
                1264.18972536, 2.0, 0.0, 1264.18972536);
        assertArrayEquals(new double[] { 1.0, 1264.1893298496836, 1.0,
                (-1264.1893298496836) }, doubleArray0, 0.01);
    }

    @Test
    public void test09() throws Throwable {
        double[] doubleArray0 = GraphUtils
                .calcCircleIntersectionPoints(639.5273, 0.0, 440.97, 0.0);
        assertNull(doubleArray0);
    }

    @Test
    public void test10() throws Throwable {
        double double0 = GraphUtils.arcCalcArcExtent(180.0, 0.0);
        assertEquals((-180.0), double0, 0.01D);
    }

    @Test
    public void test11() throws Throwable {
        double double0 = GraphUtils.calcTextAngle(2253.5, (-602.9008065610722),
                (-602.9008065610722));
        assertEquals((-90.0), double0, 0.01D);
    }

    @Test
    public void test12() throws Throwable {
        double double0 = GraphUtils.calcTextAngle(0.0, 639.5273, 639.5273);
        assertEquals(270.0, double0, 0.01D);
    }

    @Test
    public void test13() throws Throwable {
        double double0 = GraphUtils.calcAngleOnCircle(3105.0, 0.0, 0.0);
        assertEquals(Double.NaN, double0, 0.01D);
    }

    @Test
    public void test14() throws Throwable {
        double double0 = GraphUtils.vecLengthSqr(216.507, 216.507);
        assertEquals(93750.56209800001, double0, 0.01D);
    }

    @Test
    public void test15() throws Throwable {
        GraphUtils.calcTextAngle((-397.9), (-397.9), 3092.1946);
        GraphicsContext graphicsContext0 = null;
        // Undeclared exception!
        try {
            GraphUtils.strokeCircleCentred((GraphicsContext) null, 3092.1946,
                    352.60674605759357, 352.60674605759357);
            fail("Expecting exception: NullPointerException");

        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }

    @Test
    public void test16() throws Throwable {
        double[] doubleArray0 = new double[2];
        // Undeclared exception!
        try {
            GraphUtils.filterArcIntersectionPoint(doubleArray0, 0.0, 0.0, 0.0,
                    0.0);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");

        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // 2
            //
        }
    }

    @Test
    public void test17() throws Throwable {
        boolean boolean0 = GraphUtils.isPointInArcSection(639.5273, 2673.384,
                0.0, 2366.24430903189, 2673.384, 2189.0);
        assertTrue(boolean0);
    }

    @Test
    public void test18() throws Throwable {
        double double0 = GraphUtils.calcAngleOnCircle(1030.79, 3768.0,
                (-839.2986785836));
        assertEquals(Double.NaN, double0, 0.01D);
    }

    @Test
    public void test19() throws Throwable {
        double double0 = GraphUtils.calcAngleOnCircle((-509.4885013925),
                (-509.4885013925), 0.026746158801103093);
        assertEquals(Double.NaN, double0, 0.01D);
    }

    @Test
    public void test20() throws Throwable {
        double double0 = GraphUtils.vecLength(0.0, 0.0);
        assertEquals(0.0, double0, 0.01D);
    }

    @Test
    public void test21() throws Throwable {
        double[] doubleArray0 = GraphUtils.vectorsAround(0.0, 0.0, (-1.0));
        assertArrayEquals(new double[] { -0.0, -0.0 }, doubleArray0, 0.01);
    }

    @Test
    public void test22() throws Throwable {
        double[] doubleArray0 = GraphUtils.vectorsAround((-2938.101282),
                (-530.273051980836), 2253.5);
        double[] doubleArray1 = GraphUtils.filterArcIntersectionPoint(
                doubleArray0, 174.85969426486, 396.5866203550845, 2253.5, 0.0);
        assertNull(doubleArray1);
        assertArrayEquals(
                new double[] { (-0.626290353657779), (-0.7795898876429912),
                        (-0.8593024384085348), 0.5114678087085673 },
                doubleArray0, 0.01);
    }

    @Test
    public void test23() throws Throwable {
        double[] doubleArray0 = GraphUtils.vectorsAround(1.0, 2.0, 0.0);
        double[] doubleArray1 = GraphUtils.filterArcIntersectionPoint(
                doubleArray0, (-559.79), 360.0, 360.0, 0.0);
        assertNotNull(doubleArray1);
        assertArrayEquals(
                new double[] { 0.894427190999916, (-0.447213595499958) },
                doubleArray1, 0.01);
    }

    @Test
    public void test24() throws Throwable {
        double[] doubleArray0 = GraphUtils.filterArcIntersectionPoint(
                (double[]) null, 0.0, 2673.384, (-69.5603967218878), 0.0);
        assertNull(doubleArray0);
    }

    @Test
    public void test25() throws Throwable {
        double[] doubleArray0 = GraphUtils.vectorsAround((-2938.101282),
                (-530.273051980836), 2253.5);
        double[] doubleArray1 = GraphUtils.filterArcIntersectionPoint(
                doubleArray0, 2142.2059900281356, 396.5866203550845,
                (-530.273051980836), 2142.2059900281356);
        assertNotNull(doubleArray1);
        assertArrayEquals(
                new double[] { (-0.8593024384085348), 0.5114678087085673 },
                doubleArray1, 0.01);
    }

    @Test
    public void test26() throws Throwable {
        double[] doubleArray0 = GraphUtils.calcCircleIntersectionPoints(
                (-4624.37), (-4.025307475262816), (-4.025307475262816),
                (-4624.37));
        assertNotNull(doubleArray0);
        assertArrayEquals(
                new double[] { 3267.910112578727, (-3271.935420054267),
                        (-3271.935420054267), 3267.910112578727 },
                doubleArray0, 0.01);
    }

    @Test
    public void test27() throws Throwable {
        double[] doubleArray0 = GraphUtils.calcCircleIntersectionPoints(
                (-1509.7910697), (-1509.7910697), 0.0, 0.0);
        assertArrayEquals(
                new double[] { (-1509.7910697), 0.0, (-1509.7910697), -0.0 },
                doubleArray0, 0.01);
    }

    @Test
    public void test28() throws Throwable {
        double double0 = GraphUtils.arcCalcArcExtent(4.0, (-282.0));
        assertEquals(74.0, double0, 0.01D);
    }

    @Test
    public void test29() throws Throwable {
        double double0 = GraphUtils.arcCalcArcExtent(0.0, 3225.2396097237);
        assertEquals(2865.2396097237, double0, 0.01D);
    }

    @Test
    public void test30() throws Throwable {
        double double0 = GraphUtils.arcCalcArcExtent(0.0, 180.0);
        assertEquals(180.0, double0, 0.01D);
    }

    @Test
    public void test31() throws Throwable {
        double double0 = GraphUtils.calcTextAngle((-4.025307475262816), 0.0,
                (-4.025307475262816));
        assertEquals(0.0, double0, 0.01D);
    }

    @Test
    public void test32() throws Throwable {
        double double0 = GraphUtils.calcAngleOnCircle((-4213.239597772244),
                216.507, 216.507);
        assertEquals(270.0, double0, 0.01D);
    }

    @Test
    public void test33() throws Throwable {
        double double0 = GraphUtils.calcAngleOnCircle(0.0, 0.0,
                (-3279.843072802703));
        assertEquals(-0.0, double0, 0.01D);
    }

    @Test
    public void test34() throws Throwable {
        double double0 = GraphUtils.calcTextAngle((-1663.384671), (-2.0), 2.0);
        assertEquals(90.0, double0, 0.01D);
    }

    @Test
    public void test35() throws Throwable {
        double double0 = GraphUtils.calcTextAngle(1298.682951836875,
                1298.682951836875, (-1819.3));
        assertEquals(405.54793641596405, double0, 0.01D);
    }

    @Test
    public void test36() throws Throwable {
        // Undeclared exception!
        try {
            GraphUtils.strokeCircleCentred((GraphicsContext) null, 0.0, 0.0,
                    0.0);
            fail("Expecting exception: NullPointerException");

        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }

    @Test
    public void test37() throws Throwable {
        // Undeclared exception!
        try {
            GraphUtils.setGcRotation((GraphicsContext) null, (-1422.125802124),
                    0.0, 1817.115466);
            fail("Expecting exception: NullPointerException");

        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }

    @Test
    public void test38() throws Throwable {
        // Undeclared exception!
        try {
            GraphUtils.fillCircleCentred((GraphicsContext) null, 0.0,
                    238624.06048649907, (-1551.39));
            fail("Expecting exception: NullPointerException");

        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }

    @Test
    public void test39() throws Throwable {
        // Undeclared exception!
        try {
            GraphUtils.fillArrowHead((GraphicsContext) null, 0.0, 0.0, 0.0, 0.0,
                    0.0);
            fail("Expecting exception: NullPointerException");

        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }

    @Test
    public void test40() throws Throwable {
        GraphUtils graphUtils0 = new GraphUtils();
    }
}
