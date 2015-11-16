package ui;

import com.mxgraph.model.mxICell;

/**
 * Command to add a transition to an automaton
 * 
 * @author sp611
 */
public class AddTransitionUICommand extends UICommand {

	private final mxICell transition;

	public AddTransitionUICommand(Graph graph, mxICell transition) {
		super(graph);
		this.transition = transition;
	}

	public mxICell getTransition() {
		return transition;
	}

	public void redo() {
		graph.addTransition(transition);
	}

	public void undo() {
		graph.removeTransition(transition);
	}

}
