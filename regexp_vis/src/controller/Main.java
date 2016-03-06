package controller;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static final String TITLE = "Regular Language Visualiser";
    // 0.{iteration number} alpha, 1.0 when feature complete
    public static final String VERSION = "0.3a";

    public static void main(String[] args) {
        initLogging();
        setLogLevels(Level.INFO);

        if (args.length == 1) {
            if (args[0].equals("-v")) { // Debug logging
                setLogLevels(Level.FINE);
            }
        }

        Application.launch();
    }

    @SuppressWarnings("unused")
    @Override
    public void start(Stage stage) throws Exception {
        new RegexpVisApp(stage);
    }

    private static void initLogging() {
        // Disable default log handler
        LogManager.getLogManager().reset();

        // Add own log handlers
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        Logger.getLogger("model").addHandler(handler);
        Logger.getLogger("view").addHandler(handler);
        Logger.getLogger("controller").addHandler(handler);
    }

    private static void setLogLevels(Level level) {
        Logger.getLogger("model").setLevel(level);
        Logger.getLogger("view").setLevel(level);
        Logger.getLogger("controller").setLevel(level);
    }

}