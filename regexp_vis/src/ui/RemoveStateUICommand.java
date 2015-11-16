package ui;

/**
 * Command to remove a state and its outgoing transitions from an automaton
 * 
 * @author sp611
 */
public class RemoveStateUICommand extends UICommand {

	private final GraphState state;
	private GraphTransition[] transitions;

	public RemoveStateUICommand(Graph graph, GraphState state) {
		super(graph);
		this.state = state;
	}

	public GraphState getState() {
		return state;
	}

	public void redo() {
		transitions = graph.removeState(state);
	}

	public void undo() {
		graph.addStateWithTransitions(state, transitions);
	}

}
