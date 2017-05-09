/*
 * Copyright (c) 2015, 2016 Matthew J. Nicholls, Samuel Pengelly,
 * Parham Ghassemi, William R. Dix
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
    public static final String VERSION = "0.4a";

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
