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

import java.util.Set;

import javafx.geometry.Point2D;
import model.AddStateCommand;
import model.AutomatonState;
import model.Command;
import model.RemoveNonDeterminismCommand;
import view.GraphCanvasFX;
import view.GraphNode;

public class RemoveNonDeterminismUICommand extends CompositeUICommand {

    private final RemoveNonDeterminismCommand ccmd;

    public RemoveNonDeterminismUICommand(GraphCanvasFX graph,
            RemoveNonDeterminismCommand cmd) {
        super(graph, cmd);
        this.ccmd = cmd;

        super.commands.clear();
        AddStateCommand newStateCmd = cmd.getNewStateCommand();
        for (Command c : cmd.getCommands()) {
            // Place position of a new state a bit more intelligently
            if (c == newStateCmd) {
                Set<AutomatonState> reachableSet = cmd.getReachableSet();

                // Calculate average position of the reachable states
                Point2D location = Point2D.ZERO;
                // reachableSet.size() should always be > 1
                int adjustedSize = reachableSet.size();
                for (AutomatonState s : reachableSet) {
                    if (s == cmd.getState()) {
                        // Don't average the over the target state if for
                        // example a loop is part of the non-determinism
                        adjustedSize--;
                        continue;
                    }
                    GraphNode n = graph.lookupNode(s.getId());
                    location = location.add(n.getX(), n.getY());
                }
                location = location.multiply(1.0 / adjustedSize);

                // Average that average with the location of the target state
                GraphNode targetNode = graph.lookupNode(cmd.getState().getId());
                location = location.add(targetNode.getX(), targetNode.getY());
                location = location.multiply(0.5);

                super.commands.add(new AddStateUICommand(graph, newStateCmd,
                        location));
            } else {
                super.commands.add(UICommand.fromCommand(graph, c));
            }
        }
    }

    @Override
    public String getDescription() {
        return "Removed non-determinism from state "
                + this.ccmd.getState().toString();
    }

}
