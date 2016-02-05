package view;

import java.util.ArrayList;

public class GraphNode {
    /**
     * The ID for this node
     */
    final int mId;
    /**
     * Coordinates of the centre of this node
     */
    double mX, mY;
    /**
     * The radius this node will be rendered with
     */
    double mRadius;
    boolean mUseStartStateStyle;
    boolean mUseFinalStateStyle;

    double mLoopDirVecX;
    double mLoopDirVecY;
    //double mLoopDirNormalVecX;
    //double mLoopDirNormalVecY;

    double mStartLineX1;
    double mStartLineY1;
    double mStartLineX2;
    double mStartLineY2;

    double mEndLineX1;
    double mEndLineY1;
    double mEndLineX2;
    double mEndLineY2;

    double mStartAngle;
    double mArcExtent;

    public GraphNode(int id, double x, double y, double r, boolean startStyle, boolean finalStyle)
    {
        mId = id;
        mX = x;
        mY = y;
        mRadius = r;
        mUseStartStateStyle = startStyle;
        mUseFinalStateStyle = finalStyle;

        // Loops are on top by default
        mLoopDirVecX = 0;
        mLoopDirVecY = -1;
        //mLoopDirNormalVecX = -mLoopDirVecY;
        //mLoopDirNormalVecY = mLoopDirVecX;
    }

    public double getX()
    {
        return mX;
    }

    public double getY()
    {
        return mY;
    }
    
    public int getId() {
        return this.mId;
    }
}
