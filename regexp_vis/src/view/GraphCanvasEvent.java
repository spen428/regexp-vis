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

import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.input.MouseEvent;

public class GraphCanvasEvent extends Event {
    /**
     * Super type for all graph canvas specific events
     */
    // Not sure if it would be better to use InputEvent.ANY
    public static final EventType<GraphCanvasEvent> ANY = new EventType<>(
            EventType.ROOT);

    private MouseEvent mMouseEvent;
    private GraphNode mTargetNode;
    private GraphEdge mTargetEdge;

    public GraphCanvasEvent(MouseEvent event, GraphNode targetNode,
            GraphEdge targetEdge)
    {
        super(ANY);
        mMouseEvent = event;
        mTargetNode = targetNode;
        mTargetEdge = targetEdge;
    }

    public MouseEvent getMouseEvent()
    {
        return mMouseEvent;
    }

    public GraphNode getTargetNode()
    {
        return mTargetNode;
    }

    public GraphEdge getTargetEdge()
    {
        return mTargetEdge;
    }
}
