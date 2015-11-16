package ui;

/**
 * Command to remove a transition from an automaton
 * 
 * @author sp611
 */
public class RemoveTransitionUICommand extends UICommand {

	private final GraphTransition transition;

	public RemoveTransitionUICommand(Graph graph, GraphTransition transition) {
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
