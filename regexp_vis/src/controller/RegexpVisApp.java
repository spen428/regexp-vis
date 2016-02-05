package controller;

import java.util.LinkedList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
import model.Automaton;
import model.AutomatonState;
import model.AutomatonTransition;
import model.BasicRegexp;
import model.InvalidRegexpException;
import view.GraphCanvasEvent;
import view.GraphCanvasFX;
import view.GraphEdge;
import view.GraphNode;

public class RegexpVisApp extends Application {

    private static final String TEXTFIELD_PROMPT = "Type a regular expression and press Enter.";
    private static final double CONTROL_PANEL_PADDING_VERTICAL = 20;
    private static final double CONTROL_PANEL_PADDING_HORIZONTAL = 35;
    private static final int BUTTON_PANEL_PADDING = 10;
    private static final double HISTORY_LIST_WIDTH = 140;
    private static final String HISTORY_LIST_HIDE_TEXT = "Hide History List";
    private static final String HISTORY_LIST_SHOW_TEXT = "Show History List";

    private GraphCanvasFX mCanvas;

    @Override
    public void start(Stage stage) {
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

        // Graph canvas
        mCanvas = new GraphCanvasFX();
        VBox.setVgrow(canvasContainer, javafx.scene.layout.Priority.ALWAYS);
        VBox.setVgrow(mCanvas, javafx.scene.layout.Priority.ALWAYS);
        HBox.setHgrow(canvasContainer, javafx.scene.layout.Priority.ALWAYS);
        HBox.setHgrow(mCanvas, javafx.scene.layout.Priority.ALWAYS);
        canvasContainer.getChildren().add(mCanvas);

        // History list also part of the canvas container
        for (int i = 0; i < 33; i++) {
            historyList.getItems().add("Step " + i);
        }
        historyList.setMinWidth(HISTORY_LIST_WIDTH);
        historyList.setMaxWidth(HISTORY_LIST_WIDTH);
        canvasContainer.getChildren().add(historyList);

        root.getChildren().add(canvasContainer);
        mCanvas.requestFocus(); // Pulls focus away from the text field

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
                    onEnteredRegexp(textField.getText());
                    String input = textField.getText().trim();
                    if (!input.isEmpty()) {
                        System.out.printf("Entered regexp: %s%n", input);
                    }
                }
            }
        });

        mCanvas.setOnEdgeClicked(new EventHandler<GraphCanvasEvent>() {
            @Override
            public void handle(GraphCanvasEvent event) {
                MouseEvent mouseEvent = event.getMouseEvent();
                if (mouseEvent.getClickCount() == 2) {
                    onEdgeDoubleClicked(event);
                }
            }
        });

        stage.setTitle("Hello World!");
        stage.setScene(scene);
        stage.show();
    }

    private void onEnteredRegexp(String text)
    {
        text = text.trim();
        System.out.printf("Entered: %s%n", text);
        if (text.isEmpty()) {
            // TODO: message user about empty text field
            System.out.printf("Entered regexp: %s%n", text);
            return;
        }

        BasicRegexp re = null;
        try {
            re = BasicRegexp.parseRegexp(text);
            //BasicRegexp.debugPrintBasicRegexp(0, re);
        } catch (InvalidRegexpException e1) {
            Alert alert = new Alert(AlertType.ERROR,
                    "Error: invalid regexp entered. Details: \n\n"
                            + e1.getMessage());
            alert.showAndWait();
            return;
        }

        mCanvas.removeAllNodes();
        Automaton automaton = new Automaton();
        AutomatonState startState = automaton.getStartState();
        AutomatonState finalState = automaton.createNewState();
        AutomatonTransition trans = automaton.createNewTransition(startState, finalState, re);
        finalState.setFinal(true);
        automaton.addStateWithTransitions(finalState, new LinkedList<AutomatonTransition>());
        automaton.addTransition(trans);

        GraphNode startNode = mCanvas.addNode(startState.getId(), 50.0, 50.0);
        GraphNode endNode = mCanvas.addNode(finalState.getId(), mCanvas.getWidth() - 50.0, mCanvas.getHeight() - 50.0);
        GraphEdge edge = mCanvas.addEdge(trans.getId(), startNode, endNode, re.toString());
    }

    private void onEdgeDoubleClicked(GraphCanvasEvent event)
    {

    }

    public static void main(String[] args) {
        launch(args);
    }
}