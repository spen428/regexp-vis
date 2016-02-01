package controller;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.GraphCanvasFX;
import view.GraphNode;

public class RegexpVisApp extends Application {

    private static final String TEXTFIELD_PROMPT = "Type a regular expression and press Enter.";
    private static final double CONTROL_PANEL_PADDING_VERTICAL = 20;
    private static final double CONTROL_PANEL_PADDING_HORIZONTAL = 35;
    private static final int BUTTON_PANEL_PADDING = 10;
    private static final double HISTORY_LIST_WIDTH = 140;
    private static final String HISTORY_LIST_HIDE_TEXT = "Hide History List";
    private static final String HISTORY_LIST_SHOW_TEXT = "Show History List";

    @Override
    public void start(Stage stage) {
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });

        final VBox root = new VBox();
        final HBox canvasContainer = new HBox();
        final ListView<String> historyList = new ListView<>();

        Scene scene = new Scene(root, 800, 600);

        MenuBar menuBar = new MenuBar();

        // --- Menu File
        Menu menuFile = new Menu("File");
        menuFile.getItems().addAll(new MenuItem("Import Graph..."),
                new MenuItem("Export Graph..."), new MenuItem("Exit"));

        // --- Menu Edit
        Menu menuEdit = new Menu("Edit");
        MenuItem menuEditBlah = new MenuItem("Blah");
        menuEdit.getItems().addAll(new MenuItem("Undo"), new MenuItem("Redo"),
                menuEditBlah, new MenuItem("Preferences..."));
        menuEditBlah.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                System.out.println("Hello World!11");
            }
        });

        // --- Menu View
        Menu menuView = new Menu("View");
        final MenuItem menuViewHistory = new MenuItem(HISTORY_LIST_HIDE_TEXT);
        menuView.getItems().addAll(menuViewHistory,
                new MenuItem("Hide Control Panel"));
        menuViewHistory.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                historyList.setVisible(!historyList.isVisible());
                historyList.setManaged(historyList.isVisible());
                menuViewHistory.setText(historyList.isVisible()
                        ? HISTORY_LIST_HIDE_TEXT : HISTORY_LIST_SHOW_TEXT);
            }
        });

        // --- Menu About
        Menu menuHelp = new Menu("Help");
        menuHelp.getItems().addAll(new MenuItem("About"));

        menuBar.getMenus().addAll(menuFile, menuEdit, menuView, menuHelp);
        root.getChildren().addAll(menuBar);
        // root.getChildren().add(btn);

        // Group group = new Group();

        // Graph canvas
        GraphCanvasFX c = new GraphCanvasFX();
        VBox.setVgrow(canvasContainer, javafx.scene.layout.Priority.ALWAYS);
        VBox.setVgrow(c, javafx.scene.layout.Priority.ALWAYS);
        HBox.setHgrow(canvasContainer, javafx.scene.layout.Priority.ALWAYS);
        HBox.setHgrow(c, javafx.scene.layout.Priority.ALWAYS);
        canvasContainer.getChildren().add(c);

        // History list also part of the canvas container
        for (int i = 0; i < 33; i++) {
            historyList.getItems().add("Step " + i);
        }
        historyList.setMinWidth(HISTORY_LIST_WIDTH);
        historyList.setMaxWidth(HISTORY_LIST_WIDTH);
        canvasContainer.getChildren().add(historyList);

        root.getChildren().add(canvasContainer);
        c.requestFocus(); // Pulls focus away from the text field

        // Control panel containing buttons and text input box
        VBox controlPanel = new VBox();

        HBox buttonPanel = new HBox();
        buttonPanel.setMinSize(0, BUTTON_PANEL_PADDING * 2);
        buttonPanel.setPadding(new Insets(BUTTON_PANEL_PADDING));
        buttonPanel.setAlignment(Pos.CENTER);
        buttonPanel.getChildren().addAll(new Button("|<<"), new Button("<--"),
                new Button("Load"), new Button("Save"),
                new Button("Self-Destruct"), new Button("-->"),
                new Button(">>|"));
        controlPanel.getChildren().add(buttonPanel);

        final TextField textField = new TextField(TEXTFIELD_PROMPT);
        textField.setPadding(new Insets(5));
        controlPanel.setPadding(new Insets(CONTROL_PANEL_PADDING_VERTICAL,
                CONTROL_PANEL_PADDING_HORIZONTAL,
                CONTROL_PANEL_PADDING_VERTICAL,
                CONTROL_PANEL_PADDING_HORIZONTAL));
        controlPanel.getChildren().add(textField);
        root.getChildren().add(controlPanel);

        // Textfield listeners
        textField.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    if (textField.getText().equals(TEXTFIELD_PROMPT)) {
                        // Clear text field on click if the prompt is displayed
                        textField.clear();
                        textField.requestFocus();
                    }
                }
            }
        });
        /*
         * onKeyReleased rather than onKeyPressed, to prevent double-hits. This
         * however may be perceived as undesirable behaviour by the user.
         */
        textField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    String input = textField.getText().trim();
                    if (!input.isEmpty()) {
                        System.out.printf("Entered regexp: %s%n", input);
                    }
                }
            }
        });

        GraphNode n1 = c.addNode(0, 10.0, 10.0);
        GraphNode n2 = c.addNode(1, 30.0, 10.0);
        c.setNodeUseStartStyle(n1, true);
        c.setNodeUseFinalStyle(n2, true);
        c.addEdge(0, n1, n2, "sometext1");
        //c.addEdge(1, n1, n2, "sometext2");
        c.addEdge(2, n2, n1, "sometext3");
        c.addEdge(3, n2, n1, "sometext4");
        c.addEdge(4, n1, n1, "loop1");
        c.addEdge(5, n1, n1, "loop2");
        c.addEdge(6, n1, n1, "loop3");
        c.setWidth(100);
        c.setHeight(100);
        // c.widthProperty().bind(canvasContainer.widthProperty());
        // c.heightProperty().bind(canvasContainer.heightProperty());
        // c.widthProperty().addListener(o -> c.doRepaint());
        c.doRedraw();

        stage.setTitle("Hello World!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}