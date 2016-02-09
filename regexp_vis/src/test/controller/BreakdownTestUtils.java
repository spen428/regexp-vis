package test.controller;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import view.GraphCanvasEvent;
import view.GraphEdge;

/**
 * Static methods for testing Regexp breakdown.
 * 
 * @author sp611
 *
 */
class BreakdownTestUtils {

    static void breakdownEdges(TestRegexpVisApp ui, int... edgeIds) {
        for (int i : edgeIds) {
            GraphEdge edge = ui.getGraph().lookupEdge(i);
            ui.getCurrentActivity().processEvent(createBreakdownEvent(edge));
        }
    }

    static GraphCanvasEvent createBreakdownEvent(GraphEdge edge) {
        return new GraphCanvasEvent(new MouseEvent(0, null, null, 0, 0, 0, 0,
                MouseButton.PRIMARY, 2, false, false, false, false, false,
                false, false, false, false, false, null), null, edge);
    }

}