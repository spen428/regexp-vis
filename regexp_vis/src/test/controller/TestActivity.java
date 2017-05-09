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
package test.controller;

import controller.Activity;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import model.Automaton;
import model.CommandHistory;
import view.GraphCanvasEvent;
import view.GraphCanvasFX;

public class TestActivity extends Activity {

    public TestActivity(GraphCanvasFX canvas, Automaton automaton) {
        super(canvas, automaton);
    }

    public TestActivity(GraphCanvasFX canvas, Automaton automaton,
            CommandHistory history) {
        super(canvas, automaton, history);
    }

    @Override
    public void onNodeClicked(GraphCanvasEvent event) {
        // Unused
    }

    @Override
    public void onEdgeClicked(GraphCanvasEvent event) {
        // Unused
    }

    @Override
    public void onBackgroundClicked(GraphCanvasEvent event) {
        // Unused
    }

    @Override
    public void onContextMenuRequested(ContextMenuEvent event) {
        // Unused
    }

    @Override
    public void onHideContextMenu(MouseEvent event) {
        // Unused
    }

}
