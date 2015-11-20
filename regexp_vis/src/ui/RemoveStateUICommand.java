package ui;

import model.RemoveStateCommand;

/**
 * Command to remove a state and its outgoing transitions from an automaton
 * 
 * @author sp611
 */
public class RemoveStateUICommand extends UICommand {

    private final RemoveStateCommand ccmd;

    public RemoveStateUICommand(Graph graph, RemoveStateCommand cmd) {
        super(graph, cmd);
        this.ccmd = cmd;
    }

    public void redo() {
        graph.removeState(ccmd.getState());
        ccmd.redo();
    }

    public void undo() {
        graph.addStateWithTransitions(ccmd.getState(), ccmd.getTransitions());
        ccmd.undo();
    }

}
