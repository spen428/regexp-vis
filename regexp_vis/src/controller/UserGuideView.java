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

import java.net.URL;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * Based on the example at
 * https://docs.oracle.com/javafx/2/webview/jfxpub-webview.htm
 *
 * @author sp611
 *
 */
public class UserGuideView extends Region {

    public static final double WIDTH = 760;
    public static final double HEIGHT = 500;

    private final WebView browser;
    private final WebEngine engine;

    public UserGuideView() {
        super();
        this.browser = new WebView();
        this.browser.setPrefWidth(WIDTH);
        this.engine = this.browser.getEngine();
        URL url = this.getClass().getResource("/userguide/index.html");
        this.engine.load(url.toExternalForm());
        // Make sure the WebView itself grows
        HBox.setHgrow(this.browser, Priority.ALWAYS);

        // Put the WebView into a HBox which automatically resizes
        HBox hbox = new HBox();
        hbox.getChildren().add(this.browser);
        hbox.prefWidthProperty().bind(widthProperty());
        hbox.prefHeightProperty().bind(heightProperty());
        getChildren().add(hbox);
    }

}
