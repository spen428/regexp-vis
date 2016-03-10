package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Automaton;
import model.Command;
import model.CommandHistory;
import view.GraphCanvasEvent;
import view.GraphCanvasFX;

public class RegexpVisApp implements Observer {

    private static final Logger LOGGER = Logger.getLogger("controller");

    /* Finals */
    final CheckMenuItem[] activityMenuItems;
    private final Activity[] activities;
    private final Automaton automaton;
    protected final GraphCanvasFX mCanvas;
    final HistoryListView historyList;
    final Stage stage;

    /* Constants */
    private static final String HISTORY_INITIAL_STATE_TEXT = "Initial state";
    private static final String CONTROL_PANEL_HIDE_TEXT = "Hide Control Panel";
    private static final String CONTROL_PANEL_SHOW_TEXT = "Show Control Panel";
    private static final String HISTORY_LIST_HIDE_TEXT = "Hide History List";
    private static final String HISTORY_LIST_SHOW_TEXT = "Show History List";
    private static final String TEXTFIELD_PROMPT = "Type a regular expression and press Enter.";
    private static final int BUTTON_PANEL_PADDING_PX = 10;
    private static final int CONTROL_PANEL_PADDING_HORIZONTAL_PX = 35;
    private static final int CONTROL_PANEL_PADDING_VERTICAL_PX = 20;
    protected static final String ABOUT_HEADER = "Regular Language Visualiser"
            + " (v" + Main.VERSION + ")";
    protected static final String ABOUT_CONTENT = "Authors:\n\n"
            + "William Dix\t\t\t<wrd2@kent.ac.uk>\n"
            + "Parham Ghassemi\t<pg272@kent.ac.uk>\n"
            + "Matthew Nicholls\t\t<mjn33@kent.ac.uk>\n"
            + "Samuel Pengelly\t\t<sp611@kent.ac.uk>\n";

    public static final String WINDOW_TITLE = Main.TITLE + " v" + Main.VERSION;
    private static final String AUTOMATON_GRAPH_FILE_EXT = ".txt";

    /* Variables */
    protected Activity currentActivity;
    protected boolean enterKeyDown;

    public RegexpVisApp(Stage stage) {
        final VBox root = new VBox();
        final SplitPane canvasContainer = new SplitPane();
        final VBox controlPanel = new VBox();
        this.stage = stage;

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

        MenuItem menuFileImport = new MenuItem("Import Graph...");
        menuFileImport.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                onImportGraph(event);
            }

        });
        MenuItem menuFileExport = new MenuItem("Export Graph...");
        menuFileExport.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                onExportGraph(event);
            }

        });
        menuFile.getItems().addAll(menuFileImport, menuFileExport, exit);

        // --- Menu Edit
        Menu menuEdit = new Menu("Edit");
        MenuItem menuEditUndo = new MenuItem("Undo");
        menuEditUndo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                RegexpVisApp.this.currentActivity.historyPrev();
            }
        });
        MenuItem menuEditRedo = new MenuItem("Redo");
        menuEditRedo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                RegexpVisApp.this.currentActivity.historyNext();
            }
        });
        // final CheckMenuItem menuEditClobberHistory = new CheckMenuItem(
        // "Clobber History");
        // menuEditClobberHistory.setSelected(CommandHistory.CLOBBER_BY_DEFAULT);
        // menuEditClobberHistory.setOnAction(new EventHandler<ActionEvent>() {
        // @Override
        // public void handle(ActionEvent t) {
        // boolean clob = RegexpVisApp.this.currentActivity.history
        // .isClobbered();
        // RegexpVisApp.this.currentActivity.history.setClobbered(!clob);
        // menuEditClobberHistory.setSelected(!clob);
        // }
        // });
        MenuItem menuEditPreferences = new MenuItem("Preferences...");
        menuEdit.getItems().addAll(menuEditUndo, menuEditRedo,
                // menuEditClobberHistory,
                menuEditPreferences);

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
        HBox.setHgrow(this.mCanvas, javafx.scene.layout.Priority.ALWAYS);
        canvasContainer.getItems().add(this.mCanvas);

        // History list also part of the canvas container
        this.historyList = new HistoryListView();
        SplitPane.setResizableWithParent(this.historyList, Boolean.FALSE);
        this.historyList.getSelectionModel().selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(
                            ObservableValue<? extends Number> observable,
                            Number oldValue, Number newValue) {
                        RegexpVisApp.this.setHistoryIdx(newValue);
                    }
                });
        canvasContainer.getItems().add(this.historyList);
        canvasContainer.setDividerPosition(0, 0.8f);

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
        Button buttonLoad = new Button("Import");
        buttonLoad.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onImportGraph(event);
            }
        });
        Button buttonSave = new Button("Export");
        buttonSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onExportGraph(event);
            }
        });
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
        this.mCanvas.setOnNodeClicked(new EventHandler<GraphCanvasEvent>() {
            @Override
            public void handle(GraphCanvasEvent event) {
                onNodeClicked(event);
            }
        });
        this.mCanvas.setOnEdgeClicked(new EventHandler<GraphCanvasEvent>() {
            @Override
            public void handle(GraphCanvasEvent event) {
                onEdgeClicked(event);
            }
        });
        this.mCanvas
                .setOnBackgroundClicked(new EventHandler<GraphCanvasEvent>() {
                    @Override
                    public void handle(GraphCanvasEvent event) {
                        onBackgroundClicked(event);
                    }
                });

        this.mCanvas.setOnCreatedEdge(new EventHandler<GraphCanvasEvent>() {
            @Override
            public void handle(GraphCanvasEvent event) {
                onCreatedEdge(event);
            }

        });

        this.mCanvas.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED,
                new EventHandler<ContextMenuEvent>() {
                    @Override
                    public void handle(ContextMenuEvent event) {
                        onContextMenuRequested(event);
                    }
                });

        this.mCanvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        onHideContextMenu(event);
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
            this.currentActivity.historySeek(idx);
            this.historyList.getSelectionModel().select(idx);
        }
    }

    protected static void exitApplication() {
        // TODO Confirm exit (unsaved changes, etc.)
        LOGGER.log(Level.FINE, "Exiting application...");
        System.exit(0);
    }

    protected void setActivity(Activity.ActivityType actType) {
        if (actType == null) {
            throw new IllegalArgumentException();
        }

        /*
         * Set "checked" status of all menu items to reflect the change of
         * activity
         */
        for (int i = 0; i < this.activityMenuItems.length; i++) {
            this.activityMenuItems[i].setSelected(i == actType.ordinal());
        }

        Activity newActivity = this.activities[actType.ordinal()];
        if (this.currentActivity == newActivity) {
            // Don't do anything, current activity hasn't actually changed
            return;
        }
        if (this.currentActivity != null
                && !this.currentActivity.onPreStarted()) {
            // Can't switch to this activity, abort
            return;
        }

        /* Observe only the current CommandHistory */
        if (this.currentActivity != null) {
            this.currentActivity.history.deleteObserver(this);
            this.currentActivity.onEnded();
        }
        this.currentActivity = newActivity;
        this.currentActivity.history.addObserver(this);

        /* Update history list */
        this.historyList.getItems().clear();
        List<Command> cmds = this.currentActivity.history.getCommands();
        for (int i = 0; i <= cmds.size(); i++) {
            String text;
            if (i == 0) {
                text = HISTORY_INITIAL_STATE_TEXT;
            } else {
                if (cmds.get(i - 1) instanceof UICommand) {
                    text = ((UICommand) cmds.get(i - 1)).getDescription();
                } else {
                    text = "Step " + i;
                }
            }
            this.historyList.addItem(text);
            this.historyList.getSelectionModel().select(i);
        }

        this.currentActivity.onStarted();
    }

    private void onNodeClicked(GraphCanvasEvent event) {
        if (this.currentActivity != null) {
            this.currentActivity.onNodeClicked(event);
        }
    }

    private void onEdgeClicked(GraphCanvasEvent event) {
        if (this.currentActivity != null) {
            this.currentActivity.onEdgeClicked(event);
        }
    }

    private void onBackgroundClicked(GraphCanvasEvent event) {
        if (this.currentActivity != null) {
            this.currentActivity.onBackgroundClicked(event);
        }
    }

    private void onCreatedEdge(GraphCanvasEvent event) {
        if (this.currentActivity != null) {
            this.currentActivity.onCreatedEdge(event);
        }
    }

    private void onContextMenuRequested(ContextMenuEvent event) {
        if (this.currentActivity != null) {
            this.currentActivity.onContextMenuRequested(event);
        }
    }

    private void onHideContextMenu(MouseEvent event) {
        if (this.currentActivity != null) {
            this.currentActivity.onHideContextMenu(event);
        }
    }

    private void onImportGraph(ActionEvent event) {
        // Based on the nice example snippet in the JavaFX documentation
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Automaton Graph File");
        // Choose .txt, our file format is text based as this just makes it
        // easier to edit with text editors.
        fileChooser.getExtensionFilters()
                .addAll(new FileChooser.ExtensionFilter("Automaton Graph Files",
                        "*" + AUTOMATON_GRAPH_FILE_EXT),
                new FileChooser.ExtensionFilter("All Files", "*"));

        File selectedFile = fileChooser.showOpenDialog(this.stage);
        if (selectedFile == null) {
            // No file selected, exit early
            return;
        }

        try {
            GraphExportFile f = new GraphExportFile(selectedFile);
            if (this.currentActivity != null) {
                this.currentActivity.onGraphFileImport(f);
            }
        } catch (BadGraphExportFileException e) {
            new Alert(AlertType.ERROR,
                    "Failed to load file, file isn't a valid automaton graph file.")
                            .showAndWait();
        } catch (FileNotFoundException e) {
            new Alert(AlertType.ERROR,
                    "Failed to load file, could not find file: "
                            + selectedFile.getPath()).showAndWait();
        } catch (IOException e) {
            new Alert(AlertType.ERROR,
                    "Failed to load file, unexpected I/O error.").showAndWait();
        }
    }

    private void onExportGraph(ActionEvent event) {
        // Based on the nice example snippet in the JavaFX documentation
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Automaton Graph File");
        // Choose .txt, our file format is text based as this just makes it
        // easier to edit with text editors.
        fileChooser.getExtensionFilters()
                .addAll(new FileChooser.ExtensionFilter("Automaton Graph Files",
                        "*" + AUTOMATON_GRAPH_FILE_EXT),
                new FileChooser.ExtensionFilter("All Files", "*"));

        File selectedFile = fileChooser.showSaveDialog(this.stage);
        if (selectedFile == null) {
            // No file selected, exit early
            return;
        }

        // Add file extension if it was not specified
        if (!selectedFile.getAbsolutePath()
                .endsWith(AUTOMATON_GRAPH_FILE_EXT)) {
            selectedFile = new File(
                    selectedFile.getAbsolutePath() + AUTOMATON_GRAPH_FILE_EXT);
        }

        try {
            GraphExportFile f = new GraphExportFile(automaton, mCanvas);
            f.writeFile(selectedFile);
        } catch (IOException e) {
            new Alert(AlertType.ERROR,
                    "Failed to save file, unexpected I/O error.").showAndWait();
        }
    }

    private void onEnteredRegexp(String text) {
        if (this.currentActivity != null) {
            this.currentActivity.onEnteredRegexp(text);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        /* Called whenever CommandHistory of current Activity changes */

        if (this.currentActivity != null) {
            this.currentActivity.onHistoryChanged(arg);
        }

        ObservableList<Label> items = this.historyList.getItems();
        if (arg instanceof Integer) {
            int idx = (int) arg;
            if (idx == CommandHistory.HISTORY_CLOBBERED) {
                /*
                 * The last element of the history list was removed, we must
                 * reflect this change in the UI.
                 */
                items.remove(items.size() - 1);
            } else if (idx == CommandHistory.HISTORY_CLEARED) {
                this.historyList.getItems().clear();
                this.historyList.addItem(HISTORY_INITIAL_STATE_TEXT);
                this.historyList.getSelectionModel().select(0);
            } else {
                this.historyList.getSelectionModel().select(idx);
            }
        } else if (arg instanceof UICommand) {
            String text = ((UICommand) arg).getDescription();
            this.historyList.addItem(text);
            this.historyList.getSelectionModel().select(items.size() - 1);
        }
        this.historyList.scrollTo(items.size() - 1);
    }

}
