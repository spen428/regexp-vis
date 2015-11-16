package ui;

import com.mxgraph.model.mxICell;

/**
 * Command to remove a transition from an automaton
 * 
 * @author sp611
 */
public class RemoveTransitionUICommand extends UICommand {

	private final mxICell transition;

	public RemoveTransitionUICommand(Graph graph, mxICell transition) {
		super(graph);
		this.transition = transition;
	}

	public void redo() {
		graph.removeTransition(transition);
	}

	public void undo() {
		graph.addTransition(transition);
	}

}
