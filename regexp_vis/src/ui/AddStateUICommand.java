package ui;

import model.AddStateCommand;

/**
 * Command to add a state to an automaton
 * 
 * @author sp611
 */
public class AddStateUICommand extends UICommand {

    private final AddStateCommand ccmd;

    public AddStateUICommand(Graph graph, AddStateCommand cmd) {
        super(graph, cmd);
        this.ccmd = cmd;
    }

    @Override
    public void redo() {
        graph.addState(ccmd.getState());
        ccmd.redo();
    }

    @Override
    public void undo() {
        graph.removeState(ccmd.getState(), false);
        ccmd.undo();
    }

}
