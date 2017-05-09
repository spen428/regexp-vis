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

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class GraphEdge {
    public static enum RenderMode {
        /**
         * Render nothing.
         */
        NONE,
        /**
         * Render the arrow head only.
         */
        ARROW,
        /**
         * Render both the line / arc and arrow head.
         */
        FULL
    }

    final int mId;
    final GraphNode mFrom;
    final GraphNode mTo;
    String mText;

    double mLabelTestX1;
    double mLabelTestY1;
    double mLabelTestX2;
    double mLabelTestY2;

    double mLabelTestBisectX;
    double mLabelTestBisectY;

    /**
     * Describes what is being rendered for the edge (set by layout
     * calculation). For example, only draw the arrow head if low on space, or
     * if there is no space at all don't render anything.
     */
    RenderMode mRenderMode;
    boolean mIsLine;

    double mArrowBaseX;
    double mArrowBaseY;
    double mStartPointX;
    double mStartPointY;
    double mArrowTipX;
    double mArrowTipY;
    double mArrowWidth;

    double mTextX;
    double mTextY;
    double mTextAngle;
    /**
     * Calculated width of the text, negative if this hasn't been calculated yet
     */
    double mTextWidth;
    double mTextHeight;

    double mArcRadius;
    double mArcCenterX;
    double mArcCenterY;
    double mArcStartAngle;
    double mArcExtent;

    Point2D mMiddlePoint;
    Color mLineColour;

    boolean mIsHoveredOver;

    public GraphEdge(int id, GraphNode from, GraphNode to, String text,
            Color lineColour)
    {
        mId = id;
        mFrom = from;
        mTo = to;
        mText = text;
        mTextWidth = -1.0;
        mTextHeight = -1.0;
        mLineColour = lineColour;
    }

    public int getId()
    {
        return mId;
    }

    public GraphNode getFrom()
    {
        return mFrom;
    }

    public GraphNode getTo()
    {
        return mTo;
    }

    public RenderMode getRenderMode()
    {
        return mRenderMode;
    }

    /**
     * @return The middle point of the edge, for a line this is the midpoint of
     * the line, for an arc this is the peak of the arc.
     */
    public Point2D getEdgeMiddlePoint()
    {
        return mMiddlePoint;
    }

}
