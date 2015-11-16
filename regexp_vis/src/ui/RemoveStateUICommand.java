package ui;

import com.mxgraph.model.mxICell;

/**
 * Command to remove a state and its outgoing transitions from an automaton
 * 
 * @author sp611
 */
public class RemoveStateUICommand extends UICommand {

	private final mxICell state;
	private mxICell[] transitions;

	public RemoveStateUICommand(Graph graph, mxICell state) {
		super(graph);
		this.state = state;
	}

	public mxICell getState() {
		return state;
	}

	public void redo() {
		transitions = graph.removeState(state);
	}

	public void undo() {
		graph.addStateWithTransitions(state, transitions);
	}

}
