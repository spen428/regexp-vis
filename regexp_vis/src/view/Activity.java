package view;

import javafx.event.Event;

/**
 * 
 * @author sp611
 *
 */
public interface Activity<T extends Event> {

    public void processEvent(T event);

}
