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
import model.AddStateCommand;
import model.AutomatonState;
import view.GraphCanvasFX;
import view.GraphNode;

/**
 * Command to add a state to an automaton
 *
 * @author sp611
 */
public class AddStateUICommand extends UICommand {
    private final AddStateCommand ccmd;

    public AddStateUICommand(GraphCanvasFX graph, AddStateCommand cmd, double x,
            double y) {
        super(graph, cmd);
        this.ccmd = cmd;
        this.location = new Point2D(x, y);
    }

    public AddStateUICommand(GraphCanvasFX graph, AddStateCommand cmd,
            Point2D location) {
        super(graph, cmd);
        this.ccmd = cmd;
        this.location = location;
    }

    @Override
    public void redo() {
        GraphNode n = this.graph.addNode(this.ccmd.getState().getId(),
                this.location.getX(), this.location.getY());
        if (this.ccmd.getState().isFinal()) {
            // This state is already marked as final, make sure we retain that
            // visually
            this.graph.setNodeUseFinalStyle(n, true);
        }
        this.ccmd.redo();
    }

    @Override
    public void undo() {
        AutomatonState state = this.ccmd.getState();
        GraphNode node = this.graph.lookupNode(state.getId());
        // Update coordinates so we restore the node to the position it was
        // before in redo()
        this.location = new Point2D(node.getX(), node.getY());
        this.graph.removeNode(state.getId());
        this.ccmd.undo();
    }

    @Override
    public String getDescription() {
        return "Added state " + this.ccmd.getState().toString();
    }

}
