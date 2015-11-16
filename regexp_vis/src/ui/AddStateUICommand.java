package ui;

import com.mxgraph.model.mxICell;

/**
 * Command to add a state to an automaton
 * 
 * @author sp611
 */
public class AddStateUICommand extends UICommand {

	private final mxICell state;

	public AddStateUICommand(Graph graph, mxICell state) {
		super(graph);
		this.state = state;
	}

	public mxICell getState() {
		return state;
	}

	public void redo() {
		graph.addState(state);
	}

	public void undo() {
		graph.removeState(state, false);
	}
	
}
