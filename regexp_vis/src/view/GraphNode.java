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

    Point2D mStartLineP1;
    Point2D mStartLineDir;
    Point2D mEndLineP1;
    Point2D mEndLineDir;

    double mStartAngle;
    double mArcExtent;

    Color mBackgroundColour;
    boolean mIsTransparent;

    public GraphNode(int id, double x, double y, double r, boolean startStyle,
            boolean finalStyle, Color backgroundColour)
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

        mBackgroundColour = backgroundColour;
        mIsTransparent = false;
    }

    public double getX()
    {
        return mX;
    }

    public double getY()
    {
        return mY;
    }

    public int getId()
    {
        return mId;
    }
}
