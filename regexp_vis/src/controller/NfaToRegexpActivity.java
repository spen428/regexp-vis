package controller;

import view.GraphCanvasEvent;
import view.GraphCanvasFX;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import model.Automaton;

public class NfaToRegexpActivity extends Activity {

    public NfaToRegexpActivity(GraphCanvasFX canvas, Automaton automaton) {
        super(canvas, automaton);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onNodeClicked(GraphCanvasEvent event) {

    }

    @Override
    public void onEdgeClicked(GraphCanvasEvent event) {

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

}
