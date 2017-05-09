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
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import model.Automaton;
import model.AutomatonState;
import model.AutomatonTransition;
import model.BreakdownCommand;
import model.Command;
import model.RemoveStateCleanlyCommand;
import model.TranslationTools;
import view.GraphCanvasEvent;
import view.GraphCanvasFX;
import view.GraphEdge;

/**
 *
 * @author sp611
 *
 */
public class RegexpBreakdownActivity extends Activity {

    private static final Logger LOGGER = Logger.getLogger("controller");

    public RegexpBreakdownActivity(GraphCanvasFX canvas, Automaton automaton) {
        super(canvas, automaton);
    }

    private void initiateActivity() {
        // Only show message if we are showing something on the canvas
        if (checkActivityDone() && canvas.getNumNodes() > 0) {
            onActivityDone();
        }
    }

    private void onActivityDone() {
        new Alert(AlertType.INFORMATION,
                "The regular expression has been converted to an NFA/DFA")
                .showAndWait();
    }

    /**
     * Checks whether this activity is done. That is when:
     * <ol>
     *   <li> All transitions are single character transitions
     * </ol>
     *
     * @return True if this activity is done, false otherwise
     */
    private boolean checkActivityDone() {
        List<AutomatonTransition> todo = TranslationTools
                .getAllTransitionsToBreakdown(this.automaton);

        return todo == null;
    }

    @Override
    public void onEnteredRegexp(String text) {
        super.onEnteredRegexp(text);
        ensureNoUnreachableStates(this.automaton, this.canvas);
        initiateActivity();
    }

    @Override
    public void onGraphFileImport(GraphExportFile file) {
        super.onGraphFileImport(file);
        ensureNoUnreachableStates(this.automaton, this.canvas);
        initiateActivity();
    }

    @Override
    public void onStarted() {
        ensureNoUnreachableStates(this.automaton, this.canvas);
        initiateActivity();
    }

    @Override
    public void onNodeClicked(GraphCanvasEvent event) {

    }

    @Override
    public void onEdgeClicked(GraphCanvasEvent event) {
        if (event.getMouseEvent().getClickCount() == 2) {
            onEdgeDoubleClick(event);
        }
    }

    @Override
    public void onBackgroundClicked(GraphCanvasEvent event) {

    }

    @Override
    public void onContextMenuRequested(ContextMenuEvent event) {

    }

    @Override
    public void onHideContextMenu(MouseEvent event) {

    }

    private void onEdgeDoubleClick(GraphCanvasEvent event) {
        if (this.history.getHistoryIdx() != this.history.getHistorySize()
                && !this.history.isClobbered()) {
            LOGGER.log(Level.FINE, "Ignoring breakdown event as we are not at "
                    + "the end of the history list.");
            return;
        }

        GraphEdge edge = event.getTargetEdge();
        if (edge == null) {
            return;
        }

        AutomatonTransition trans = this.automaton
                .getAutomatonTransitionById(edge.getId());

        if (trans == null) {
            LOGGER.log(Level.WARNING, "Could not find an edge with id " + edge.getId());
            return;
        }

        BreakdownCommand cmd = TranslationTools
                .createBreakdownCommand(this.automaton, trans);
        super.executeNewCommand(cmd);

        if (checkActivityDone()) {
            onActivityDone();
        }
    }
}
