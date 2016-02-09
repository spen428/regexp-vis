package test.controller;

import javafx.application.Application;
import javafx.stage.Stage;

public class BreakdownSequencePositioningTest extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        TestRegexpVisApp ui = new TestRegexpVisApp(stage, "abcde");
        BreakdownTestUtils.breakdownEdges(ui, 0);
    }

}