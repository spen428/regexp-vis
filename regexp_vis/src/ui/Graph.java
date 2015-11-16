package ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import model.Automaton;
import model.AutomatonState;
import model.AutomatonTransition;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxICell;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxPerimeter;
import com.mxgraph.view.mxStylesheet;

/**
 * Extends {@link mxGraph}, overriding the default stylesheet.
 * 
 * @author sp611
 * 
 */
public class Graph extends mxGraph {

	private static final int VERTEX_DIAMETER_PX = 50;

	/**
	 * A string representing the {@link mxGraph} style for final states.
	 */
	private static final String FINAL_STATE_STYLE = null; // TODO: Unimplemented

	private final HashMap<mxICell, GraphState> states;
	private final HashMap<mxICell, GraphTransition> transitions;

	private GraphState startState;

	public Graph() {
		super();
		this.states = new HashMap<>();
		this.transitions = new HashMap<>();
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

	/**
	 * Removes the given node and any edges connected to the node. If you do not
	 * wish to remove the edges, use {{@link #removeState(GraphState, boolean)}.
	 * 
	 * @param node
	 *            the node to remove
	 * @return an array of the edges that were removed
	 */
	public GraphTransition[] removeState(GraphState state) {
		return removeState(state, true);
	}

	/**
	 * Removes the given node, and optionally any edges associated with the
	 * node.
	 * 
	 * @param state
	 *            the node to remove
	 * @param removeEdges
	 *            whether to remove edges that are connected to this node
	 * @return an array of the edges that were removed
	 */
	public GraphTransition[] removeState(GraphState state, boolean removeEdges) {
		mxICell cell = state.getState();
		ArrayList<GraphTransition> removed = new ArrayList<GraphTransition>();
		model.beginUpdate();
		try {
			if (removeEdges) {
				int numEdges = model.getEdgeCount(state);
				for (int i = 0; i < numEdges; i++) {
					mxICell edge = cell.getEdgeAt(i);
					cell.remove(edge);
					removed.add((GraphTransition) transitions.remove(edge));
				}
			}
			model.remove(state);
		} finally {
			model.endUpdate();
		}
		return (GraphTransition[]) removed.toArray();
	}

	/**
	 * Removes all edges between the two given nodes.
	 * 
	 * @param node1
	 *            The starting node
	 * @param node2
	 *            The ending node
	 * @return the edges that were removed
	 */
	public Object[] removeEdgesBetween(mxICell node1, mxICell node2) {
		Object[] edges = getEdgesBetween(node1, node2);
		model.beginUpdate();
		try {
			for (Object edge : edges) {
				model.remove(edge);
			}
		} finally {
			model.endUpdate();
		}
		return edges;
	}

	/**
	 * Adds the states and transitions from the given automaton to the graph.
	 * 
	 * @param automaton
	 *            the {@link Automaton}
	 */
	void addAutomaton(Automaton automaton) {
		HashMap<AutomatonState, LinkedList<AutomatonTransition>> mGraph = automaton
				.getGraph();
		HashMap<AutomatonState, Object> stateMapping = new HashMap<>();
		ArrayList<AutomatonTransition> transitions = new ArrayList<>();
		AutomatonState mStartState = automaton.getStartState();

		getModel().beginUpdate();
		try {
			/* Insert states */
			Object ss = insertAutomatonState(mStartState);
			stateMapping.put(mStartState, ss);
			for (AutomatonState state : mGraph.keySet()) {
				Object obj = insertAutomatonState(state);
				stateMapping.put(state, obj);
			}

			/* Insert transitions */
			for (AutomatonState state : mGraph.keySet()) {
				for (AutomatonTransition transition : mGraph.get(state)) {
					if (!transitions.contains(transition)) {
						Object fromVertex = stateMapping.get(transition
								.getFrom());
						Object toVertex = stateMapping.get(transition.getTo());
						insertAutomatonTransition(transition, fromVertex,
								toVertex);
						transitions.add(transition);
					}
				}
			}
		} finally {
			getModel().endUpdate();
		}
	}

	/**
	 * Insert the given {@link AutomatonTransition} into the graph by calling
	 * {@link #insertEdge(Object, String, Object, Object, Object)}
	 * 
	 * @param transition
	 *            the {@link AutomatonTransition} to insert
	 * @param fromVertex
	 *            the Object representing the vertex of the state the transition
	 *            is coming from, as returned by
	 *            {@code insertAutomatonState(AutomatonState)}
	 * @param toVertex
	 *            the Object representing the vertex of the state the transition
	 *            is going to, as returned by
	 *            {@code insertAutomatonState(AutomatonState)}
	 * @return
	 * @throws IllegalArgumentException
	 *             if any parameters are {@code null}.
	 * @see {@link #insertAutomatonState(AutomatonState)} *
	 */
	Object insertAutomatonTransition(AutomatonTransition transition,
			Object fromVertex, Object toVertex) throws IllegalArgumentException {
		if (transition == null) {
			throw new IllegalArgumentException("transition cannot be null when"
					+ " inserting an AutomatonTransition");
		} else if (fromVertex == null) {
			throw new IllegalArgumentException("fromVertex cannot be null when"
					+ " inserting an AutomatonTransition");
		} else if (toVertex == null) {
			throw new IllegalArgumentException("toVertex cannot be null when"
					+ " inserting an AutomatonTransition");
		}
		// String id = "transition" + transition.getId();
		String id = null; // TODO: Swap if AutomatonTransition.getId()
							// implemented.
		return insertEdge(getDefaultParent(), id, transition.toString(),
				fromVertex, toVertex);
	}

	/**
	 * Insert the given {@link AutomatonState} into the graph by calling
	 * {@link #insertVertex(Object, String, Object, double, double, double, double)}
	 * 
	 * @param state
	 *            the {@link AutomatonState} to insert
	 * @return Returns the Object representing the vertex that was added to the
	 *         graph.
	 * @throws IllegalArgumentException
	 *             if the <b>state</b> parameter is {@code null}.
	 */
	Object insertAutomatonState(AutomatonState state)
			throws IllegalArgumentException {
		if (state == null) {
			throw new IllegalArgumentException("state cannot be null when"
					+ " inserting an AutomatonState");
		}
		return insertVertex(getDefaultParent(), "state" + state.getId(),
				state.toString(), 0, 0, VERTEX_DIAMETER_PX, VERTEX_DIAMETER_PX,
				state.isFinal() ? FINAL_STATE_STYLE : null);
	}

	/**
	 * Removes all vertices and edges from this graph.
	 */
	public void clear() {
		getModel().beginUpdate();
		try {
			// TODO: Unimplemented
		} finally {
			getModel().endUpdate();
		}
	}

	/**
	 * Returns true if the given state is a final state.
	 * 
	 * @param state
	 *            the state
	 * @return true if final
	 */
	public boolean isFinalState(mxICell state) {
		GraphState graphState = states.get(state);
		if (graphState != null) {
			return graphState.isFinal();
		}
		return false;
	}

	/**
	 * Sets the finality of the given state
	 * 
	 * @param state
	 *            the state
	 * @param isFinal
	 *            finality to set
	 */
	public void setFinal(mxICell state, boolean isFinal) {
		// TODO: Set final in model
		GraphState graphState = states.get(state);
		if (graphState != null) {
			graphState.setFinal(isFinal);
		}
	}

	public void removeTransition(GraphTransition transition) {
		// TODO Remove from model
		transitions.remove(transition.getTransition());
	}

	public void addTransition(GraphTransition transition) {
		mxICell cell = transition.getTransition();
		mxICell from = transition.getFrom().getState();
		mxICell to = transition.getTo().getState();
		insertEdge(getDefaultParent(), cell.getId(), cell.getValue(), from, to);
		transitions.put(cell, transition);
	}

	/**
	 * Sets the given state to be the start state. If a start state already
	 * exists, that state will become a non-start state.
	 * 
	 * @param state
	 *            the state to set as the start state.
	 */
	public void setStartState(GraphState state) {
		if (startState != null) {
			startState.setStart(false);
		}
		startState = state;
		startState.setStart(true);
	}

	// TODO: Not keeping track of start state history.
	public void addState(GraphState state) {
		mxICell cell = state.getState();
		mxGeometry g = cell.getGeometry();
		if (g == null) {
			g = new mxGeometry(0, 0, VERTEX_DIAMETER_PX, VERTEX_DIAMETER_PX);
		}

		insertVertex(getDefaultParent(), cell.getId(), cell.getValue(),
				g.getX(), g.getY(), g.getWidth(), g.getHeight(),
				cell.getStyle(), g.isRelative());
		states.put(cell, state);
	}

	public void addStateWithTransitions(GraphState state,
			GraphTransition[] transitions) {
		addState(state);
		if (transitions != null) {
			for (GraphTransition t : transitions) {
				addTransition(t);
			}
		}
	}

	public boolean containsState(GraphState state) {
		return states.containsValue(state);
	}

	public boolean containsState(mxICell state) {
		return states.containsKey(state);
	}

	public boolean containsTransition(GraphTransition transition) {
		return transitions.containsValue(transition);
	}

	public boolean containsTransition(mxICell transition) {
		return transitions.containsKey(transition);
	}

	/**
	 * Determine which {@link GraphTransition} are connected to a given
	 * {@link GraphState}
	 * 
	 * @param state
	 *            the {@link GraphState}
	 * @return an {@link ArrayList} of {@link GraphTransition} that are
	 *         connected to the given {@link GraphState}
	 */
	public ArrayList<GraphTransition> getStateTransitions(GraphState state) {
		ArrayList<GraphTransition> list = new ArrayList<>();
		for (GraphTransition t : transitions.values()) {
			if (t.getFrom() == state || t.getTo() == state) {
				list.add(t);
			}
		}
		return list;
	}

	/**
	 * Creates a new {@link GraphTransition} and adds it to the {@link Graph}
	 * 
	 * @param from
	 *            the {@link GraphState} that this transition is coming from
	 * @param to
	 *            the {@link GraphState} that this transition is going to
	 * @param value
	 *            a value representing the transition
	 * @return the {@link GraphTransition} that was created
	 */
	public GraphTransition createNewTransition(GraphState from, GraphState to,
			String value) {
		mxICell transition = new mxCell(value);
		return new GraphTransition(from, to, transition);
	}

	/**
	 * Creates a new {@link GraphState} and adds it to the {@link Graph}
	 * 
	 * @param value
	 *            a value representing the state
	 * @return the {@link GraphState} that was created
	 */
	public GraphState createNewState(Object value) {
		mxICell cell = new mxCell(value);
		return new GraphState(cell);
	}

}
