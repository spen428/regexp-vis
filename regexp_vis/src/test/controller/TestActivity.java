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
