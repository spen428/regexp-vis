package view;

import java.util.LinkedList;

import javafx.event.Event;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Automaton;
import model.AutomatonState;
import model.AutomatonTransition;
import model.BasicRegexp;
import model.InvalidRegexpException;

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

    public void onEnteredRegexp(String text) {
        System.out.printf("Entered regexp: %s%n", text);
        BasicRegexp re = null;
        try {
            re = BasicRegexp.parseRegexp(text);
            // BasicRegexp.debugPrintBasicRegexp(0, re);
        } catch (InvalidRegexpException e1) {
            Alert alert = new Alert(AlertType.ERROR,
                    "Error: invalid regexp entered. Details: \n\n"
                            + e1.getMessage());
            alert.showAndWait();
            return;
        }

        this.canvas.removeAllNodes();
        this.automaton.clear();
        AutomatonState startState = this.automaton.getStartState();
        AutomatonState finalState = this.automaton.createNewState();
        AutomatonTransition trans = this.automaton
                .createNewTransition(startState, finalState, re);
        finalState.setFinal(true);
        this.automaton.addStateWithTransitions(finalState,
                new LinkedList<AutomatonTransition>());
        this.automaton.addTransition(trans);

        GraphNode startNode = this.canvas.addNode(startState.getId(), 50.0,
                50.0);
        this.canvas.setNodeUseStartStyle(startNode, true);
        GraphNode endNode = this.canvas.addNode(finalState.getId(),
                this.canvas.getWidth() - 50.0, this.canvas.getHeight() - 50.0);
        this.canvas.setNodeUseFinalStyle(endNode, true);
        GraphEdge edge = this.canvas.addEdge(trans.getId(), startNode, endNode,
                re.toString());
    }

    public abstract void processEvent(T event);

}
