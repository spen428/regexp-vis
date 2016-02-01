package controller;

import model.AddTransitionCommand;
import model.AutomatonTransition;
import view.GraphCanvasFX;
import view.GraphNode;

/**
 * Command to add a transition to an automaton
 *
 * @author sp611
 */
public class AddTransitionUICommand extends UICommand {

    private final AddTransitionCommand ccmd;

    public AddTransitionUICommand(GraphCanvasFX graph, AddTransitionCommand cmd) {
        super(graph, cmd);
        this.ccmd = cmd;
    }

    @Override
    public void redo() {
        AutomatonTransition t = ccmd.getTransition();
        GraphNode nodeFrom = graph.lookupNode(t.getFrom().getId());
        GraphNode nodeTo = graph.lookupNode(t.getTo().getId());
        graph.addEdge(t.getId(), nodeFrom, nodeTo, t.getData().toString());
        cmd.redo();
    }

    @Override
    public void undo() {
        graph.removeEdge(ccmd.getTransition().getId());
        cmd.undo();
    }

}
