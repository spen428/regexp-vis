package controller;

import model.AutomatonTransition;
import model.RemoveTransitionCommand;
import view.GraphCanvasFX;
import view.GraphNode;

/**
 * Command to remove a transition from an automaton
 *
 * @author sp611
 */
public class RemoveTransitionUICommand extends UICommand {

    private final RemoveTransitionCommand ccmd;

    public RemoveTransitionUICommand(GraphCanvasFX graph, RemoveTransitionCommand cmd) {
        super(graph, cmd);
        this.ccmd = cmd;
    }

    @Override
    public void redo() {
        graph.removeEdge(ccmd.getTransition().getId());
        cmd.redo();
    }

    @Override
    public void undo() {
        AutomatonTransition t = ccmd.getTransition();
        GraphNode nodeFrom = graph.lookupNode(t.getFrom().getId());
        GraphNode nodeTo = graph.lookupNode(t.getTo().getId());
        graph.addEdge(t.getId(), nodeFrom, nodeTo, t.getData().toString());
        cmd.undo();
    }

}
