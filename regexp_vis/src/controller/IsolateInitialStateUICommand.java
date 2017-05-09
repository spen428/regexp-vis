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
import model.Command;
import model.IsolateInitialStateCommand;
import view.GraphCanvasFX;
import view.GraphNode;

public class IsolateInitialStateUICommand extends CompositeUICommand {

    private final IsolateInitialStateCommand ccmd;

    private static final double PLACEMENT_RADIUS_MIN = 80;
    private static final double PLACEMENT_RADIUS_MAX = 120;

    public IsolateInitialStateUICommand(GraphCanvasFX graph,
            IsolateInitialStateCommand cmd) {
        super(graph, cmd);
        this.ccmd = cmd;

        GraphNode startStateNode = graph.lookupNode(cmd.getAutomaton()
                .getStartState().getId());
        Point2D startStatePos = new Point2D(startStateNode.getX(),
                startStateNode.getY());

        super.commands.clear();
        AddStateCommand newStateCmd = cmd.getNewStateCommand();
        for (Command c : cmd.getCommands()) {
            // Place position of a new state a bit more intelligently
            if (c == newStateCmd) {
                // Choose a random position around the current initial state
                double r = PLACEMENT_RADIUS_MIN + Math.random()
                        * (PLACEMENT_RADIUS_MAX - PLACEMENT_RADIUS_MIN);
                double theta = Math.random() * 2 * Math.PI;
                double x = r * Math.cos(theta);
                double y = r * Math.sin(theta);
                Point2D location = startStatePos.add(x, y);

                super.commands.add(new AddStateUICommand(graph, newStateCmd,
                        location));
            } else {
                super.commands.add(UICommand.fromCommand(graph, c));
            }
        }
    }

    @Override
    public String getDescription() {
        return "Isolated initial state";
    }
}
