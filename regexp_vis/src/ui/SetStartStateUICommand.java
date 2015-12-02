package ui;

import model.AddStateCommand;
import model.AutomatonState;

/**
 * Command to set the start state of the graph
 * 
 * @author sp611
 */
public class SetStartStateUICommand extends UICommand {

    /*
     * TODO: This doesn't follow the same design pattern as the rest of the
     * UICommands as there is no corresponding SetStartStateCommand in the model
     * package.
     */

    private final AutomatonState state;
    private AutomatonState oldStartState;

    public SetStartStateUICommand(Graph graph, AutomatonState state) {
        super(graph, new AddStateCommand(null, state)); // Hack to prevent NPE
        this.state = state;
    }

    @Override
    public void redo() {
        oldStartState = graph.getStartState();
        graph.setStartState(state);
    }

    @Override
    public void undo() {
        graph.setStartState(oldStartState);
    }
}
