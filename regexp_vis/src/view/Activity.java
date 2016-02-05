package view;

import javafx.event.Event;
import model.Automaton;

/**
 * 
 * @author sp611
 *
 */
public abstract class Activity<T extends Event> {

    protected final GraphCanvasFX canvas;
    protected final Automaton automaton;

    public Activity(GraphCanvasFX canvas, Automaton automaton) {
        super();
        this.canvas = canvas;
        this.automaton = automaton;
    }

    public abstract void processEvent(T event);

}
