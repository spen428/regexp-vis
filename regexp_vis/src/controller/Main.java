package controller;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static final String TITLE = "Regular Expression Visualiser";
    public static final String VERSION = "0.987a";

    public static void main(String[] args) {
        Application.launch(args);
    }

    @SuppressWarnings("unused")
    @Override
    public void start(Stage stage) throws Exception {
        new RegexpVisApp(stage);
    }

}