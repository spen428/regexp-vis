package controller;

import java.net.URL;

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

    public static final double WIDTH = 400;
    public static final double HEIGHT = 600;

    private final WebView browser;
    private final WebEngine engine;

    public UserGuideView() {
        super();
        this.browser = new WebView();
        this.engine = this.browser.getEngine();
        URL url = this.getClass().getResource("/userguide/index.html");
        System.out.println(url);
        this.engine.load(url.toExternalForm());
        getChildren().add(this.browser);
    }

}
