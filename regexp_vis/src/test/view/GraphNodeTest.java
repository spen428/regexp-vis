package test.view;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import javafx.scene.paint.Color;
import view.GraphNode;

@SuppressWarnings("static-method")
public class GraphNodeTest {

    @Test
    public void test0() throws Throwable {
        GraphNode graphNode0 = new GraphNode(41, 41, 41, 41, true, true,
                (Color) null);
        double double0 = graphNode0.getY();
        assertEquals(41.0, double0, 0.01D);
        assertEquals(41.0, graphNode0.getX(), 0.01D);
        assertEquals(41, graphNode0.getId());
    }

    @Test
    public void test1() throws Throwable {
        GraphNode graphNode0 = new GraphNode(1338, 0.0, 1338, (-1.0), false,
                false, (Color) null);
        double double0 = graphNode0.getX();
        assertEquals(1338.0, graphNode0.getY(), 0.01D);
        assertEquals(0.0, double0, 0.01D);
        assertEquals(1338, graphNode0.getId());
    }

    @Test
    public void test2() throws Throwable {
        GraphNode graphNode0 = new GraphNode((-2912), (-2912), (-2912), (-2912),
                false, false, (Color) null);
        double double0 = graphNode0.getX();
        assertEquals((-2912.0), graphNode0.getY(), 0.01D);
        assertEquals(-2912, graphNode0.getId());
        assertEquals((-2912.0), double0, 0.01D);
    }

    @Test
    public void test3() throws Throwable {
        GraphNode graphNode0 = new GraphNode(0, 0, 0, (-101.2911020273539),
                true, true, (Color) null);
        int int0 = graphNode0.getId();
        assertEquals(0.0, graphNode0.getY(), 0.01D);
        assertEquals(0, int0);
        assertEquals(0.0, graphNode0.getX(), 0.01D);
    }

    @Test
    public void test4() throws Throwable {
        GraphNode graphNode0 = new GraphNode((-2912), (-2912), (-2912), (-2912),
                false, false, (Color) null);
        int int0 = graphNode0.getId();
        assertEquals((-2912.0), graphNode0.getY(), 0.01D);
        assertEquals((-2912), int0);
        assertEquals((-2912.0), graphNode0.getX(), 0.01D);
    }

    @Test
    public void test5() throws Throwable {
        GraphNode graphNode0 = new GraphNode(0, 0, 0, (-367.6389), false, true,
                (Color) null);
        double double0 = graphNode0.getY();
        assertEquals(0.0, graphNode0.getX(), 0.01D);
        assertEquals(0.0, double0, 0.01D);
        assertEquals(0, graphNode0.getId());
    }

    @Test
    public void test6() throws Throwable {
        GraphNode graphNode0 = new GraphNode(2103, 3114.17451490969, 0.0, 2103,
                true, false, (Color) null);
        double double0 = graphNode0.getX();
        assertEquals(2103, graphNode0.getId());
        assertEquals(3114.17451490969, double0, 0.01D);
        assertEquals(0.0, graphNode0.getY(), 0.01D);
    }

    @Test
    public void test7() throws Throwable {
        GraphNode graphNode0 = new GraphNode(350, 350, 350, 0.0, true, false,
                (Color) null);
        int int0 = graphNode0.getId();
        assertEquals(350, int0);
        assertEquals(350.0, graphNode0.getY(), 0.01D);
        assertEquals(350.0, graphNode0.getX(), 0.01D);
    }

    @Test
    public void test8() throws Throwable {
        GraphNode graphNode0 = new GraphNode((-1), 0.0, (-1), 0.0, true, true,
                (Color) null);
        double double0 = graphNode0.getY();
        assertEquals(0.0, graphNode0.getX(), 0.01D);
        assertEquals((-1.0), double0, 0.01D);
        assertEquals(-1, graphNode0.getId());
    }
}
