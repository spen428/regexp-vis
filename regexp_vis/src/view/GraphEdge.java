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
