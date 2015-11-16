package ui;

import com.mxgraph.model.mxICell;

/**
 * 
 * @author sp611
 *
 */
public class GraphTransition {

	private final GraphState from, to;
	private final mxICell transition;

	public GraphTransition(GraphState from, GraphState to, mxICell transition) {
		super();
		this.from = from;
		this.to = to;
		this.transition = transition;
	}

	public GraphState getFrom() {
		return from;
	}

	public GraphState getTo() {
		return to;
	}

	public mxICell getTransition() {
		return transition;
	}

}
