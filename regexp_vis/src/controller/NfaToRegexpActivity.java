package controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import view.GraphCanvasEvent;
import view.GraphCanvasFX;
import view.GraphNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import model.Automaton;
import model.AutomatonState;
import model.AutomatonTransition;
import model.Command;
import model.ConjoinParallelTransitionsCommand;
import model.IsolateFinalStateCommand;
import model.IsolateInitialStateCommand;
import model.RemoveLoopTransitionCommand;
import model.RemoveStateCleanlyCommand;
import model.SequenceStateTransitionsCommand;
import model.TranslationTools;

public class NfaToRegexpActivity extends Activity {
    private ContextMenu contextMenu;
    private MenuItem itemRemoveLoop;
    private MenuItem itemRemoveParallelTrans;
    private MenuItem itemRemoveAndSeq;
    /**
     * The state that has been right-clicked we we open up a context menu.
     */
    private AutomatonState rightClickedState;
    private AutomatonTransition finalTransition = null;
    /**
     * When trying to ensure the initial invariants, true if we find a final
     * state, false otherwise. We can't do much if there is no final state.
     */
    private boolean hasFinalState = true;

    public NfaToRegexpActivity(GraphCanvasFX canvas, Automaton automaton) {
        super(canvas, automaton);
    }

    private void ensureInvariants() {
        // Remove unreachable states first
        Set<AutomatonState> unreachable = TranslationTools
                .automatonCalcUnreachableStates(automaton);

        for (AutomatonState state : unreachable) {
            List<Command> cmds = new RemoveStateCleanlyCommand(automaton, state)
                    .getCommands();
            for (Command cmd : cmds) {
                UICommand uiCmd = UICommand.fromCommand(canvas, cmd);
                // Don't add to the history
                uiCmd.redo();
            }
        }

        hasFinalState = TranslationTools.automatonHasFinalState(automaton);
        if (!hasFinalState) {
            new Alert(AlertType.INFORMATION,
                    "The automaton doesn't have a final state, the language of "
                            + "the automaton is the empty set").showAndWait();
            return;
        }

        IsolateFinalStateCommand isolateFinalCmd = IsolateFinalStateCommand
                .create(automaton);
        if (isolateFinalCmd != null) {
            IsolateFinalStateUICommand isolateFinalUiCmd =
                    new IsolateFinalStateUICommand(canvas, isolateFinalCmd);
            // Don't add to the history
            isolateFinalUiCmd.redo();
        }

        IsolateInitialStateCommand isolateInitialCmd =
                IsolateInitialStateCommand.create(automaton);
        if (isolateInitialCmd != null) {
            IsolateInitialStateUICommand isolateInitialUiCmd =
                    new IsolateInitialStateUICommand(canvas, isolateInitialCmd);
            // Don't add to the history
            isolateInitialUiCmd.redo();
        }
    }

    private void initiateActivity() {
        if (checkActivityDone()) {
            onActivityDone();
        }
    }

    private void onActivityDone() {
        new Alert(AlertType.INFORMATION,
                "A regular expression which describes the language of this "
                        + "automaton is: "
                        + finalTransition.getData().toString())
                .showAndWait();
    }

    /**
     * Checks whether this activity is done. That is when:
     * <ol>
     *   <li> There are only two states
     *   <li> There is only one transition from the initial state to the final
     *   state
     * </ol>
     *
     * Also sets finalTransition which is the single transition (null if not
     * applicable).
     *
     * @return True if this activity is done, false otherwise
     */
    private boolean checkActivityDone() {
        finalTransition = null;

        int numStates = automaton.getNumStates();
        ArrayList<AutomatonTransition> trans = new ArrayList<>();
        Iterator<Automaton.StateTransitionsPair> it = automaton.graphIterator();
        while (it.hasNext()) {
            Automaton.StateTransitionsPair pair = it.next();
            trans.addAll(pair.getTransitions());
        }

        if (numStates > 2) {
            // Only initial and final should remain
            return false;
        }

        if (trans.size() != 1) {
            // More than one transition in the automaton
            return false;
        }

        // Verify the transition is from the start state, to the other state
        AutomatonTransition t = trans.get(0);
        if (t.getFrom() != automaton.getStartState() ||
            t.getTo() == automaton.getStartState()) {
            return false;
        }

        finalTransition = t;
        return true;
    }

    @Override
    public void onGraphFileImport(GraphExportFile file) {
        super.onGraphFileImport(file);
        ensureInvariants();
        initiateActivity();
    }

    @Override
    public void onStarted() {
        ensureInvariants();
        initiateActivity();
    }

    /**
     * Create the context menu and its menu items.
     */
    private void createContextMenu() {
        if (contextMenu != null) {
            return;
        }

        contextMenu = new ContextMenu();
        itemRemoveLoop = new MenuItem("Remove loop");
        itemRemoveParallelTrans = new MenuItem(
                "Remove parallel transitions");
        itemRemoveAndSeq = new MenuItem("Remove state, sequence transitions");

        itemRemoveLoop.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                onRemoveLoop(event);
            }
        });
        itemRemoveParallelTrans.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                onRemoveParallelTrans(event);
            }
        });
        itemRemoveAndSeq.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                onRemoveState(event);
            }
        });

        contextMenu.getItems().addAll(itemRemoveLoop, itemRemoveParallelTrans,
                itemRemoveAndSeq);
    }

    private void onRemoveLoop(ActionEvent event) {
        AutomatonTransition trans = TranslationTools.getSingleLoop(automaton,
                this.rightClickedState);

        RemoveLoopTransitionCommand cmd =
                new RemoveLoopTransitionCommand(automaton, trans);
        RemoveLoopTransitionUICommand uiCmd =
                new RemoveLoopTransitionUICommand(canvas, cmd);

        super.executeNewUICommand(uiCmd);

        if (checkActivityDone()) {
            onActivityDone();
        }
    }

    private void onRemoveParallelTrans(ActionEvent event) {
        GraphNode n = canvas.lookupNode(this.rightClickedState.getId());
        canvas.startCreateEdgeMode(n);
    }

    @Override
    public void onCreatedEdge(GraphCanvasEvent event) {
        AutomatonState from = automaton.getStateById(event.getTargetEdge()
                .getFrom().getId());
        AutomatonState to = automaton.getStateById(event.getTargetEdge()
                .getTo().getId());

        // The user may have selected a pair of nodes which don't have parallel
        // transitions.
        boolean hasParallelTrans = TranslationTools.stateHasParallelTrans(
                automaton, from, to);

        if (!hasParallelTrans) {
            new Alert(AlertType.INFORMATION,
                    "No parallel transitions exist from the given source and "
                            + "target nodes.").showAndWait();
            return;
        }

        ConjoinParallelTransitionsCommand cmd =
                new ConjoinParallelTransitionsCommand(automaton, from, to);
        ConjoinParallelTransitionsUICommand uiCmd =
                new ConjoinParallelTransitionsUICommand(canvas, cmd);

        super.executeNewUICommand(uiCmd);

        if (checkActivityDone()) {
            onActivityDone();
        }
    }

    private void onRemoveState(ActionEvent event) {
        SequenceStateTransitionsCommand cmd =
                new SequenceStateTransitionsCommand(automaton,
                        this.rightClickedState);
        SequenceStateTransitionsUICommand uiCmd =
                new SequenceStateTransitionsUICommand(canvas, cmd);

        super.executeNewUICommand(uiCmd);

        if (checkActivityDone()) {
            onActivityDone();
        }
    }

    @Override
    public void onEnteredRegexp(String text) {
        super.onEnteredRegexp(text);
        // Need to fully breakdown everything, otherwise there would be nothing
        // to do initially
        ensureNotHybridAutomaton(this.automaton, this.canvas);
        ensureInvariants();
        initiateActivity();
    }

    @Override
    public void onNodeClicked(GraphCanvasEvent event) {

    }

    @Override
    public void onEdgeClicked(GraphCanvasEvent event) {

    }

    @Override
    public void onBackgroundClicked(GraphCanvasEvent event) {

    }

    @Override
    public void onContextMenuRequested(ContextMenuEvent event) {
        createContextMenu();
        // Query which node the context menu hit
        GraphNode nodeHit = canvas.findNodeHit(event.getX(),
                event.getY());
        if (nodeHit == null) {
            this.rightClickedState = null;
        } else {
            this.rightClickedState = automaton.getStateById(nodeHit
                    .getId());
        }

        // Enable all context menu items first, then disable which ones
        // don't apply here
        itemRemoveLoop.setDisable(false);
        itemRemoveParallelTrans.setDisable(false);
        itemRemoveAndSeq.setDisable(false);

        if (this.rightClickedState == null ||
                !this.hasFinalState) {
            // Didn't right click on a state, no items applicable
            // No final state exists, no point in doing anything
            itemRemoveLoop.setDisable(true);
            itemRemoveParallelTrans.setDisable(true);
            itemRemoveAndSeq.setDisable(true);
        } else {
            // Cannot remove multiple loops, need to
            // "remove parallel transitions" first
            boolean hasSingleLoop = TranslationTools.getSingleLoop(automaton,
                    this.rightClickedState) != null;
            itemRemoveLoop.setDisable(!hasSingleLoop);

            boolean hasParallelTrans = TranslationTools.stateHasParallelTrans(
                    automaton, this.rightClickedState, null);
            itemRemoveParallelTrans.setDisable(!hasParallelTrans);

            // Can't remove the initial state or final state, or if there are
            // any loops.
            boolean canRemove = !this.rightClickedState.isFinal()
                    && automaton.getStartState() != this.rightClickedState
                    && !TranslationTools.stateHasLoop(automaton,
                            this.rightClickedState);
            itemRemoveAndSeq.setDisable(!canRemove);

        }

        contextMenu.show(canvas, event.getScreenX(),
                event.getScreenY());
        event.consume();
    }

    @Override
    public void onHideContextMenu(MouseEvent event) {
        if (contextMenu != null) {
            contextMenu.hide();
        }
    }

}
