package ui;

import com.mxgraph.model.mxICell;

/**
 * Command to change the finality of a state
 * 
 * @author sp611
 */
public class SetIsFinalUICommand extends UICommand {

	private final mxICell state;
	private final boolean differs;

	public SetIsFinalUICommand(Graph graph, mxICell state, boolean isFinal) {
		super(graph);
		this.state = state;
		differs = (graph.isFinalState(state) != isFinal);
	}

	public void redo() {
		if (differs) {
			graph.setFinal(state, !graph.isFinalState(state));
		}
	}

	public void undo() {
		if (differs) {
			graph.setFinal(state, !graph.isFinalState(state));
		}
	}

}
