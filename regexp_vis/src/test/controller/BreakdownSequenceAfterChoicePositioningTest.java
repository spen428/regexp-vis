package test.controller;

import javafx.application.Application;
import javafx.stage.Stage;

public class BreakdownSequenceAfterChoicePositioningTest extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        TestRegexpVisApp ui = new TestRegexpVisApp(stage, "ab*c*|(d*e)*f");
        BreakdownTestUtils.breakdownEdges(ui, 0, 2);
    }

}