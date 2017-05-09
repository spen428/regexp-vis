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
package test.controller;

import controller.Activity;
import controller.RegexpVisApp;
import javafx.stage.Stage;
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

    public void setActivity(Activity activity) {
        this.currentActivity = activity;
    }

    public Activity getCurrentActivity() {
        return this.currentActivity;
    }

    public GraphCanvasFX getGraph() {
        return this.mCanvas;
    }

}
