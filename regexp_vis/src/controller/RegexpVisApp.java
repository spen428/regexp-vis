package controller;

import java.util.Observable;
import java.util.Observer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Automaton;
import model.CommandHistory;
import view.GraphCanvasEvent;
import view.GraphCanvasFX;

public class RegexpVisApp implements Observer {

    /* Finals */
    final CheckMenuItem[] activityMenuItems;
    private final Activity<GraphCanvasEvent>[] activities;
    private final Automaton automaton;
    protected final GraphCanvasFX mCanvas;
    final ListView<String> historyList;

    /* Constants */
    private static final String CONTROL_PANEL_HIDE_TEXT = "Hide Control Panel";
    private static final String CONTROL_PANEL_SHOW_TEXT = "Show Control Panel";
    private static final String HISTORY_LIST_HIDE_TEXT = "Hide History List";
    private static final String HISTORY_LIST_SHOW_TEXT = "Show History List";
    private static final String TEXTFIELD_PROMPT = "Type a regular expression and press Enter.";
    private static final int BUTTON_PANEL_PADDING_PX = 10;
    private static final int CONTROL_PANEL_PADDING_HORIZONTAL_PX = 35;
    private static final int CONTROL_PANEL_PADDING_VERTICAL_PX = 20;
    private static final int HISTORY_LIST_WIDTH_PX = 140;
    protected static final String ABOUT_HEADER = "Regular Expression Visualiser"
            + " (v" + Main.VERSION + ")";
    protected static final String ABOUT_CONTENT = "Authors:\n\n"
            + "Matthew Nicholls\t<mjn33@kent.ac.uk>\n"
            + "Parham Ghassemi\t<pg272@kent.ac.uk>\n"
            + "Samuel Pengelly\t<sp611@kent.ac.uk>\n"
            + "William Dix\t\t<wrd2@kent.ac.uk>\n";
    public static final String WINDOW_TITLE = Main.TITLE + " v" + Main.VERSION;

    /* Variables */
    protected Activity<GraphCanvasEvent> currentActivity;
    protected boolean enterKeyDown;

    public RegexpVisApp(Stage stage) {
        final VBox root = new VBox();
        final HBox canvasContainer = new HBox();
        this.historyList = new ListView<>();
        final VBox controlPanel = new VBox();

        Scene scene = new Scene(root, 800, 600);

        MenuBar menuBar = new MenuBar();

        // --- Menu File
        Menu menuFile = new Menu("File");
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                exitApplication();
            }
        });
        menuFile.getItems().addAll(new MenuItem("Import Graph..."),
                new MenuItem("Export Graph..."), exit);

        // --- Menu Edit
        Menu menuEdit = new Menu("Edit");
        MenuItem menuEditUndo = new MenuItem("Undo");
        menuEditUndo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                RegexpVisApp.this.currentActivity.history.prev();
            }
        });
        MenuItem menuEditRedo = new MenuItem("Redo");
        menuEditRedo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                RegexpVisApp.this.currentActivity.history.next();
            }
        });
        final CheckMenuItem menuEditClobberHistory = new CheckMenuItem(
                "Clobber History");
        menuEditClobberHistory.setSelected(CommandHistory.CLOBBER_BY_DEFAULT);
        menuEditClobberHistory.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                boolean clob = RegexpVisApp.this.currentActivity.history
                        .isClobbered();
                RegexpVisApp.this.currentActivity.history.setClobbered(!clob);
                menuEditClobberHistory.setSelected(!clob);
            }
        });
        MenuItem menuEditPreferences = new MenuItem("Preferences...");
        menuEdit.getItems().addAll(menuEditUndo, menuEditRedo,
                menuEditClobberHistory, menuEditPreferences);

        // --- Menu Activity
        Menu menuActivity = new Menu("Activity");
        Activity.ActivityType[] actTypes = Activity.ActivityType.values();
        this.activityMenuItems = new CheckMenuItem[actTypes.length];
        for (int i = 0; i < actTypes.length; i++) {
            final CheckMenuItem item = new CheckMenuItem(actTypes[i].getText());
            this.activityMenuItems[i] = item;
            final Activity.ActivityType act = actTypes[i];
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
        menuViewHistory.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                RegexpVisApp.this.historyList
                        .setVisible(!RegexpVisApp.this.historyList.isVisible());
                RegexpVisApp.this.historyList
                        .setManaged(RegexpVisApp.this.historyList.isVisible());
                menuViewHistory
                        .setText(RegexpVisApp.this.historyList.isVisible()
                                ? HISTORY_LIST_HIDE_TEXT
                                : HISTORY_LIST_SHOW_TEXT);
            }
        });
        final MenuItem menuViewControlPanel = new MenuItem(
                CONTROL_PANEL_HIDE_TEXT);
        menuViewControlPanel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                controlPanel.setVisible(!controlPanel.isVisible());
                controlPanel.setManaged(controlPanel.isVisible());
                menuViewControlPanel.setText(controlPanel.isVisible()
                        ? CONTROL_PANEL_HIDE_TEXT : CONTROL_PANEL_SHOW_TEXT);
            }
        });
        menuView.getItems().addAll(menuViewHistory, menuViewControlPanel);

        // --- Menu About
        Menu menuHelp = new Menu("Help");
        MenuItem about = new MenuItem("About");
        about.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("About");
                alert.setHeaderText(ABOUT_HEADER);
                alert.setContentText(ABOUT_CONTENT);
                alert.showAndWait();
            }
        });
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
        this.historyList.setMinWidth(HISTORY_LIST_WIDTH_PX);
        this.historyList.setMaxWidth(HISTORY_LIST_WIDTH_PX);
        this.historyList.getSelectionModel().selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(
                            ObservableValue<? extends Number> observable,
                            Number oldValue, Number newValue) {
                        RegexpVisApp.this.setHistoryIdx(newValue);
                    }
                });
        canvasContainer.getChildren().add(this.historyList);

        root.getChildren().add(canvasContainer);
        this.mCanvas.requestFocus(); // Pulls focus away from the text field

        HBox buttonPanel = new HBox();
        buttonPanel.setMinSize(0, BUTTON_PANEL_PADDING_PX * 2);
        buttonPanel.setPadding(new Insets(BUTTON_PANEL_PADDING_PX));
        buttonPanel.setAlignment(Pos.CENTER);
        Button buttonBackToStart = new Button("|<<");
        buttonBackToStart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                RegexpVisApp.this.currentActivity.historyStart();
            }
        });
        Button buttonBack = new Button("<--");
        buttonBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                RegexpVisApp.this.currentActivity.historyPrev();
            }
        });
        Button buttonLoad = new Button("Load");
        Button buttonSave = new Button("Save");
        Button buttonForward = new Button("-->");
        buttonForward.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                // TODO: Breakdown next edge if at end of history?
                RegexpVisApp.this.currentActivity.historyNext();
            }
        });
        Button buttonForwardToEnd = new Button(">>|");
        buttonForwardToEnd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                // TODO: Breakdown all edges if at end of history?
                RegexpVisApp.this.currentActivity.historyEnd();
            }
        });
        buttonPanel.getChildren().addAll(buttonBackToStart, buttonBack,
                buttonLoad, buttonSave, buttonForward, buttonForwardToEnd);
        controlPanel.getChildren().add(buttonPanel);

        final HBox inputPanel = new HBox();
        final TextField textField = new TextField(TEXTFIELD_PROMPT);
        textField.setPadding(new Insets(5));
        HBox.setHgrow(textField, Priority.ALWAYS);
        Button buttonEnter = new Button("Enter");
        inputPanel.getChildren().addAll(textField, buttonEnter);
        controlPanel.setPadding(new Insets(CONTROL_PANEL_PADDING_VERTICAL_PX,
                CONTROL_PANEL_PADDING_HORIZONTAL_PX,
                CONTROL_PANEL_PADDING_VERTICAL_PX,
                CONTROL_PANEL_PADDING_HORIZONTAL_PX));
        controlPanel.getChildren().add(inputPanel);
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
        buttonEnter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String input = textField.getText().trim();
                if (!input.isEmpty()) {
                    onEnteredRegexp(input);
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
        this.activities = new Activity[Activity.ActivityType.values().length];
        /* Using ordinals of enum to prevent misordering */
        this.activities[Activity.ActivityType.ACTIVITY_REGEXP_BREAKDOWN
                .ordinal()] = new RegexpBreakdownActivity(this.mCanvas,
                        this.automaton);
        this.activities[Activity.ActivityType.ACTIVITY_NFA_TO_DFA
                .ordinal()] = new NfaToDfaActivity(this.mCanvas,
                        this.automaton);
        this.activities[Activity.ActivityType.ACTIVITY_NFA_TO_REGEXP
                .ordinal()] = new NfaToRegexpActivity(this.mCanvas,
                        this.automaton);
        setActivity(Activity.ActivityType.ACTIVITY_REGEXP_BREAKDOWN);

        stage.setTitle(WINDOW_TITLE);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Called by the {@link ListView} component to perform a
     * {@link CommandHistory} seek when a list item is selected.
     * 
     * @param idx
     *            the historyIdx to seek to
     */
    protected void setHistoryIdx(Number value) {
        int idx = (int) value;
        // Note: called with value -1 when the history is reset.
        if (idx >= 0) {
            this.currentActivity.history.seekIdx(idx);
            this.historyList.getSelectionModel().select(idx);
        }
    }

    protected static void exitApplication() {
        // TODO Confirm exit (unsaved changes, etc.)
        System.out.println("Exiting application...");
        System.exit(0);
    }

    protected void setActivity(Activity.ActivityType actType) {
        if (actType == null) {
            throw new IllegalArgumentException();
        }

        /* Observe only the current CommandHistory */
        if (this.currentActivity != null) {
            this.currentActivity.history.deleteObserver(this);
        }
        this.currentActivity = this.activities[actType.ordinal()];
        this.currentActivity.history.addObserver(this);

        /* Update history list */
        this.historyList.getItems().clear();
        for (int i = 0; i <= this.currentActivity.history
                .getHistorySize(); i++) {
            // 1 extra, so that "Step 0" is the inital state
            this.historyList.getItems().add("Step " + i);
            this.historyList.getSelectionModel().select(i);
        }

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
            this.historyList.getItems().clear();
            this.historyList.getItems().add("Step 0");
            this.historyList.getSelectionModel().select(0);
            this.currentActivity.history.clear();
            this.currentActivity.onEnteredRegexp(text);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        /* Called whenever CommandHistory of current Activity changes */
        if (arg instanceof Integer) {
            int idx = (int) arg;
            ObservableList<String> items = this.historyList.getItems();
            if (items.size() <= idx) {
                items.add("Step " + idx);
            }
            this.historyList.getSelectionModel().select(idx);
        }
    }

}
