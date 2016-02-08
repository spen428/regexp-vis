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

    public RemoveTransitionUICommand(GraphCanvasFX graph,
            RemoveTransitionCommand cmd) {
        super(graph, cmd);
        this.ccmd = cmd;
    }

    @Override
    public void redo() {
        this.graph.removeEdge(this.ccmd.getTransition().getId());
        this.cmd.redo();
    }

    @Override
    public void undo() {
        AutomatonTransition t = this.ccmd.getTransition();
        GraphNode nodeFrom = this.graph.lookupNode(t.getFrom().getId());
        GraphNode nodeTo = this.graph.lookupNode(t.getTo().getId());
        this.graph.addEdge(t.getId(), nodeFrom, nodeTo, t.getData().toString());
        this.cmd.undo();
    }

}
