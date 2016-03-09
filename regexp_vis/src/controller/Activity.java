package controller;

import java.util.LinkedList;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import model.Automaton;
import model.AutomatonState;
import model.AutomatonTransition;
import model.BasicRegexp;
import model.Command;
import model.CommandHistory;
import model.InvalidRegexpException;
import view.GraphCanvasEvent;
import view.GraphCanvasFX;
import view.GraphEdge;
import view.GraphNode;

/**
 *
 * @author sp611
 *
 */
public abstract class Activity {

    enum ActivityType {
        ACTIVITY_REGEXP_BREAKDOWN("Breakdown Regular Expression to FSA"), ACTIVITY_NFA_TO_REGEXP(
                "Convert NFA to Regular Expression"), ACTIVITY_NFA_TO_DFA(
                "Convert NFA to DFA");

        private final String text;

        private ActivityType(String text) {
            this.text = text;
        }

        public String getText() {
            return this.text;
        }
    }

    protected final GraphCanvasFX canvas;
    protected final Automaton automaton;
    protected final CommandHistory history;

    Activity(GraphCanvasFX canvas, Automaton automaton) {
        super();
        this.canvas = canvas;
        this.automaton = automaton;
        this.history = new CommandHistory();
    }

    Activity(GraphCanvasFX canvas, Automaton automaton, CommandHistory history) {
        this.canvas = canvas;
        this.automaton = automaton;
        this.history = history;
    }

    /**
     * Called by RegexpVisApp after entering a regular expression.
     *
     * @param text
     *            The regexp the user entered
     */
    public void onEnteredRegexp(String text) {
        historyClear();

        System.out.printf("Entered regexp: %s%n", text);
        BasicRegexp re = null;
        try {
            re = BasicRegexp.parseRegexp(text);
            // BasicRegexp.debugPrintBasicRegexp(0, re);
        } catch (InvalidRegexpException e1) {
            Alert alert = new Alert(AlertType.ERROR,
                    "Error: invalid regexp entered. Details: \n\n"
                            + e1.getMessage());
            alert.showAndWait();
            return;
        }

        this.canvas.removeAllNodes();
        this.automaton.clear();
        // TODO: Add the following to history
        AutomatonState startState = this.automaton.getStartState();
        AutomatonState finalState = this.automaton.createNewState();
        AutomatonTransition trans = this.automaton.createNewTransition(
                startState, finalState, re);
        finalState.setFinal(true);
        this.automaton.addStateWithTransitions(finalState,
                new LinkedList<AutomatonTransition>());
        this.automaton.addTransition(trans);

        GraphNode startNode = this.canvas.addNode(startState.getId(), 50.0,
                50.0);
        this.canvas.setNodeUseStartStyle(startNode, true);
        GraphNode endNode = this.canvas.addNode(finalState.getId(),
                this.canvas.getWidth() - 50.0, this.canvas.getHeight() - 50.0);
        this.canvas.setNodeUseFinalStyle(endNode, true);
        GraphEdge edge = this.canvas.addEdge(trans.getId(), startNode, endNode,
                re.toString());
    }

    /**
     * Called by RegexpVisApp when we load a new graph from a file, the activity
     * can decide to do nothing, pre-process the file or just load file as
     * normal.
     *
     * @param file
     *            The graph export file we read from disk, not loaded yet
     */
    public void onGraphFileImport(GraphExportFile file) {
        this.canvas.removeAllNodes();
        this.automaton.clear();
        this.history.clear();
        file.loadFile(this.automaton, this.canvas);
    }

    /**
     * Called by RegexpVisApp when we are about to change from a another
     * activity to this one.
     *
     * @return true if we can start this activity, false otherwise in which case
     *         the switching of activities will be aborted.
     */
    public boolean onPreStarted() {
        return true;
    }

    /**
     * Called by RegexpVisApp when we have just changed to this activity
     */
    public void onStarted() {

    }

    /**
     * Called by RegexpVisApp when we have just changed away from this activity
     */
    public void onEnded() {

    }

    /**
     * Called by RegexpVisApp when the state of the CommandHistory changes.
     *
     * @param obj
     *            The object the observer got passed
     */
    public void onHistoryChanged(Object obj) {

    }

    public abstract void onNodeClicked(GraphCanvasEvent event);

    public abstract void onEdgeClicked(GraphCanvasEvent event);

    public abstract void onBackgroundClicked(GraphCanvasEvent event);

    public abstract void onContextMenuRequested(ContextMenuEvent event);

    public abstract void onHideContextMenu(MouseEvent event);

    protected void executeNewCommand(Command cmd) {
        UICommand uiCmd;
        if (cmd instanceof UICommand) {
            uiCmd = (UICommand) cmd;
        } else {
            uiCmd = UICommand.fromCommand(this.canvas, cmd);
        }

        if (uiCmd != null) {
            this.history.executeNewCommand(uiCmd);
        }
    }

    /**
     * @deprecated Use {@link #executeNewCommand(Command)} instead, there is no
     *             need to instantiate the {@link UICommand} yourself
     */
    protected void executeNewUICommand(UICommand cmd) {
        executeNewCommand(cmd);
    }

    // Expose CommandHistory methods, except for executeNewCommand()
    void historyPrev() {
        this.history.prev();
    }

    void historyNext() {
        this.history.next();
    }

    void historySeek(int idx) {
        this.history.seekIdx(idx);
    }

    void historyStart() {
        this.history.seekIdx(0);
    }

    void historyEnd() {
        this.history.seekIdx(this.history.getHistorySize());
    }

    void historyClear() {
        this.history.clear();
    }

}
