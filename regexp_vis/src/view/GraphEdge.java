package view;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class GraphEdge {
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
     * Whether or not this node is being rendered (set by layout 
     * calculation), due to too little space. Could in theory be replaced 
     * with an enumeration if more complex handling of too little space is 
     * implemented at a later date.
     */
    boolean mIsRendered;
    boolean mIsLine;

    double mArrowBaseX;
    double mArrowBaseY;
    double mStartPointX;
    double mStartPointY;
    double mArrowTipX;
    double mArrowTipY;

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
    
    boolean hoverTrue;

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

    public boolean isRendered()
    {
        return mIsRendered;
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
