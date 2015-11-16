package ui;

import model.Command;

/**
 * Extends {@link Command} to facilitate UI-side command history.
 * 
 * @author sp611
 *
 */
public class UICommand extends Command {

	private final Graph graph;
	
	public UICommand(Graph graph) {
		super(null);
		this.graph = graph;
	}

	public Graph getGraph() {
		return graph;
	}

	@Override
	public void undo() {
		// TODO Auto-generated method stub

	}

	@Override
	public void redo() {
		// TODO Auto-generated method stub

	}

}
