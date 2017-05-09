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
package test.view;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import javafx.scene.paint.Color;
import view.GraphEdge;
import view.GraphNode;

@SuppressWarnings("static-method")
public class GraphEdgeTest {

    @Test
    public void test0() throws Throwable {
        GraphNode graphNode0 = new GraphNode(1, 3349, (-1.0), 1784.665326,
                false, false, (Color) null);
        GraphEdge graphEdge0 = new GraphEdge(3349, graphNode0, graphNode0, "",
                (Color) null);
        int int0 = graphEdge0.getId();
        assertEquals(3349, int0);
    }

    @Test
    public void test1() throws Throwable {
        GraphNode graphNode0 = new GraphNode((-10), (-10), (-10), (-10), false,
                false, (Color) null);
        GraphEdge graphEdge0 = new GraphEdge((-10), graphNode0, graphNode0,
                (String) null, (Color) null);
        int int0 = graphEdge0.getId();
        assertEquals((-10), int0);
    }

    @Test
    public void test3() throws Throwable {
        GraphNode graphNode0 = new GraphNode(0, 0, 0, 0.0, true, true,
                (Color) null);
        GraphNode graphNode1 = new GraphNode(703, 0.0, 703, (-504.59891684192),
                true, false, (Color) null);
        GraphEdge graphEdge0 = new GraphEdge(276, graphNode0, graphNode1, "W",
                (Color) null);
        assertEquals(276, graphEdge0.getId());
    }

    @Test
    public void test4() throws Throwable {
        GraphNode graphNode0 = new GraphNode(0, 0, 0, 0.0, true, true,
                (Color) null);
        GraphEdge graphEdge0 = new GraphEdge(703, graphNode0, graphNode0,
                (String) null, (Color) null);
        graphEdge0.getRenderMode();
        assertEquals(703, graphEdge0.getId());
    }

    @Test
    public void test5() throws Throwable {
        GraphNode graphNode0 = new GraphNode((-10), (-10), (-10), (-10), false,
                false, (Color) null);
        GraphEdge graphEdge0 = new GraphEdge((-10), graphNode0, graphNode0,
                (String) null, (Color) null);
        graphEdge0.getEdgeMiddlePoint();
        assertEquals(-10, graphEdge0.getId());
    }

    @Test
    public void test6() throws Throwable {
        GraphEdge graphEdge0 = new GraphEdge(0, (GraphNode) null,
                (GraphNode) null, "P^{DP~3l&`Mar-$4r", (Color) null);
        int int0 = graphEdge0.getId();
        assertEquals(0, int0);
    }
}
