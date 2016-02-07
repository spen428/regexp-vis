package test.view;

import javafx.application.Application;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import view.GraphCanvasEvent;
import view.GraphEdge;

public class BreakdownIterationPositioningTest extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        TestRegexpVisApp ui = new TestRegexpVisApp(stage, "ab*c*d*e*f");
        GraphEdge edge = ui.getGraph().lookupEdge(0);
        ui.getCurrentActivity().processEvent(createBreakdownEvent(edge));
        edge = ui.getGraph().lookupEdge(2);
        ui.getCurrentActivity().processEvent(createBreakdownEvent(edge));
        edge = ui.getGraph().lookupEdge(3);
        ui.getCurrentActivity().processEvent(createBreakdownEvent(edge));
        edge = ui.getGraph().lookupEdge(5);
        ui.getCurrentActivity().processEvent(createBreakdownEvent(edge));
        edge = ui.getGraph().lookupEdge(4);
        ui.getCurrentActivity().processEvent(createBreakdownEvent(edge));
    }

    private static GraphCanvasEvent createBreakdownEvent(GraphEdge edge) {
        return new GraphCanvasEvent(new MouseEvent(0, null, null, 0, 0, 0, 0,
                MouseButton.PRIMARY, 2, false, false, false, false, false,
                false, false, false, false, false, null), null, edge);
    }

}