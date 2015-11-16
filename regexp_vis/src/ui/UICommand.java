package ui;

import model.Command;

/**
 * Extends {@link Command} to facilitate UI-side command history.
 * 
 * @author sp611
 *
 */
public abstract class UICommand extends Command {

	protected final Graph graph;
	
	public UICommand(Graph graph) {
		super(null);
		this.graph = graph;
	}

}
