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

}
