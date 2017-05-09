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

import java.util.List;

import model.AutomatonTransition;
import model.ConjoinParallelTransitionsCommand;
import view.GraphCanvasFX;

public class ConjoinParallelTransitionsUICommand extends CompositeUICommand {

    private final ConjoinParallelTransitionsCommand ccmd;

    public ConjoinParallelTransitionsUICommand(GraphCanvasFX graph,
            ConjoinParallelTransitionsCommand cmd) {
        super(graph, cmd);
        this.ccmd = cmd;
    }

    @Override
    public String getDescription() {
        List<AutomatonTransition> oldTransitions = ccmd
                .getParallelTransitions();

        String fromStr = StringUtils.transitionListToEnglish(oldTransitions);

        String toStr = ccmd.getNewTransition().getData().toString();
        return String
                .format("Conjoined transitions %s into %s", fromStr, toStr);
    }
}
