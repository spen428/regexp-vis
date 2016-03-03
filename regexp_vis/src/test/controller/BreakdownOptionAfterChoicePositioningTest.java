package test.controller;

import javafx.application.Application;
import javafx.stage.Stage;

public class BreakdownOptionAfterChoicePositioningTest extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        TestRegexpVisApp ui = new TestRegexpVisApp(stage, "c(d?e|f)|b*|d?");
        BreakdownTestUtils.breakdownEdges(ui, 0, 1, 2);
    }

}