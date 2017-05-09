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
package test.view;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import view.GraphCanvasEvent;
import view.GraphCanvasFX;
import view.GraphNode;

public class GraphCanvasFXTest extends Application {
    private GraphCanvasFX mCanvas;

    @Override
    public void start(Stage stage)
    {
        final VBox root = new VBox();
        final HBox canvasContainer = new HBox();
        final ListView<String> historyList = new ListView<>();
        final VBox controlPanel = new VBox();

        Scene scene = new Scene(root, 800, 600);

        MenuBar menuBar = new MenuBar();

        // --- Menu File
        Menu menuFile = new Menu("File");
        MenuItem exit = new MenuItem("Exit");

        // --- Menu Edit
        Menu menuEdit = new Menu("Edit");
        MenuItem menuEditBlah = new MenuItem("Blah");

        // --- Menu Activity
        Menu menuActivity = new Menu("Activity");

        // --- Menu View
        Menu menuView = new Menu("View");

        // --- Menu About
        Menu menuHelp = new Menu("Help");
        MenuItem about = new MenuItem("About");
        menuHelp.getItems().addAll(about);

        menuBar.getMenus().addAll(menuFile, menuEdit, menuActivity, menuView,
                menuHelp);
        root.getChildren().addAll(menuBar);

        // Graph canvas
        this.mCanvas = new GraphCanvasFX();
        VBox.setVgrow(canvasContainer, javafx.scene.layout.Priority.ALWAYS);
        VBox.setVgrow(this.mCanvas, javafx.scene.layout.Priority.ALWAYS);
        HBox.setHgrow(canvasContainer, javafx.scene.layout.Priority.ALWAYS);
        HBox.setHgrow(this.mCanvas, javafx.scene.layout.Priority.ALWAYS);
        canvasContainer.getChildren().add(this.mCanvas);

        // History list also part of the canvas container
        for (int i = 0; i < 33; i++) {
            historyList.getItems().add("Step " + i);
        }

        canvasContainer.getChildren().add(historyList);

        root.getChildren().add(canvasContainer);
        this.mCanvas.requestFocus(); // Pulls focus away from the text field

        root.getChildren().add(controlPanel);

        stage.setTitle("GraphCanvasFX test app");
        stage.setScene(scene);
        stage.show();

        GraphNode n1 = mCanvas.addNode(0, 100.0, 100.0);
        GraphNode n2 = mCanvas.addNode(1, 300.0, 100.0);
        mCanvas.addEdge(0, n1, n2, "sometext1");
        mCanvas.addEdge(1, n1, n2, "sometext2");
        mCanvas.addEdge(2, n2, n1, "sometext3");
        mCanvas.addEdge(3, n2, n1, "sometext4");
        mCanvas.addEdge(4, n1, n1, "loop1");
        mCanvas.addEdge(5, n1, n1, "loop2");
        mCanvas.addEdge(6, n1, n1, "loop3");
        mCanvas.doRedraw();
        mCanvas.startCreateEdgeMode(n1);
        mCanvas.setNodeBackgroundColour(n1, Color.BEIGE);

        mCanvas.setOnCreatedEdge(new EventHandler<GraphCanvasEvent>() {
            @Override
            public void handle(GraphCanvasEvent event) {
                Alert a = new Alert(AlertType.INFORMATION, "Edge created");
                a.showAndWait();
            }
        });

    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
