package test.controller;

import controller.Activity;
import controller.RegexpVisApp;
import javafx.stage.Stage;
import view.GraphCanvasEvent;
import view.GraphCanvasFX;

/**
 * Extension of {@link RegexpVisApp} with exposed methods for testing purposes.s
 * 
 * @author sp611
 *
 */
public class TestRegexpVisApp extends RegexpVisApp {

    public TestRegexpVisApp(Stage stage) {
        super(stage);
    }

    public TestRegexpVisApp(Stage stage, String regexp) {
        super(stage);
        this.currentActivity.onEnteredRegexp(regexp);
    }

    public void setActivity(Activity<GraphCanvasEvent> activity) {
        this.currentActivity = activity;
    }

    public Activity<GraphCanvasEvent> getCurrentActivity() {
        return this.currentActivity;
    }

    public GraphCanvasFX getGraph() {
        return this.mCanvas;
    }

}
