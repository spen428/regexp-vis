package ui;

import model.AddTransitionCommand;

/**
 * Command to add a transition to an automaton
 * 
 * @author sp611
 */
public class AddTransitionUICommand extends UICommand {

    private final AddTransitionCommand ccmd;

    public AddTransitionUICommand(Graph graph, AddTransitionCommand cmd) {
        super(graph, cmd);
        this.ccmd = cmd;
    }

    public void redo() {
        graph.addTransition(ccmd.getTransition());
        cmd.redo();
    }

    public void undo() {
        graph.removeTransition(ccmd.getTransition());
        cmd.undo();
    }

}
