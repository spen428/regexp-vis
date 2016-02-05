package view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;

public final class GraphCanvasFX extends Canvas {
    private static class NodeEdgePair {
        final GraphNode mNode;
        /**
         * Edges which come from this node to another, different node
         */
        final ArrayList<GraphEdge> mEdges;
        /**
         * Edges which come from this node and "loop back" to this node
         */
        final ArrayList<GraphEdge> mLoopedEdges;

        NodeEdgePair(GraphNode node) {
            mNode = node;
            mEdges = new ArrayList<GraphEdge>();
            mLoopedEdges = new ArrayList<GraphEdge>();
        }
    }

    /**
     * All nodes and edges
     */
    private HashMap<Integer, NodeEdgePair> mGraph;
    /**
     * Current node we are dragging, null if we aren't dragging anything
     */
    private GraphNode mDragNode;
    /**
     * Current edge we are dragging, null if we aren't dragging anything
     */
    private GraphEdge mDragEdge;
    /**
     * Coordinates of where the node was originally before we started dragging
     * it
     */
    private double mDragOrigX, mDragOrigY;
    /**
     * Coordinates of where the last onMouseDown event was
     */
    private double mDownX, mDownY;
    /**
     * The highest x coordinate and highest y coordinate of all nodes (note
     * these could be separate nodes). Used to handle placement when canvas is
     * resized too small.
     */
    private double mMaxNodeX, mMaxNodeY;
    /**
     * The graphics context we are using to do rendering
     */
    private GraphicsContext mGC;
    /**
     * The font we are using to render labels for edges
     */
    private Font mLabelFont;
    /**
     * The font we are using to render text on nodes
     */
    private Font mNodeFont;
    /**
     * The colour we are using to render the labels for edges
     */
    private Color mEdgeLabelColour;
    /**
     * The colour we are using to render the lines/arcs for edges
     */
    private Color mEdgeLineColour;
    /**
     * The colour we are using to draw the background of nodes
     */
    private Color mNodeBackgroundColour;
    /**
     * The colour we are using to draw the border of nodes
     */
    private Color mNodeBorderColour;
    /**
     * The colour we are using to render text on nodes
     */
    private Color mNodeTextColour;
    /**
     * Event handler for when a node is clicked / double clicked
     */
    private EventHandler<GraphCanvasEvent> mNodeClickedHandler;
    /**
     * Event handler for a when an edge label is clicked / double clicked
     */
    private EventHandler<GraphCanvasEvent> mEdgeClickedHandler;
    /**
     * Event handler for a when neither an edge label or node is clicked (just
     * the background)
     */
    private EventHandler<GraphCanvasEvent> mBackgroundClickedHandler;

    public GraphCanvasFX() {
        super();
        // /mNodes = new ArrayList<>();
        // mEdges = new ArrayList<>();
        mGraph = new HashMap<Integer, NodeEdgePair>();
        mGC = getGraphicsContext2D();
        mLabelFont = Font.font("Consolas", 16.0);
        mNodeFont = Font.font("Consolas", 16.0);
        mEdgeLabelColour = Color.BLACK;
        mEdgeLineColour = Color.BLACK;
        mNodeBackgroundColour = Color.WHITE;
        mNodeBorderColour = Color.BLACK;
        mNodeTextColour = Color.BLACK;

        // Delegate event handling to class methods
        setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onMouseDragged(event);
            }
        });

        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onMousePressed(event);
            }
        });

        setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onMouseReleased(event);
            }
        });

        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onMouseClicked(event);
            }
        });
    }

    // Input event handling registration methods, tried to keep the same
    // convention as JavaFX's Node class

    public final void setOnNodeClicked(EventHandler<GraphCanvasEvent> handler) {
        mNodeClickedHandler = handler;
    }

    public final void setOnEdgeClicked(EventHandler<GraphCanvasEvent> handler) {
        mEdgeClickedHandler = handler;
    }

    public final void setOnBackgroundClicked(
            EventHandler<GraphCanvasEvent> handler) {
        mBackgroundClickedHandler = handler;
    }

    public GraphNode lookupNode(int id) {
        NodeEdgePair pair = mGraph.get(id);
        if (pair == null) {
            return null;
        } else {
            return pair.mNode;
        }
    }

    public GraphEdge lookupEdge(int id) {
        // TODO: Not too efficient, my be a good idea to store the edges in a
        // map as well
        for (NodeEdgePair pair : mGraph.values()) {
            for (GraphEdge e : pair.mEdges) {
                if (e.mId == id) {
                    return e;
                }
            }
            for (GraphEdge e : pair.mLoopedEdges) {
                if (e.mId == id) {
                    return e;
                }
            }
        }
        return null;
    }

    public GraphNode addNode(int id, double x, double y) {
        // Need to check the ID, otherwise we would overwrite the previous value
        if (mGraph.containsKey(id)) {
            throw new RuntimeException("Node ID already exists");
        }

        double radius = DEFAULT_NODE_RADIUS;

        // Prevent placing the node outside the canvas, preventing the user from
        // ever dragging it
        x = Math.min(getWidth() - radius, x);
        x = Math.max(radius, x);
        y = Math.min(getHeight() - radius, y);
        y = Math.max(radius, y);

        GraphNode n = new GraphNode(id, x, y, radius, false, false);
        NodeEdgePair pair = new NodeEdgePair(n);
        mGraph.put(id, pair);

        // TODO: be smarter about this (do minimum amount of layout
        // recalculation necessary)
        updateAllLayoutData();
        doRedraw();
        return n;
    }

    public void setNodeUseFinalStyle(GraphNode n, boolean value) {
        // No layout data update required (for now at least)
        n.mUseFinalStateStyle = value;
    }

    public void setNodeUseStartStyle(GraphNode n, boolean value) {
        // No layout data update required (for now at least)
        n.mUseStartStateStyle = value;
    }

    public void setEdgeLabelText(GraphEdge edge, String text) {
        edge.mText = text;
        // Text metrics need to be recalculated
        edge.mTextWidth = -1.0;
        edge.mTextHeight = -1.0;
    }

    public void removeAllNodes() {
        mGraph.clear();
    }

    public void removeNode(int id) {
        NodeEdgePair pair = mGraph.get(id);
        if (pair == null) {
            throw new RuntimeException("Node ID doesn't exist");
        }

        mGraph.remove(id);
    }

    public GraphEdge addEdge(int id, GraphNode from, GraphNode to, String text) {
        NodeEdgePair pair = mGraph.get(from.mId);
        if (pair == null) {
            throw new RuntimeException("Node ID doesn't exist");
        }

        // Check a transition doesn't already exist
        for (GraphEdge edge : pair.mEdges) {
            if (edge.mId == id) {
                throw new RuntimeException("Edge ID already exists");
            }
        }
        for (GraphEdge edge : pair.mLoopedEdges) {
            if (edge.mId == id) {
                throw new RuntimeException("Edge ID already exists");
            }
        }

        GraphEdge e = new GraphEdge(id, from, to, text);
        if (from == to) {
            pair.mLoopedEdges.add(e);
        } else {
            pair.mEdges.add(e);
        }

        // TODO: be smarter about this (do minimum amount of layout
        // recalculation necessary)
        updateAllLayoutData();
        doRedraw();
        return e;
    }

    public void removeEdge(int id) {
        NodeEdgePair pair = mGraph.get(id);
        if (pair == null) {
            throw new RuntimeException("Node ID doesn't exist");
        }

        // Check a transition doesn't already exist
        Iterator<GraphEdge> it = pair.mEdges.iterator();
        while (it.hasNext()) {
            GraphEdge edge = it.next();
            if (edge.mId == id) {
                it.remove();
                return;
            }
        }
        it = pair.mLoopedEdges.iterator();
        while (it.hasNext()) {
            GraphEdge edge = it.next();
            if (edge.mId == id) {
                it.remove();
                return;
            }
        }

        throw new RuntimeException("Edge ID doesn't exists");
    }

    private final static double DEFAULT_NODE_RADIUS = 20;
    /**
     * The distance between TODO:
     */
    private final static double FINAL_STATE_BORDER_GAP = 3;
    /**
     * How long the line pointing to the initial node should be (not including
     * the arrow)
     */
    private final static double INITIAL_STATE_LINE_LENGTH = 50;
    /**
     * How long arrows should be
     */
    private final static double ARROW_LENGTH = 20;
    /**
     * How wide a base arrows should have
     */
    private final static double ARROW_WIDTH = 10;
    /**
     * How high the text should be rendered above an arc (centre point)
     */
    private final static double TEXT_POS_HEIGHT = 10;
    /**
     * The base distance looping edges should be from the node itself
     */
    private final static double ARC_LOOP_BASE_DISTANCE = 30;
    /**
     * The desired gap between arcs
     */
    private final static double ARC_GAP_SIZE = 30;
    /**
     * For looped edges, the cosine of half the angle of the arcs
     */
    private final static double ARC_LOOP_COS_HALF_ANGLE = Math.sqrt(3) * 0.5;

    private void updateEdgeLineLayoutData(GraphEdge edge, double textAngle) {
        // Set "mIsRendered" here so we can exit the method early to avoid
        // rendering the line if problems arise
        edge.mIsRendered = false;

        GraphNode from = edge.mFrom;
        GraphNode to = edge.mTo;
        double x1 = from.mX;
        double y1 = from.mY;
        double x2 = to.mX;
        double y2 = to.mY;
        double lengthSqr = GraphUtils.vecLengthSqr(x2 - x1, y2 - y1);
        double l = Math.sqrt(lengthSqr);
        double midPointX = (x1 + x2) * 0.5;
        double midPointY = (y1 + y2) * 0.5;
        double S_E_gradientVecX = x2 - x1;
        double S_E_gradientVecY = y2 - y1;

        // Check distance is large enough to draw arrow
        if (l < from.mRadius + to.mRadius + ARROW_LENGTH) {
            System.out
                    .println("DEBUG: couldn't draw line, too little distance");
            return;
        }

        double invVecLength = 1 / GraphUtils.vecLength(S_E_gradientVecX,
                S_E_gradientVecY);
        S_E_gradientVecX *= invVecLength;
        S_E_gradientVecY *= invVecLength;

        double G_C_gradientVecX = -S_E_gradientVecY;
        double G_C_gradientVecY = S_E_gradientVecX;

        double newX1, newY1, newX2, newY2, arrowX, arrowY;
        newX1 = x1 + S_E_gradientVecX * from.mRadius;
        newY1 = y1 + S_E_gradientVecY * from.mRadius;
        newX2 = x2 - S_E_gradientVecX * to.mRadius;
        newY2 = y2 - S_E_gradientVecY * to.mRadius;
        arrowX = newX2 - S_E_gradientVecX * ARROW_LENGTH;
        arrowY = newY2 - S_E_gradientVecY * ARROW_LENGTH;

        // Draw label
        double textX = midPointX - TEXT_POS_HEIGHT * G_C_gradientVecX;
        double textY = midPointY - TEXT_POS_HEIGHT * G_C_gradientVecY;
        updateEdgeLabelHitTestData(edge, textX, textY, S_E_gradientVecX,
                S_E_gradientVecY, G_C_gradientVecX, G_C_gradientVecY);

        edge.mArrowBaseX = arrowX;
        edge.mArrowBaseY = arrowY;
        edge.mStartPointX = newX1;
        edge.mStartPointY = newY1;
        edge.mArrowTipX = newX2;
        edge.mArrowTipY = newY2;

        edge.mIsRendered = true;
        edge.mIsLine = true;

        edge.mTextX = textX;
        edge.mTextY = textY;
        edge.mTextAngle = textAngle;
    }

    private void updateEdgeArcLayoutData(GraphEdge edge, double height,
            double textAngle) {
        // Set "mIsRendered" here so we can exit the method early to avoid
        // rendering the arc if problems arise
        edge.mIsRendered = false;

        GraphNode from = edge.mFrom;
        GraphNode to = edge.mTo;
        double x1 = from.mX;
        double y1 = from.mY;
        double x2 = to.mX;
        double y2 = to.mY;
        double absHeight = Math.abs(height);
        double lengthSqr = GraphUtils.vecLengthSqr(x2 - x1, y2 - y1);
        double midPointX = (x1 + x2) * 0.5;
        double midPointY = (y1 + y2) * 0.5;
        double radius = (0.25 * lengthSqr + height * height) / (2 * absHeight);

        double S_E_gradientVecX = x2 - x1;
        double S_E_gradientVecY = y2 - y1;
        // Normalise to get a unit vector (so it is easy to move a given
        // distance along the line)
        double invVecLength = 1 / GraphUtils.vecLength(S_E_gradientVecX,
                S_E_gradientVecY);
        S_E_gradientVecX *= invVecLength;
        S_E_gradientVecY *= invVecLength;
        double G_C_gradientVecX = -S_E_gradientVecY;
        double G_C_gradientVecY = S_E_gradientVecX;
        // Handle negative height, arc goes in the opposite direction
        if (height < 0) {
            G_C_gradientVecX = S_E_gradientVecY;
            G_C_gradientVecY = -S_E_gradientVecX;
        }

        // TODO: document: work around: assumption that radius >= height
        double circleX = midPointX + (radius - absHeight) * G_C_gradientVecX;
        double circleY = midPointY + (radius - absHeight) * G_C_gradientVecY;

        double tmpGradientX = y2 - y1;
        double tmpGradientY = x1 - x2;
        // Handle negative height, arc goes in the opposite direction
        if (height < 0) {
            tmpGradientX = y1 - y2;
            tmpGradientY = x2 - x1;
        }
        double[] results = GraphUtils.calcCircleIntersectionPoints(radius, x1
                - circleX, y1 - circleY, from.mRadius);
        results = GraphUtils.filterArcIntersectionPoint(results, x1 - circleX,
                y1 - circleY, x2 - circleX, y2 - circleY, tmpGradientX,
                tmpGradientY);
        double newX1, newY1;
        if (results == null) {
            System.out
                    .println("DEBUG: couldn't draw arc, probably too little distance (1)");
            return;
        } else {
            newX1 = results[0] + circleX;
            newY1 = results[1] + circleY;
        }

        tmpGradientX = y2 - newY1;
        tmpGradientY = newX1 - x2;
        // Handle negative height, arc goes in the opposite direction
        if (height < 0) {
            tmpGradientX = newY1 - y2;
            tmpGradientY = x2 - newX1;
        }
        results = GraphUtils.calcCircleIntersectionPoints(radius, x2 - circleX,
                y2 - circleY, to.mRadius);
        results = GraphUtils.filterArcIntersectionPoint(results, newX1
                - circleX, newY1 - circleY, x2 - circleX, y2 - circleY,
                tmpGradientX, tmpGradientY);
        double newX2, newY2;
        if (results == null) {
            System.out
                    .println("DEBUG: couldn't draw arc, probably too little distance (2)");
            return;
        } else {
            newX2 = results[0] + circleX;
            newY2 = results[1] + circleY;
        }

        tmpGradientX = newY2 - newY1;
        tmpGradientY = newX1 - newX2;
        // Handle negative height, arc goes in the opposite direction
        if (height < 0) {
            tmpGradientX = newY1 - newY2;
            tmpGradientY = newX2 - newX1;
        }
        results = GraphUtils.calcCircleIntersectionPoints(radius, newX2
                - circleX, newY2 - circleY, ARROW_LENGTH);
        results = GraphUtils.filterArcIntersectionPoint(results, newX1
                - circleX, newY1 - circleY, newX2 - circleX, newY2 - circleY,
                tmpGradientX, tmpGradientY);
        double arrowX, arrowY;
        if (results == null) {
            System.out
                    .println("DEBUG: couldn't draw arc, probably too little distance (3)");
            return;
        } else {
            arrowX = results[0] + circleX;
            arrowY = results[1] + circleY;
        }

        // Draw label
        double textX = midPointX - (absHeight + TEXT_POS_HEIGHT)
                * G_C_gradientVecX;
        double textY = midPointY - (absHeight + TEXT_POS_HEIGHT)
                * G_C_gradientVecY;
        updateEdgeLabelHitTestData(edge, textX, textY, S_E_gradientVecX,
                S_E_gradientVecY, G_C_gradientVecX, G_C_gradientVecY);

        double startAngle = GraphUtils.calcAngleOnCircle(newX1 - circleX, newY1
                - circleY, radius);
        double endAngle = GraphUtils.calcAngleOnCircle(arrowX - circleX, arrowY
                - circleY, radius);
        double arcExtent = GraphUtils.arcCalcArcExtent(startAngle, endAngle);

        edge.mArrowBaseX = arrowX;
        edge.mArrowBaseY = arrowY;
        edge.mStartPointX = newX1;
        edge.mStartPointY = newY1;
        edge.mArrowTipX = newX2;
        edge.mArrowTipY = newY2;

        edge.mIsRendered = true;
        edge.mIsLine = false;

        edge.mTextX = textX;
        edge.mTextY = textY;
        edge.mTextAngle = textAngle;

        edge.mArcRadius = radius;
        edge.mArcCenterX = circleX;
        edge.mArcCenterY = circleY;
        edge.mArcStartAngle = startAngle;
        edge.mArcExtent = arcExtent;
    }

    private void updateEdgesLoopedLayoutData(GraphNode n,
            ArrayList<GraphEdge> edges) {
        double maxRadius = n.mRadius + ARC_LOOP_BASE_DISTANCE + ARC_GAP_SIZE
                * (edges.size() - 1);

        double[] results = GraphUtils.vectorsAround(n.mLoopDirVecX,
                n.mLoopDirVecY, ARC_LOOP_COS_HALF_ANGLE);
        double startVecX = results[0];
        double startVecY = results[1];
        double endVecX = results[2];
        double endVecY = results[3];

        double startAngle = GraphUtils.calcAngleOnCircle(startVecX, startVecY,
                1);
        double endAngle = GraphUtils.calcAngleOnCircle(endVecX, endVecY, 1);

        n.mStartAngle = startAngle;
        n.mArcExtent = GraphUtils.arcCalcArcExtent(startAngle, endAngle);

        n.mStartLineX1 = n.mX + startVecX * n.mRadius;
        n.mStartLineY1 = n.mY + startVecY * n.mRadius;
        n.mStartLineX2 = n.mX + startVecX * maxRadius;
        n.mStartLineY2 = n.mY + startVecY * maxRadius;
        n.mEndLineX1 = n.mX + endVecX * n.mRadius;
        n.mEndLineY1 = n.mY + endVecY * n.mRadius;
        n.mEndLineX2 = n.mX + endVecX * maxRadius;
        n.mEndLineY2 = n.mY + endVecY * maxRadius;

        double loopDirNormalVecX = -n.mLoopDirVecY;
        double loopDirNormalVecY = n.mLoopDirVecX;

        double textAngle = GraphUtils.calcTextAngle(loopDirNormalVecX,
                loopDirNormalVecY, 1);// GraphUtils.calcAngleOnCircle(n.mLoopDirNormalVecX,
                                      // n.mLoopDirNormalVecY, 1);
        double tmpRadius = n.mRadius + ARC_LOOP_BASE_DISTANCE;
        for (GraphEdge edge : edges) {
            double textX = n.mX + (tmpRadius + TEXT_POS_HEIGHT)
                    * n.mLoopDirVecX;
            double textY = n.mY + (tmpRadius + TEXT_POS_HEIGHT)
                    * n.mLoopDirVecY;

            edge.mIsLine = false;
            edge.mIsRendered = true;

            edge.mTextX = textX;
            edge.mTextY = textY;
            edge.mTextAngle = textAngle;

            edge.mArcRadius = tmpRadius;
            edge.mArcCenterX = n.mX;
            edge.mArcCenterY = n.mY;

            updateEdgeLabelHitTestData(edge, textX, textY, loopDirNormalVecX,
                    loopDirNormalVecY, n.mLoopDirVecX, n.mLoopDirVecY);
            tmpRadius += ARC_GAP_SIZE;
        }
    }

    private void updateConnectionLayoutData(NodeEdgePair pair1,
            NodeEdgePair pair2) {
        GraphNode n1 = pair1.mNode;
        GraphNode n2 = pair2.mNode;

        // Count the number of edges between these two nodes
        int count = 0;
        for (GraphEdge e : pair1.mEdges) {
            if (e.mTo == n2) {
                count++;
            }
        }
        for (GraphEdge e : pair2.mEdges) {
            if (e.mTo == n1) {
                count++;
            }
        }

        // Mid edge index refers to the middle edge which should be rendered as
        // a straight line, remains -1 for an even number of connections
        int midEdgeIdx = -1;
        if ((count % 2) != 0) {
            // Odd number of connections
            midEdgeIdx = count / 2;
        }

        double height = (count - 1) * ARC_GAP_SIZE * 0.5;
        double length = GraphUtils.vecLength(n2.mX - n1.mX, n2.mY - n1.mY);
        double textAngle = GraphUtils.calcTextAngle(n2.mX - n1.mX, n2.mY
                - n1.mY, length);
        int i = 0;

        for (GraphEdge e : pair1.mEdges) {
            if (e.mTo != n2) {
                continue;
            }
            if (i != midEdgeIdx) {
                updateEdgeArcLayoutData(e, height - i * ARC_GAP_SIZE, textAngle);
                // /drawArcEdge(e, height - k * ARC_GAP_SIZE);
            } else {
                updateEdgeLineLayoutData(e, textAngle);
                // /drawLineEdge(e);
            }
            i++;
        }
        for (GraphEdge e : pair2.mEdges) {
            if (e.mTo != n1) {
                continue;
            }

            if (i != midEdgeIdx) {
                // Negative since the arc is going in the opposite direction
                updateEdgeArcLayoutData(e, -(height - i * ARC_GAP_SIZE),
                        textAngle);
                // /drawArcEdge(e, -(height - k * ARC_GAP_SIZE));
            } else {
                updateEdgeLineLayoutData(e, textAngle);
                // /drawLineEdge(e);
            }
            i++;
        }
    }

    private void updateAllLayoutData() {
        for (NodeEdgePair pair : mGraph.values()) {
            GraphNode n = pair.mNode;
            // drawNode(n);

            updateEdgesLoopedLayoutData(n, pair.mLoopedEdges);
        }

        Object[] tmpPairs = mGraph.values().toArray();

        for (int i = 0; i < tmpPairs.length; i++) {
            for (int j = i + 1; j < tmpPairs.length; j++) {
                updateConnectionLayoutData((NodeEdgePair) tmpPairs[i],
                        (NodeEdgePair) tmpPairs[j]);
            }
        }
    }

    private void drawEdgeLine(GraphEdge edge) {
        mGC.setFill(mEdgeLabelColour);
        mGC.setFontSmoothingType(FontSmoothingType.LCD);
        mGC.setFont(mLabelFont);
        mGC.setTextAlign(TextAlignment.CENTER);
        mGC.setTextBaseline(VPos.CENTER);

        GraphUtils.setGcRotation(mGC, -edge.mTextAngle, edge.mTextX,
                edge.mTextY);
        mGC.fillText(edge.mText, edge.mTextX, edge.mTextY);
        // Turn off rotation, identity transformation
        mGC.setTransform(new Affine());

        mGC.setStroke(mEdgeLineColour);
        mGC.setFill(mEdgeLineColour);
        mGC.strokeLine(edge.mStartPointX, edge.mStartPointY, edge.mArrowBaseX,
                edge.mArrowBaseY);

        GraphUtils.fillArrowHead(mGC, edge.mArrowBaseX, edge.mArrowBaseY,
                edge.mArrowTipX, edge.mArrowTipY, ARROW_WIDTH);
    }

    private void drawEdgeArc(GraphEdge edge) {
        mGC.setFill(mEdgeLabelColour);
        mGC.setFontSmoothingType(FontSmoothingType.LCD);
        mGC.setFont(mLabelFont);
        mGC.setTextAlign(TextAlignment.CENTER);
        mGC.setTextBaseline(VPos.CENTER);

        GraphUtils.setGcRotation(mGC, -edge.mTextAngle, edge.mTextX,
                edge.mTextY);
        mGC.fillText(edge.mText, edge.mTextX, edge.mTextY);
        // Turn off rotation, identity transformation
        mGC.setTransform(new Affine());

        mGC.setStroke(mEdgeLineColour);
        mGC.setFill(mEdgeLineColour);
        mGC.strokeArc(edge.mArcCenterX - edge.mArcRadius, edge.mArcCenterY
                - edge.mArcRadius, edge.mArcRadius * 2, edge.mArcRadius * 2,
                edge.mArcStartAngle, edge.mArcExtent, ArcType.OPEN);

        GraphUtils.fillArrowHead(mGC, edge.mArrowBaseX, edge.mArrowBaseY,
                edge.mArrowTipX, edge.mArrowTipY, ARROW_WIDTH);
    }

    private void drawEdge(GraphEdge edge) {
        if (!edge.mIsRendered) {
            return;
        }

        if (edge.mIsLine) {
            drawEdgeLine(edge);
        } else {
            drawEdgeArc(edge);
        }
    }

    private void drawEdgesLooped(GraphNode n, ArrayList<GraphEdge> edges) {
        if (edges == null || edges.isEmpty()) {
            return;
        }

        mGC.setStroke(mEdgeLineColour);
        mGC.strokeLine(n.mStartLineX1, n.mStartLineY1, n.mStartLineX2,
                n.mStartLineY2);
        mGC.strokeLine(n.mEndLineX1, n.mEndLineY1, n.mEndLineX2, n.mEndLineY2);

        for (GraphEdge edge : edges) {
            mGC.strokeArc(n.mX - edge.mArcRadius, n.mY - edge.mArcRadius,
                    edge.mArcRadius * 2, edge.mArcRadius * 2, n.mStartAngle,
                    n.mArcExtent, ArcType.OPEN);
        }

        mGC.setFill(mEdgeLabelColour);
        mGC.setFontSmoothingType(FontSmoothingType.LCD);
        mGC.setFont(mLabelFont);
        mGC.setTextAlign(TextAlignment.CENTER);
        mGC.setTextBaseline(VPos.CENTER);

        double loopDirNormalVecX = -n.mLoopDirVecY;
        double loopDirNormalVecY = n.mLoopDirVecX;

        for (GraphEdge edge : edges) {
            GraphUtils.setGcRotation(mGC, -edge.mTextAngle, edge.mTextX,
                    edge.mTextY);
            mGC.fillText(edge.mText, edge.mTextX, edge.mTextY);
            updateEdgeLabelHitTestData(edge, edge.mTextX, edge.mTextY,
                    loopDirNormalVecX, loopDirNormalVecY, n.mLoopDirVecX,
                    n.mLoopDirVecY);
        }

        mGC.setTransform(new Affine());
    }

    void drawNode(GraphNode n) {
        mGC.setFill(mNodeBackgroundColour);
        mGC.setStroke(mNodeBorderColour);

        GraphUtils.fillCircleCentred(mGC, n.mX, n.mY, n.mRadius);
        GraphUtils.strokeCircleCentred(mGC, n.mX, n.mY, n.mRadius);

        if (n.mUseFinalStateStyle) {
            GraphUtils.strokeCircleCentred(mGC, n.mX, n.mY, n.mRadius
                    - FINAL_STATE_BORDER_GAP);
        }

        if (n.mUseStartStateStyle) {
            double startX = n.mX - n.mRadius - ARROW_LENGTH
                    - INITIAL_STATE_LINE_LENGTH;
            double arrowBaseX = n.mX - n.mRadius - ARROW_LENGTH;
            double arrowTipX = n.mX - n.mRadius;

            mGC.setFill(mEdgeLabelColour);
            mGC.setStroke(mEdgeLabelColour);
            mGC.strokeLine(startX, n.mY, arrowBaseX, n.mY);
            GraphUtils.fillArrowHead(mGC, arrowBaseX, n.mY, arrowTipX, n.mY,
                    ARROW_WIDTH);
        }

        mGC.setFill(mNodeTextColour);
        mGC.setFontSmoothingType(FontSmoothingType.LCD);
        mGC.setFont(mNodeFont);
        mGC.setTextAlign(TextAlignment.CENTER);
        mGC.setTextBaseline(VPos.CENTER);
        mGC.fillText(Integer.toString(n.mId), n.mX, n.mY);
    }

    private void updateEdgeLabelHitTestData(GraphEdge edge, double textX,
            double textY, double textDirVecX, double textDirVecY,
            double textNormalVecX, double textNormalVecY) {
        if (edge.mTextWidth < 0.0) {
            // http://stackoverflow.com/a/13020490
            Text text = new Text(edge.mText);
            text.setFont(mLabelFont);
            new Scene(new Group(text));

            text.applyCss();

            Bounds bounds = text.getLayoutBounds();
            edge.mTextWidth = bounds.getWidth();
            edge.mTextHeight = bounds.getHeight();

        }

        edge.mLabelTestX1 = textX + textDirVecX * edge.mTextWidth * 0.5
                - textNormalVecX * edge.mTextHeight * 0.5;
        edge.mLabelTestY1 = textY + textDirVecY * edge.mTextWidth * 0.5
                - textNormalVecY * edge.mTextHeight * 0.5;

        edge.mLabelTestX2 = textX - textDirVecX * edge.mTextWidth * 0.5
                + textNormalVecX * edge.mTextHeight * 0.5;
        edge.mLabelTestY2 = textY - textDirVecY * edge.mTextWidth * 0.5
                + textNormalVecY * edge.mTextHeight * 0.5;

        /*
         * mGC.setFill(Color.BLUE); mGC.setStroke(Color.BLUE);
         * mGC.fillOval(edge.mLabelTestX1 - 2.5, edge.mLabelTestY1 - 2.5, 5, 5);
         * mGC.strokeLine(edge.mLabelTestX1, edge.mLabelTestY1,
         * edge.mLabelTestX1 + edge.mLabelTestBisectX * 50, edge.mLabelTestY1 +
         * edge.mLabelTestBisectY * 50); mGC.setStroke(Color.BLACK);
         * 
         * mGC.setFill(Color.GREEN); mGC.fillOval(edge.mLabelTestX2 - 2.5,
         * edge.mLabelTestY2 - 2.5, 5, 5);
         */

        edge.mLabelTestBisectX = -textDirVecX + textNormalVecX;
        edge.mLabelTestBisectY = -textDirVecY + textNormalVecY;

        double invVecLength = 1 / GraphUtils.vecLength(edge.mLabelTestBisectX,
                edge.mLabelTestBisectY);
        edge.mLabelTestBisectX *= invVecLength;
        edge.mLabelTestBisectY *= invVecLength;
    }

    private void onMouseDragged(MouseEvent event) {
        if (mDragNode != null) {
            double newX = (event.getX() - mDownX) + mDragOrigX;
            double newY = (event.getY() - mDownY) + mDragOrigY;

            // Prevent user dragging the node outside the canvas, preventing
            // them from ever dragging it again
            newX = Math.min(getWidth() - mDragNode.mRadius, newX);
            newX = Math.max(mDragNode.mRadius, newX);
            newY = Math.min(getHeight() - mDragNode.mRadius, newY);
            newY = Math.max(mDragNode.mRadius, newY);

            mDragNode.mX = newX;
            mDragNode.mY = newY;
        } else if (mDragEdge != null && mDragEdge.mFrom == mDragEdge.mTo) {
            // Trying to drag a looped edge, update loop direction vector
            System.out.println("Dragging edge");
            GraphNode n = mDragEdge.mFrom;
            n.mLoopDirVecX = event.getX() - n.mX;
            n.mLoopDirVecY = event.getY() - n.mY;
            double invVecLength = 1 / GraphUtils.vecLength(n.mLoopDirVecX,
                    n.mLoopDirVecY);
            n.mLoopDirVecX *= invVecLength;
            n.mLoopDirVecY *= invVecLength;
        }

        // TODO: be smarter about this (do minimum amount of layout
        // recalculation necessary)
        updateAllLayoutData();
        doRedraw();
    }

    private void onMousePressed(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY) {
            // Left-click to drag
            return;
        }
        
        this.requestFocus();

        mDownX = event.getX();
        mDownY = event.getY();

        // Check for a hit on a node first, they take preference
        GraphNode n = findNodeHit(mDownX, mDownY);
        GraphEdge e = findEdgeLabelHit(mDownX, mDownY);
        if (n != null) {
            mDragNode = n;
            mDragOrigX = n.mX;
            mDragOrigY = n.mY;
            if (mNodeClickedHandler != null) {
                mNodeClickedHandler
                        .handle(new GraphCanvasEvent(event, n, null));
            }
            // System.out.println("onMousePressed, hit NODE " + n);
        } else if (e != null) {
            mDragEdge = e;
            System.out.println("onMousePressed, hit EDGE " + e
                    + " label text = \"" + e.mText + "\"");
            if (mEdgeClickedHandler != null) {
                mEdgeClickedHandler
                        .handle(new GraphCanvasEvent(event, null, e));
            }
        } else {
            // System.out.println("onMousePressed, X = " + event.getX() +
            // ", Y = " + event.getY());
            if (mBackgroundClickedHandler != null) {
                mNodeClickedHandler.handle(new GraphCanvasEvent(event, null,
                        null));
            }
        }

        // TODO: be smarter about this (do minimum amount of layout
        // recalculation necessary)
        updateAllLayoutData();
        doRedraw();
    }

    private void onMouseReleased(MouseEvent event) {
        System.out.println("onMouseUp, X = " + event.getX() + ", Y = "
                + event.getY());
        mDragNode = null;
        mDragEdge = null;

        // TODO: be smarter about this (do minimum amount of layout
        // recalculation necessary)
        updateAllLayoutData();
        doRedraw();
    }

    private void onMouseClicked(MouseEvent event) {
        System.out.println("onMouseClicked, X = " + event.getX() + ", Y = "
                + event.getY() + ", clickCount = " + event.getClickCount());
    }

    private GraphNode findNodeHit(double x, double y) {
        for (NodeEdgePair pair : mGraph.values()) {
            if (nodeHitTest(pair.mNode, x, y)) {
                return pair.mNode;
            }
        }
        return null;
    }

    private GraphEdge findEdgeLabelHit(double x, double y) {
        for (NodeEdgePair pair : mGraph.values()) {
            for (GraphEdge e : pair.mLoopedEdges) {
                if (edgeLabelHitTest(e, mDownX, mDownY)) {
                    return e;
                }
            }
            for (GraphEdge e : pair.mEdges) {
                if (edgeLabelHitTest(e, mDownX, mDownY)) {
                    return e;
                }
            }
        }
        return null;
    }

    private boolean nodeHitTest(GraphNode n, double x, double y) {
        double distSqr = (x - n.mX) * (x - n.mX) + (y - n.mY) * (y - n.mY);
        if (distSqr <= n.mRadius * n.mRadius) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Cosine of 45 degrees, used by edgeLabelHitTest
     */
    private static final double COS_45_DEG = Math.sqrt(2) * 0.5;

    private boolean edgeLabelHitTest(GraphEdge e, double x, double y) {
        if (!e.mIsRendered) {
            // Can't hit an edge we aren't rendering
            return false;
        }

        double x1 = x - e.mLabelTestX1;
        double y1 = y - e.mLabelTestY1;
        double cosAlpha = x1 * e.mLabelTestBisectX + y1 * e.mLabelTestBisectY;
        cosAlpha /= GraphUtils.vecLength(x1, y1); // Math.sqrt(x1 * x1 + y1 *
                                                  // y1);
        if (cosAlpha < COS_45_DEG) {
            return false;
        }

        x1 = x - e.mLabelTestX2;
        y1 = y - e.mLabelTestY2;
        // Negative since we want mLabelTestBisect{X,Y} vector to be in the
        // opposite direction
        cosAlpha = -(x1 * e.mLabelTestBisectX + y1 * e.mLabelTestBisectY);
        cosAlpha /= GraphUtils.vecLength(x1, y1); // Math.sqrt(x1 * x1 + y1 *
                                                  // y1);
        if (cosAlpha < COS_45_DEG) {
            return false;
        }

        return true;
    }

    public void doRedraw() {
        mGC.clearRect(0, 0, getWidth(), getHeight());
        mGC.setFill(Color.WHITE);
        mGC.setStroke(Color.BLACK);

        // TODO: document: remember, drawing coordinates has the origin in the
        // top left corner, with y increasing going downward, the maths in
        // places accounts for this (where it matters)

        // Draw nodes first
        for (NodeEdgePair pair : mGraph.values()) {
            drawNode(pair.mNode);
        }

        // Draw looped edges
        for (NodeEdgePair pair : mGraph.values()) {
            drawEdgesLooped(pair.mNode, pair.mLoopedEdges);
        }

        // Draw edges
        for (NodeEdgePair pair : mGraph.values()) {
            for (GraphEdge e : pair.mEdges) {
                drawEdge(e);
            }
        }
    }

    // Overrides required for automatic resizing
    // See stackoverflow: http://stackoverflow.com/a/34263646

    @Override
    public double minHeight(double width) {
        return 0;
    }

    @Override
    public double maxHeight(double width) {
        return Double.MAX_VALUE;
    }

    @Override
    public double minWidth(double height) {
        return 0;
    }

    @Override
    public double maxWidth(double height) {
        return Double.MAX_VALUE;
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public void resize(double width, double height) {
        super.setWidth(width);
        super.setHeight(height);
        doRedraw();
    }
}
