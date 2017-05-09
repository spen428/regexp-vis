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
import model.RemoveLoopTransitionCommand;
import view.GraphCanvasFX;

public class RemoveLoopTransitionUICommand extends CompositeUICommand {

    private final RemoveLoopTransitionCommand ccmd;

    public RemoveLoopTransitionUICommand(GraphCanvasFX graph,
            RemoveLoopTransitionCommand cmd) {
        super(graph, cmd);
        this.ccmd = cmd;
    }

    @Override
    public String getDescription() {
        AutomatonTransition t = ccmd.getLoopTransition();
        String transStr = t.getData().toString();
        String stateStr = t.getFrom().toString();

        return String.format("Removed loop transition %s from state %s",
                transStr, stateStr);
    }

}
