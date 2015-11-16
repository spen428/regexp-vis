package ui;


/**
 * Command to add a state to an automaton
 * 
 * @author sp611
 */
public class AddStateUICommand extends UICommand {

	private final GraphState state;

	public AddStateUICommand(Graph graph, GraphState state) {
		super(graph);
		this.state = state;
	}

	public GraphState getState() {
		return state;
	}

	public void redo() {
		graph.addState(state);
	}

	public void undo() {
		graph.removeState(state, false);
	}
	
}
