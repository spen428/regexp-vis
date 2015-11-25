package ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import model.Automaton;
import model.AutomatonState;
import model.AutomatonTransition;

import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxPerimeter;
import com.mxgraph.view.mxStylesheet;

/**
 * Represents an {@link Automaton} using the JGraph graphing libraries.
 * <p>
 * Extends {@link mxGraph}, overriding the default stylesheet.
 *
 * @author sp611
 *
 */
public class Graph extends mxGraph {

    // FIELDS //
    private static final int VERTEX_DIAMETER_PX = 50;
    /**
     * A string representing the {@link mxGraph} style for start states.
     */
    private static final String START_STATE_STYLE = ""; // TODO: Unimplemented
    /**
     * A string representing the {@link mxGraph} style for final states.
     */
    private static final String FINAL_STATE_STYLE = ""; // TODO: Unimplemented
    /**
     * Maps the {@link mxCell} states to the {@link AutomatonState} states.
     */
    private final Map<AutomatonState, mxCell> states;
    /**
     * Maps the {@link mxCell} transitions to the {@link AutomatonTransition}
     * transitions.
     */
    private final Map<AutomatonTransition, mxCell> transitions;
    /**
     * The starting state.
     */
    private AutomatonState startState;
    /**
     * The underlying {@link Automaton} that this graph represents.
     */
    private final Automaton automaton;

    // CONSTRUCTORS //
    public Graph() {
        super();
        this.automaton = new Automaton();
        this.states = new HashMap<>();
        this.transitions = new HashMap<>();
    }

    public Graph(Automaton automaton) {
        super();
        this.automaton = automaton;
        this.states = new HashMap<>();
        this.transitions = new HashMap<>();
        addAutomaton(automaton);
    }

    // GETTERS //
    /**
     * The size of this graph is defined as the total number of states and
     * transitions it has.
     *
     * @return size
     */
    public int getSize() {
        return states.size() + transitions.size();
    }

    public Automaton getAutomaton() {
        return automaton;
    }

    public int getNumStateTransitions(AutomatonState state) {
        mxCell cell = states.get(state);
        return model.getEdgeCount(cell);
    }

    public AutomatonState getStartState() {
        return startState;
    }

    /**
     * @return Returns a <b>clone</b> of the {@link AutomatonState} to
     *         {@link mxCell} HashMap that this graph uses. The graph cannot be
     *         modified by manipulating the returned Map.
     */
    public HashMap<AutomatonState, mxCell> getStates() {
        return new HashMap<AutomatonState, mxCell>(states);
    }

    /**
     * @return Returns a <b>clone</b> of the {@link AutomatonTransition} to
     *         {@link mxCell} HashMap that this graph uses. The graph cannot be
     *         modified by manipulating the returned Map.
     */
    public HashMap<AutomatonTransition, mxCell> getTransitions() {
        return new HashMap<AutomatonTransition, mxCell>(transitions);
    }

    /**
     * Generates a stylesheet defining the default visual appearance of the
     * graph.
     */
    @Override
    protected mxStylesheet createStylesheet() {
        // TODO: Load from external file
        Map<String, Object> edgeStyle = new HashMap<String, Object>();
        /* The curve shape seems to mess up the arrow heads. */
        // edgeStyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CURVE);
        edgeStyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CONNECTOR);
        edgeStyle.put(mxConstants.STYLE_STROKECOLOR, "#000000");
        edgeStyle.put(mxConstants.STYLE_FONTSTYLE, mxConstants.FONT_BOLD);
        edgeStyle.put(mxConstants.STYLE_FONTSIZE, "12");
        edgeStyle.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);
        edgeStyle.put(mxConstants.STYLE_VERTICAL_ALIGN,
                mxConstants.ALIGN_MIDDLE);
        edgeStyle.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC);

        Map<String, Object> vertexStyle = new HashMap<String, Object>();
        vertexStyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
        vertexStyle.put(mxConstants.STYLE_STROKECOLOR, "#000000");
        vertexStyle.put(mxConstants.STYLE_FILLCOLOR, "#ffffff");
        vertexStyle.put(mxConstants.STYLE_FONTSIZE, "14");
        vertexStyle.put(mxConstants.STYLE_PERIMETER,
                mxPerimeter.EllipsePerimeter);
        vertexStyle.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);
        vertexStyle.put(mxConstants.STYLE_VERTICAL_ALIGN,
                mxConstants.ALIGN_MIDDLE);

        mxStylesheet styleSheet = new mxStylesheet();
        styleSheet.setDefaultEdgeStyle(edgeStyle);
        styleSheet.setDefaultVertexStyle(vertexStyle);
        return styleSheet;
    }

    // STATE MANIPULATION //
    /**
     * Sets the given state to be the start state. If a start state already
     * exists, that state will become a non-start state.
     *
     * @param state
     *            the state to set as the start state
     * @return the {@link mxCell} representing the state
     */
    public mxCell setStartState(AutomatonState state) {
        if (startState != null) {
            /* Must replace the old entry with the new */
            mxCell startCell = states.remove(startState);
            startCell = setStartState(startCell, false);
            states.put(startState, startCell);
        }
        if (state != null) {
            mxCell cell = states.remove(state);
            if (cell == null) {
                /* State doesn't exist yet, let's create it */
                cell = addState(state);
            }
            /* Must replace the old entry with the new */
            cell = setStartState(cell, true);
            states.put(state, cell);
            startState = state;
            return cell;
        } else {
            startState = null;
            return null;
        }
    }

    private mxCell setStartState(mxCell cell, boolean isStart) {
        model.beginUpdate();
        try {
            // TODO
        } finally {
            model.endUpdate();
        }
        return cell;
    }

    /**
     * Sets the finality of the given state
     *
     * @param state
     *            the state
     * @param isFinal
     *            finality to set
     * @return the {@link mxCell} representing the state
     */
    public mxCell setFinal(AutomatonState state, boolean isFinal) {
        mxCell cell = states.remove(state);
        if (cell != null) {
            /* Must replace the old entry with the new */
            cell = setFinal(cell, isFinal);
            state.setFinal(isFinal);
            states.put(state, cell);
        }
        return null;
    }

    private mxCell setFinal(mxCell cell, boolean isFinal) {
        model.beginUpdate();
        try {
            // TODO
        } finally {
            model.endUpdate();
        }
        return cell;
    }

    /**
     * Adds an {@link AutomatonState} to the graph if it does not already exist.
     *
     * @param state
     *            the {@link AutomatonState} to add
     * @return the {@link mxCell} that was added to the graph, or {@code null}
     *         if nothing was added
     * @throws IllegalArgumentException
     *             if the <b>state</b> parameter is {@code null}.
     */
    public mxCell addState(AutomatonState state) {
        if (state == null) {
            throw new IllegalArgumentException("state cannot be null when"
                    + " inserting an AutomatonState");
        } else if (states.get(state) != null) {
            return null;
        }

        String id = "state" + state.getId();
        String style = state.isFinal() ? FINAL_STATE_STYLE : null;

        mxCell cell = null;
        model.beginUpdate();
        try {
            cell = (mxCell) insertVertex(getDefaultParent(), id, id, 0, 0,
                    VERTEX_DIAMETER_PX, VERTEX_DIAMETER_PX, style);
            states.put(state, cell);
        } finally {
            model.endUpdate();
        }
        return cell;
    }

    public boolean addStateWithTransitions(AutomatonState state,
            LinkedList<AutomatonTransition> transitions) {
        if (addState(state) == null) {
            /* addState() failed so do not continue. */
            return false;
        }

        if (transitions != null) {
            for (AutomatonTransition t : transitions) {
                addTransition(t);
            }
        }

        return true;
    }

    /**
     * Removes the given state and any transitions associated with the state.
     * <p>
     * If you do not wish to remove the transitions, use
     * {@link #removeState(AutomatonState, boolean)}.
     *
     * @param state
     *            the state to remove
     * @param removeTransitions
     *            whether to remove transitions that are connected to this state
     * @return an array of the cells that were removed
     */
    public mxCell[] removeState(AutomatonState state) {
        return removeState(state, true);
    }

    /**
     * Removes the given state and optionally any transitions associated with
     * the state.
     *
     * @param state
     *            the state to remove
     * @param removeTransitions
     *            whether to remove transitions that are connected to this state
     * @return an array of the cells that were removed
     */
    public mxCell[] removeState(AutomatonState state, boolean removeTransitions) {
        /* Find and remove from maps */
        ArrayList<mxCell> removed = new ArrayList<mxCell>();
        if (removeTransitions) {
            for (AutomatonTransition t : new ArrayList<>(transitions.keySet())) {
                if (t.getFrom() == state || t.getTo() == state) {
                    removed.add(transitions.remove(t));
                }
            }
        }
        removed.add(states.remove(state));

        /* Remove from model */
        model.beginUpdate();
        try {
            for (mxCell cell : removed) {
                model.remove(cell);
            }
        } finally {
            model.endUpdate();
        }

        return removed.toArray(new mxCell[] {});
    }

    // TRANSITION MANIPULATION //
    /**
     * Insert the given {@link AutomatonTransition} into the graph by calling
     * {@link #insertEdge(Object, String, Object, Object, Object)}
     *
     * @param transition
     *            the {@link AutomatonTransition} to insert
     * @return the {@link mxCell} that was added to the graph, or {@code null}
     *         if nothing was added
     * @throws IllegalArgumentException
     *             if any parameters are {@code null}.
     * @see {@link #addAutomatonState(AutomatonState)}
     */
    public mxCell addTransition(AutomatonTransition transition) {
        if (transition == null) {
            throw new IllegalArgumentException("transition cannot be null when"
                    + " inserting an AutomatonTransition");
        }

        mxCell from = states.get(transition.getFrom());
        mxCell to = states.get(transition.getTo());
        if (from == null || to == null) {
            throw new RuntimeException("Cannot add a transition to the graph"
                    + " before adding its associated states.");
        }

        String id = "transition" + transition.getId();
        mxCell cell = null;

        model.beginUpdate();
        try {
            cell = (mxCell) insertEdge(getDefaultParent(), id, id, from, to);
            transitions.put(transition, cell);
        } finally {
            model.endUpdate();
        }
        return cell;
    }

    /**
     * Removes a transition from the graph.
     *
     * @param transition
     *            the {@link AutomatonTransition} to remove
     * @return the cell that was removed
     */
    public mxCell removeTransition(AutomatonTransition transition) {
        mxCell cell = null;
        model.beginUpdate();
        try {
            cell = transitions.remove(transition);
            model.remove(cell);
        } finally {
            model.endUpdate();
        }
        return cell;
    }

    // UTILITY //

    public boolean containsState(AutomatonState state) {
        return states.containsKey(state);
    }

    public boolean containsTransition(AutomatonTransition transition) {
        return transitions.containsKey(transition);
    }

    /**
     * Removes all nodes and edges from this graph.
     */
    public void clear() {
        model.beginUpdate();
        try {
            for (mxCell c : states.values()) {
                model.remove(c);
            }
            for (mxCell c : transitions.values()) {
                model.remove(c);
            }
            states.clear();
            transitions.clear();
            startState = null;
        } finally {
            model.endUpdate();
        }
    }

    /**
     * Adds all of the states and transitions from the given {@link Automaton}
     * to the graph.
     *
     * @param automaton
     *            the automaton
     */
    private void addAutomaton(Automaton automaton) {
        // FIXME(mjn33): Need to implement iterator in Automaton / something
        // to prevent modifying internal state

        /*
        HashMap<AutomatonState, LinkedList<AutomatonTransition>> mGraph = automaton
                .getGraph();
        AutomatonState startState = automaton.getStartState();
        // Insert states
        setStartState(startState);
        for (AutomatonState state : mGraph.keySet()) {
            addState(state);
        }
        // Insert transitions
        for (AutomatonState state : mGraph.keySet()) {
            for (AutomatonTransition transition : mGraph.get(state)) {
                addTransition(transition);
            }
        }
        */
    }

}
