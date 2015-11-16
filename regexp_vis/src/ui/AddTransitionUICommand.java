package ui;

/**
 * Command to add a transition to an automaton
 * 
 * @author sp611
 */
public class AddTransitionUICommand extends UICommand {

	private final GraphTransition transition;

	public AddTransitionUICommand(Graph graph, GraphTransition transition) {
		super(graph);
		this.transition = transition;
	}

	public GraphTransition getTransition() {
		return transition;
	}

	public void redo() {
		graph.addTransition(transition);
	}

	public void undo() {
		graph.removeTransition(transition);
	}

}
