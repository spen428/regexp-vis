package controller;

import java.util.List;
import java.util.Set;

import view.GraphCanvasEvent;
import view.GraphCanvasFX;
import view.GraphNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import model.Automaton;
import model.AutomatonState;
import model.Command;
import model.CommandHistory;
import model.RemoveEpsilonTransitionsCommand;
import model.RemoveEpsilonTransitionsContext;
import model.RemoveEquivalentStatesCommand;
import model.TranslationTools;

/**
 *
 * @author sp611
 *
 */
public class NfaToDfaActivity extends Activity {

    private RemoveEpsilonTransitionsActivity removeEpsilonActivity;
    private RemoveNonDeterminismActivity removeNonDeterminismActivity;
    private Activity activeActivity;

    public NfaToDfaActivity(GraphCanvasFX canvas, Automaton automaton) {
        super(canvas, automaton);
        this.removeEpsilonActivity = new RemoveEpsilonTransitionsActivity(
                canvas, automaton, this.history);
        this.removeNonDeterminismActivity = new RemoveNonDeterminismActivity(
                canvas, automaton, this.history);
        this.activeActivity = removeEpsilonActivity;
        // TODO Auto-generated constructor stub
    }

    // Forward events to current sub-activity
    @Override
    public void onNodeClicked(GraphCanvasEvent event) {
        activeActivity.onNodeClicked(event);
    }

    @Override
    public void onEdgeClicked(GraphCanvasEvent event) {
        activeActivity.onEdgeClicked(event);
    }

    @Override
    public void onBackgroundClicked(GraphCanvasEvent event) {
        activeActivity.onBackgroundClicked(event);
    }

    @Override
    public void onContextMenuRequested(ContextMenuEvent event) {
        activeActivity.onContextMenuRequested(event);
    }

    @Override
    public void onHideContextMenu(MouseEvent event) {
        activeActivity.onHideContextMenu(event);
    }

    private void onRemoveEpsilonTransitionsDone() {
        // Called by RemoveEpsilonTransitionsActivity when the activity is
        // finished

    }

    private void onRemoveNonDeterminismDone() {
        // Called by RemoveNonDeterminismActivity when the activity is finished

    }

    @Override
    public void onEnteredRegexp(String text) {
        super.onEnteredRegexp(text);

        // Just break everything down in one go, don't add to the history
        List<Command> commands = TranslationTools
                .breakdownAllTransitions(this.automaton);
        while (commands != null) {
            for (Command cmd : commands) {
                UICommand uiCmd = UICommand.fromCommand(this.canvas, cmd);
                uiCmd.redo();
            }
            commands = TranslationTools.breakdownAllTransitions(this.automaton);
        }
    }

    public class RemoveEpsilonTransitionsActivity extends Activity {
        private ContextMenu contextMenu;
        private MenuItem itemShowReachable;
        private MenuItem itemRemoveOutgoing;
        private MenuItem itemRemoveEquivalent;
        private AutomatonState rightClickedState;

        public RemoveEpsilonTransitionsActivity(GraphCanvasFX canvas,
                Automaton automaton, CommandHistory history) {
            super(canvas, automaton, history);
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

        private RemoveEpsilonTransitionsContext ctx;
        private void createContextMenu() {
            if (contextMenu != null) {
                return;
            }
            // TODO: place creation of context somewhere else
            ctx = new RemoveEpsilonTransitionsContext(this.automaton);
            contextMenu = new ContextMenu();
            itemShowReachable = new MenuItem("Show reachable states");
            itemRemoveOutgoing = new MenuItem(
                    "Remove out-going epsilon transitions");
            itemRemoveEquivalent = new MenuItem("Remove equivalent states");

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

            contextMenu.getItems().addAll(itemShowReachable,
                    itemRemoveOutgoing, itemRemoveEquivalent);
        }

        private void onShowReachableStates(ActionEvent event) {
            Set<AutomatonState> set = TranslationTools
                    .calcEpsilonReachableStates(this.automaton,
                            this.rightClickedState);
            for (AutomatonState s : set) {
                GraphNode n = this.canvas.lookupNode(s.getId());
                this.canvas.setNodeBackgroundColour(n, Color.GREEN);
            }
        }

        private void onRemoveOutgoing(ActionEvent event) {
            // TODO: Just execute one command, like this for easier debugging
            List<Command> cc = new RemoveEpsilonTransitionsCommand(
                    this.automaton, this.rightClickedState).getCommands();
            for (Command cmd : cc) {
                super.executeNewCommand(cmd);
            }
        }

        private void onRemoveEquivalent(ActionEvent event) {
            // TODO: Just execute one command, like this for easier debugging
            List<Command> cc2 = new RemoveEquivalentStatesCommand(
                    this.automaton, ctx, this.rightClickedState).getCommands();
            for (Command cmd : cc2) {
                super.executeNewCommand(cmd);
            }
        }

        @Override
        public void onContextMenuRequested(ContextMenuEvent event) {
            createContextMenu();
            // Query which node the context menu hit
            GraphNode nodeHit = this.canvas.findNodeHit(event.getX(),
                    event.getY());
            if (nodeHit == null) {
                this.rightClickedState = null;
            } else {
                this.rightClickedState = this.automaton.getStateById(nodeHit
                        .getId());
            }

            // Enable all context menu items first, then disable which ones
            // don't apply here
            itemShowReachable.setDisable(false);
            itemRemoveOutgoing.setDisable(false);
            itemRemoveEquivalent.setDisable(false);

            if (this.rightClickedState == null) {
                // Didn't right click on a state, no items applicable
                itemShowReachable.setDisable(true);
                itemRemoveOutgoing.setDisable(true);
                itemRemoveEquivalent.setDisable(true);
            } else {
                // Can only remove out-going epsilon transitions if there are
                // actually ones to remove, so disable the menu item if this is
                // the case
                boolean canRemoveOutgoing = TranslationTools
                        .stateHasEpsilonTransitions(this.automaton,
                                this.rightClickedState);
                itemRemoveOutgoing.setDisable(!canRemoveOutgoing);

                // Can only remove other equivalent states if we don't have any
                // out-going epsilon transitions, and some equivalent states do
                // actually exist
                boolean canRemoveEquivalent = !canRemoveOutgoing
                        && this.ctx.equivalentStatesExist(this.automaton,
                                this.rightClickedState);
                itemRemoveEquivalent.setDisable(!canRemoveEquivalent);
            }

            contextMenu.show(this.canvas, event.getScreenX(),
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

    public class RemoveNonDeterminismActivity extends Activity {

        public RemoveNonDeterminismActivity(GraphCanvasFX canvas,
                Automaton automaton, CommandHistory history) {
            super(canvas, automaton, history);
            // TODO Auto-generated constructor stub
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

        }

        @Override
        public void onHideContextMenu(MouseEvent event) {

        }

    }

}
