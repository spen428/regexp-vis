package ui;

import model.RemoveTransitionCommand;

/**
 * Command to remove a transition from an automaton
 * 
 * @author sp611
 */
public class RemoveTransitionUICommand extends UICommand {

    private final RemoveTransitionCommand ccmd;

    public RemoveTransitionUICommand(Graph graph, RemoveTransitionCommand cmd) {
        super(graph, cmd);
        this.ccmd = cmd;
    }

    public void redo() {
        graph.removeTransition(ccmd.getTransition());
        ccmd.redo();
    }

    public void undo() {
        graph.addTransition(ccmd.getTransition());
        ccmd.undo();
    }

}
