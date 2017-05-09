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

    @Override
    public String getDescription() {
        return "Removed transition "
                + this.ccmd.getTransition().getData().toString();
    }

}
