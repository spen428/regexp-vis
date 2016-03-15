package controller;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import model.Automaton;
import model.AutomatonState;
import model.RemoveEpsilonTransitionsCommand;
import model.RemoveEpsilonTransitionsContext;
import model.RemoveEquivalentStatesCommand;
import model.RemoveNonDeterminismCommand;
import model.RemoveNonDeterminismContext;
import model.RemoveStateCleanlyCommand;
import model.TranslationTools;
import view.GraphCanvasEvent;
import view.GraphCanvasFX;
import view.GraphNode;

/**
 *
 * @author sp611
 *
 */
public class NfaToDfaActivity extends Activity {

    private RemoveEpsilonTransitionsActivity removeEpsilonActivity;
    private RemoveNonDeterminismActivity removeNonDeterminismActivity;
    private ISubActivity subActivity;

    public NfaToDfaActivity(GraphCanvasFX canvas, Automaton automaton) {
        super(canvas, automaton);
        this.removeEpsilonActivity = new RemoveEpsilonTransitionsActivity();
        this.removeNonDeterminismActivity = new RemoveNonDeterminismActivity();
        this.subActivity = removeEpsilonActivity;
    }

    /**
     * Helper method. After the various methods to create an automaton to start
     * with have been handled, we want to have some logic handling actually
     * initiating the activity.
     */
    private void initiateActivity() {
        removeEpsilonActivity.recreateContext();

        // Check if nothing needs to be done, fast-track to removal of
        // non-determinism
        if (removeEpsilonActivity.checkActivityDone()) {
            subActivity = removeNonDeterminismActivity;
            removeNonDeterminismActivity.recreateContext();

            if (this.canvas.getNumNodes() == 0) {
                // There are no nodes, therefore nothing is on the screen, don't
                // show any message to the user.
                return;
            }

            // Now check if nothing still needs to be done, in which case
            // inform the user that nothing needs to be done
            if (removeNonDeterminismActivity.checkActivityDone()) {
                new Alert(
                        AlertType.INFORMATION,
                        "This automaton is already an DFA.")
                        .showAndWait();
            } else {
                new Alert(
                        AlertType.INFORMATION,
                        "This automaton already has no epsilon transitions, but has some non-determinism.")
                        .showAndWait();
            }
        }
    }

    // Forward events to current sub-activity

    @Override
    public void onStarted() {
        // Start off removing epsilon transitions
        subActivity = removeEpsilonActivity;
        ensureNotHybridAutomaton(this.automaton, this.canvas);
        initiateActivity();
    }

    @Override
    public void onGraphFileImport(GraphExportFile file) {
        // Start off removing epsilon transitions
        subActivity = removeEpsilonActivity;
        super.onGraphFileImport(file);
        ensureNotHybridAutomaton(this.automaton, this.canvas);
        initiateActivity();
    }

    private void resetNodeHighlighting() {
        Iterator<GraphCanvasFX.NodeEdgePair> it = this.canvas.graphIterator();
        while (it.hasNext()) {
            GraphCanvasFX.NodeEdgePair pair = it.next();
            GraphNode n = pair.getNode();
            this.canvas.setNodeBackgroundColour(n,
                    GraphCanvasFX.DEFAULT_NODE_BACKGROUND_COLOUR);
        }
    }

    @Override
    public void onHistoryChanged(Object obj) {
        super.onHistoryChanged(obj);

        // Reset any node highlighting
        resetNodeHighlighting();
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
        subActivity.onContextMenuRequested(event);
    }

    @Override
    public void onHideContextMenu(MouseEvent event) {
        subActivity.onHideContextMenu(event);
    }

    private void onRemoveEpsilonTransitionsDone() {
        // Called by RemoveEpsilonTransitionsActivity when the activity is
        // finished
        historyClear();
        this.subActivity = removeNonDeterminismActivity;
        removeNonDeterminismActivity.recreateContext();
        if (removeNonDeterminismActivity.checkActivityDone()) {
            // No non-determinism
            new Alert(
                    AlertType.INFORMATION,
                    "You have finished removing all epsilon transitions, "
                    + "equivalent states, and unreachable states. The result is"
                    + " already a DFA.")
                    .showAndWait();
        } else {
            new Alert(
                AlertType.INFORMATION,
                "You have finished removing all epsilon transitions, equivalent "
                + "states, and unreachable states.")
                .showAndWait();
        }


    }

    private void onRemoveNonDeterminismDone() {
        // Called by RemoveNonDeterminismActivity when the activity is finished
        new Alert(
                AlertType.INFORMATION,
                "You have finished translating the NFA to a DFA.")
                .showAndWait();
    }

    @Override
    public void onEnteredRegexp(String text) {
        // Start off removing epsilon transitions
        subActivity = removeEpsilonActivity;
        super.onEnteredRegexp(text);
        ensureNotHybridAutomaton(this.automaton, this.canvas);
        initiateActivity();
    }

    /**
     * Interface for sub-activities of this Activity, which aren't managed by
     * the regular Activity system in RegexpVisApp. This makes more sense
     * semantically than inheriting from Activity.
     */
    private interface ISubActivity {
        void recreateContext();
        boolean checkActivityDone();
        void onContextMenuRequested(ContextMenuEvent event);
        void onHideContextMenu(MouseEvent event);
    }

    private class RemoveEpsilonTransitionsActivity implements ISubActivity {
        private ContextMenu contextMenu;
        private MenuItem itemShowReachable;
        private MenuItem itemRemoveOutgoing;
        private MenuItem itemRemoveEquivalent;
        private MenuItem itemRemoveUnreachable;
        /**
         * The state that has been right-clicked we we open up a context menu.
         */
        private AutomatonState rightClickedState;
        /**
         * Context containing information needed for the translation
         */
        private RemoveEpsilonTransitionsContext ctx;

        /**
         * Colour used for highlighting states in the epsilon closure of a
         * state.
         */
        public final Color REACHABLE_STATE_BACKGROUND_COLOUR = Color.LIGHTGREEN;

        public void recreateContext() {
            // Called by NfaToDfaActivity
            ctx = new RemoveEpsilonTransitionsContext(automaton);
        }

        /**
         * Create the context menu and its menu items.
         */
        private void createContextMenu() {
            if (contextMenu != null) {
                return;
            }

            contextMenu = new ContextMenu();
            itemShowReachable = new MenuItem("Show epsilon closure");
            itemRemoveOutgoing = new MenuItem(
                    "Remove out-going epsilon transitions");
            itemRemoveEquivalent = new MenuItem("Remove equivalent states");
            itemRemoveUnreachable = new MenuItem("Remove unreachable state");

            itemShowReachable.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    onShowReachableStates(event);
                }
            });
            itemRemoveOutgoing.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    onRemoveOutgoing(event);
                }
            });
            itemRemoveEquivalent.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    onRemoveEquivalent(event);
                }
            });
            itemRemoveUnreachable.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    onRemoveUnreachable(event);
                }
            });

            contextMenu.getItems().addAll(itemShowReachable,
                    itemRemoveOutgoing, itemRemoveEquivalent,
                    itemRemoveUnreachable);
        }

        /**
         * Checks whether this activity is done. That is when:
         * <ol>
         *   <li> There are no epsilon transitions
         *   <li> There are no equivalent states (states that had the same epsilon
         *      closure)
         *   <li> There are no unreachable states
         * </ol>
         * @return True if this activity is done, false otherwise
         */
        public boolean checkActivityDone() {
            return !TranslationTools
                    .automatonHasEpsilonTransitions(automaton)
                    && !ctx.equivalentStatesExist(automaton)
                    && TranslationTools.automatonCalcUnreachableStates(
                            automaton).isEmpty();

        }

        private void onShowReachableStates(ActionEvent event) {
            // Reset highlighting otherwise results would be wrong
            resetNodeHighlighting();

            Set<AutomatonState> set = TranslationTools
                    .calcEpsilonReachableStates(automaton,
                            this.rightClickedState);
            for (AutomatonState s : set) {
                GraphNode n = canvas.lookupNode(s.getId());
                canvas.setNodeBackgroundColour(n,
                        REACHABLE_STATE_BACKGROUND_COLOUR);
            }
        }

        private void onRemoveOutgoing(ActionEvent event) {
            RemoveEpsilonTransitionsCommand cmd = new RemoveEpsilonTransitionsCommand(
                    automaton, this.rightClickedState);
            RemoveEpsilonTransitionsUICommand uiCmd = new RemoveEpsilonTransitionsUICommand(
                    canvas, cmd);
            executeNewUICommand(uiCmd);

            // Transfers to removing non-determinism if we are finished
            if (checkActivityDone()) {
                onRemoveEpsilonTransitionsDone();
            }
        }

        private void onRemoveEquivalent(ActionEvent event) {
            RemoveEquivalentStatesCommand cmd = new RemoveEquivalentStatesCommand(
                    automaton, this.ctx, this.rightClickedState);
            RemoveEquivalentStatesUICommand uiCmd = new RemoveEquivalentStatesUICommand(
                    canvas, cmd);
            executeNewUICommand(uiCmd);

            // Transfers to removing non-determinism if we are finished
            if (checkActivityDone()) {
                onRemoveEpsilonTransitionsDone();
            }
        }

        private void onRemoveUnreachable(ActionEvent event) {
            RemoveStateCleanlyCommand cmd = new RemoveStateCleanlyCommand(
                    automaton, this.rightClickedState);
            RemoveUnreachableStateUICommand uiCmd = new RemoveUnreachableStateUICommand(
                    canvas, cmd);
            executeNewUICommand(uiCmd);

            // Transfers to removing non-determinism if we are finished
            if (checkActivityDone()) {
                onRemoveEpsilonTransitionsDone();
            }
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
            itemShowReachable.setDisable(false);
            itemRemoveOutgoing.setDisable(false);
            itemRemoveEquivalent.setDisable(false);
            itemRemoveUnreachable.setDisable(false);

            if (this.rightClickedState == null) {
                // Didn't right click on a state, no items applicable
                itemShowReachable.setDisable(true);
                itemRemoveOutgoing.setDisable(true);
                itemRemoveEquivalent.setDisable(true);
                itemRemoveUnreachable.setDisable(true);
            } else {
                // Can only remove out-going epsilon transitions if there are
                // actually ones to remove, so disable the menu item if this is
                // the case
                boolean canRemoveOutgoing = TranslationTools
                        .stateHasEpsilonTransitions(automaton,
                                this.rightClickedState);
                itemRemoveOutgoing.setDisable(!canRemoveOutgoing);

                // Can only remove other equivalent states if we don't have any
                // out-going epsilon transitions, and some equivalent states do
                // actually exist
                boolean canRemoveEquivalent = !canRemoveOutgoing
                        && this.ctx.equivalentStatesExist(automaton,
                                this.rightClickedState);
                itemRemoveEquivalent.setDisable(!canRemoveEquivalent);

                // IDEA(mjn33): cache this result?
                boolean canRemoveUnreachable = TranslationTools
                        .automatonCalcUnreachableStates(automaton)
                        .contains(this.rightClickedState);
                itemRemoveUnreachable.setDisable(!canRemoveUnreachable);

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

    private class RemoveNonDeterminismActivity implements ISubActivity {
        private ContextMenu contextMenu;
        private MenuItem itemRemoveUnreachable;
        /**
         * The state that has been right-clicked we we open up a context menu.
         */
        private AutomatonState rightClickedState;
        /**
         * Context containing information needed for the translation
         */
        private RemoveNonDeterminismContext ctx;

        public void recreateContext() {
            ctx = new RemoveNonDeterminismContext(automaton);
        }

        private void createContextMenu() {
            if (contextMenu != null) {
                return;
            }

            contextMenu = new ContextMenu();
        }

        /**
         * Update the context menu items, based on the right-clicked state
         */
        private void updateContextMenuItems() {
            contextMenu.getItems().clear();

            if (this.rightClickedState == null) {
                MenuItem noStateItem = new MenuItem(
                        "No state right-clicked");
                contextMenu.getItems().add(noStateItem);
                return;
            }

            itemRemoveUnreachable = new MenuItem("Remove unreachable state");
            itemRemoveUnreachable.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    onRemoveUnreachable(event);
                }
            });
            contextMenu.getItems().add(itemRemoveUnreachable);
            boolean canRemoveUnreachable = TranslationTools
                    .automatonCalcUnreachableStates(automaton)
                    .contains(this.rightClickedState);
            itemRemoveUnreachable.setDisable(!canRemoveUnreachable);

            List<Character> charList = TranslationTools
                    .calcNonDeterministicTrans(automaton,
                            this.rightClickedState);

            // This item informs the user that there is no non-determinism for
            // this state
            if (charList.isEmpty()) {
                MenuItem noNonDeterminismItem = new MenuItem(
                        "No non-determinism out-going from this state");
                contextMenu.getItems().add(noNonDeterminismItem);
                return;
            }

            // Remove non-determinism menu item for each non-deterministic
            // character transition
            for (char c : charList) {
                MenuItem removeItem = new MenuItem(
                        "Remove non-determinism in '" + c + "' transitions");
                removeItem.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {
                        onRemoveNonDeterminism(event, c);
                    }
                });
                contextMenu.getItems().add(removeItem);
            }
        }

        /**
         * Checks whether this activity is done. That is when:
         * <ol>
         *   <li> For all states, all transitions are deterministic (i.e. all
         *        non-determinism has been removed)
         *   <li> There are no unreachable states
         * </ol>
         * @return True if this activity is done, false otherwise
         */
        public boolean checkActivityDone() {
            return TranslationTools.automatonCalcUnreachableStates(
                    automaton).isEmpty()
                    && !TranslationTools.automatonHasNonDeterminism(automaton);
        }

        private void onRemoveNonDeterminism(ActionEvent event, char c) {
            RemoveNonDeterminismCommand cmd = new RemoveNonDeterminismCommand(
                    this.ctx, this.rightClickedState, c);
            RemoveNonDeterminismUICommand uiCmd = new RemoveNonDeterminismUICommand(
                    canvas, cmd);
            executeNewUICommand(uiCmd);

            if (checkActivityDone()) {
                onRemoveNonDeterminismDone();
            }
        }

        private void onRemoveUnreachable(ActionEvent event) {
            RemoveStateCleanlyCommand cmd = new RemoveStateCleanlyCommand(
                    automaton, this.rightClickedState);
            RemoveUnreachableStateUICommand uiCmd = new RemoveUnreachableStateUICommand(
                    canvas, cmd);
            executeNewUICommand(uiCmd);

            if (checkActivityDone()) {
                onRemoveNonDeterminismDone();
            }
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
            // Update menu items now we have found the node hit
            updateContextMenuItems();

            contextMenu.show(canvas, event.getScreenX(), event.getScreenY());
            event.consume();
        }

        @Override
        public void onHideContextMenu(MouseEvent event) {
            if (contextMenu != null) {
                contextMenu.hide();
            }
        }

    }

}
