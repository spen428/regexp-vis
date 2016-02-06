package controller;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Automaton;
import view.Activity;
import view.GraphCanvasEvent;
import view.GraphCanvasFX;
import view.NfaToDfaActivity;
import view.NfaToRegexpActivity;
import view.RegexpBreakdownActivity;

public class RegexpVisApp {

    enum ActivityType {
        ACTIVITY_REGEXP_BREAKDOWN("Breakdown Regular Expression to FSA"),
        ACTIVITY_NFA_TO_REGEXP("Convert NFA to Regular Expression"),
        ACTIVITY_NFA_TO_DFA("Convert NFA to DFA");

        private final String text;

        private ActivityType(String text) {
            this.text = text;
        }

        public String getText() {
            return this.text;
        }
    }

    private static final int BUTTON_PANEL_PADDING = 10;
    private static final int CONTROL_PANEL_PADDING_HORIZONTAL = 35;
    private static final int CONTROL_PANEL_PADDING_VERTICAL = 20;
    private static final String HISTORY_LIST_HIDE_TEXT = "Hide History List";
    private static final String HISTORY_LIST_SHOW_TEXT = "Show History List";
    private static final int HISTORY_LIST_WIDTH = 140;
    private static final String TEXTFIELD_PROMPT = "Type a regular expression and press Enter.";

    private final GraphCanvasFX mCanvas;
    private final Automaton automaton;
    private final Activity<GraphCanvasEvent>[] activities;
    final CheckMenuItem[] activityMenuItems;

    /* Keep track of enter key to prevent submitting regexp multiple times. */
    protected boolean enterKeyDown;
    private Activity<GraphCanvasEvent> currentActivity;

    public RegexpVisApp(Stage stage) {
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
            @Override
            public void handle(ActionEvent t) {
                System.out.println("Hello World!11");
            }
        });

        // --- Menu Activity
        Menu menuActivity = new Menu("Activity");
        ActivityType[] actTypes = ActivityType.values();
        this.activityMenuItems = new CheckMenuItem[actTypes.length];
        for (int i = 0; i < actTypes.length; i++) {
            final CheckMenuItem item = new CheckMenuItem(actTypes[i].getText());
            this.activityMenuItems[i] = item;
            final ActivityType act = actTypes[i];
            item.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    setActivity(act);
                }
            });
            menuActivity.getItems().add(item);
        }

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
        historyList.setMinWidth(HISTORY_LIST_WIDTH);
        historyList.setMaxWidth(HISTORY_LIST_WIDTH);
        canvasContainer.getChildren().add(historyList);

        root.getChildren().add(canvasContainer);
        this.mCanvas.requestFocus(); // Pulls focus away from the text field

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

        // Textfield focus listener
        // https://stackoverflow.com/questions/16549296/how-perform-task-on-javafx-textfield-at-onfocus-and-outfocus
        textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable,
                    Boolean oldValue, Boolean newValue) {
                if (newValue.booleanValue()) {
                    if (textField.getText().equals(TEXTFIELD_PROMPT)) {
                        // Clear text field on click if the prompt is displayed
                        textField.clear();
                        textField.requestFocus();
                    }
                } else {
                    if (textField.getText().isEmpty()) {
                        // Paste prompt on defocus if text box is empty
                        textField.setText(TEXTFIELD_PROMPT);
                    }
                }
            }
        });
        /*
         * onKeyReleased rather than onKeyPressed, to prevent double-hits. This
         * however may be perceived as undesirable behaviour by the user.
         */
        textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER
                        && !RegexpVisApp.this.enterKeyDown) {
                    RegexpVisApp.this.enterKeyDown = true;
                    String input = textField.getText().trim();
                    if (!input.isEmpty()) {
                        onEnteredRegexp(input);
                    }
                }
            }
        });
        textField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    RegexpVisApp.this.enterKeyDown = false;
                }
            }
        });

        this.mCanvas.setOnEdgeClicked(new EventHandler<GraphCanvasEvent>() {
            @Override
            public void handle(GraphCanvasEvent event) {
                MouseEvent mouseEvent = event.getMouseEvent();
                if (mouseEvent.getClickCount() == 2) {
                    onEdgeDoubleClicked(event);
                }
            }
        });

        /* Init fields */
        this.automaton = new Automaton();
        this.activities = new Activity[ActivityType.values().length];
        /* Using ordinals of enum to prevent misordering */
        this.activities[ActivityType.ACTIVITY_REGEXP_BREAKDOWN
                .ordinal()] = new RegexpBreakdownActivity(this.mCanvas,
                        this.automaton);
        this.activities[ActivityType.ACTIVITY_NFA_TO_DFA
                .ordinal()] = new NfaToDfaActivity(this.mCanvas,
                        this.automaton);
        this.activities[ActivityType.ACTIVITY_NFA_TO_REGEXP
                .ordinal()] = new NfaToRegexpActivity(this.mCanvas,
                        this.automaton);
        setActivity(ActivityType.ACTIVITY_REGEXP_BREAKDOWN);

        stage.setTitle("Hello World!");
        stage.setScene(scene);
        stage.show();
    }

    protected void setActivity(ActivityType actType) {
        if (actType == null) {
            throw new IllegalArgumentException();
        }
        this.currentActivity = this.activities[actType.ordinal()];
        /*
         * Set "checked" status of all menu items to reflect the change of
         * activity
         */
        for (int i = 0; i < this.activityMenuItems.length; i++) {
            this.activityMenuItems[i].setSelected(i == actType.ordinal());
        }
    }

    void onEdgeDoubleClicked(GraphCanvasEvent event) {
        propogateToCurrentActivity(event);
    }

    private void propogateToCurrentActivity(GraphCanvasEvent event) {
        if (this.currentActivity != null) {
            this.currentActivity.processEvent(event);
        }
    }

    void onEnteredRegexp(String text) {
        if (this.currentActivity != null) {
            this.currentActivity.onEnteredRegexp(text);
        }
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}