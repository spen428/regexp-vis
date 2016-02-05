package view;

import javafx.event.Event;

/**
 * 
 * @author sp611
 *
 */
public abstract class Activity<T extends Event> {

    protected final GraphCanvasFX canvas;

    public Activity(GraphCanvasFX canvas) {
        super();
        this.canvas = canvas;
    }

    public abstract void processEvent(T event);

}
