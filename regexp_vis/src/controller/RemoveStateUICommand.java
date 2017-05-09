/*
 * Copyright (c) 2015, 2016 Matthew J. Nicholls, Samuel Pengelly,
 * Parham Ghassemi, William R. Dix
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package controller;

import javafx.geometry.Point2D;
import model.AutomatonState;
import model.AutomatonTransition;
import model.RemoveStateCommand;
import view.GraphCanvasFX;
import view.GraphNode;

/**
 * Command to remove a state and its outgoing transitions from an automaton
 *
 * @author sp611
 */
public class RemoveStateUICommand extends UICommand {

    private final RemoveStateCommand ccmd;

    public RemoveStateUICommand(GraphCanvasFX graph, RemoveStateCommand cmd) {
        super(graph, cmd);
        this.ccmd = cmd;
    }

    @Override
    public void redo() {
        AutomatonState state = this.ccmd.getState();
        // Update coordinates so we restore the node to the position it was
        // before in undo()
        GraphNode node = this.graph.lookupNode(state.getId());
        this.location = new Point2D(node.getX(), node.getY());
        this.graph.removeNode(state.getId());
        this.cmd.redo();
    }

    @Override
    public void undo() {
        GraphNode nodeFrom = this.graph.addNode(this.ccmd.getState().getId(),
                this.location.getX(), this.location.getY());
        if (this.ccmd.getState().isFinal()) {
            // Make sure we set this node to use the "final style" if this is
            // a final state
            this.graph.setNodeUseFinalStyle(nodeFrom, true);
        }

        for (AutomatonTransition t : this.ccmd.getTransitions()) {
            GraphNode nodeTo = this.graph.lookupNode(t.getTo().getId());
            this.graph.addEdge(t.getId(), nodeFrom, nodeTo,
                    t.getData().toString());
        }
        this.cmd.undo();
    }

    @Override
    public String getDescription() {
        return "Removed state " + this.ccmd.getState().toString();
    }

}
