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

    public AddTransitionUICommand(GraphCanvasFX graph,
            AddTransitionCommand cmd) {
        super(graph, cmd);
        this.ccmd = cmd;
    }

    @Override
    public void redo() {
        AutomatonTransition t = this.ccmd.getTransition();
        GraphNode nodeFrom = this.graph.lookupNode(t.getFrom().getId());
        GraphNode nodeTo = this.graph.lookupNode(t.getTo().getId());
        this.graph.addEdge(t.getId(), nodeFrom, nodeTo, t.getData().toString());
        this.cmd.redo();
    }

    @Override
    public void undo() {
        this.graph.removeEdge(this.ccmd.getTransition().getId());
        this.cmd.undo();
    }

    public AutomatonTransition getTransition() {
        return this.ccmd.getTransition();
    }

}
