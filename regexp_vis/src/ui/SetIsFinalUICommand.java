package ui;

import com.mxgraph.model.mxICell;

/**
 * Command to change the finality of a state
 * 
 * @author sp611
 */
public class SetIsFinalUICommand extends UICommand {

	private final GraphState state;
	private final boolean differs;

	public SetIsFinalUICommand(Graph graph, GraphState state, boolean isFinal) {
		super(graph);
		this.state = state;
		differs = (graph.isFinalState(state.getState()) != isFinal);
	}

	public void redo() {
		if (differs) {
			mxICell cell = state.getState();
			graph.setFinal(cell, !graph.isFinalState(cell));
		}
	}

	public void undo() {
		redo();
	}

}
