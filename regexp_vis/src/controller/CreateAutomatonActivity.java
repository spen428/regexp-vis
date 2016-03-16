package controller;

import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import model.AddStateCommand;
import model.AddTransitionCommand;
import model.Automaton;
import model.AutomatonState;
import model.AutomatonTransition;
import model.BasicRegexp;
import model.InvalidRegexpException;
import model.RemoveStateCleanlyCommand;
import model.RemoveTransitionCommand;
import model.SetIsFinalCommand;
import view.GraphCanvasEvent;
import view.GraphCanvasFX;
import view.GraphEdge;
import view.GraphNode;

public class CreateAutomatonActivity extends Activity {
    private ContextMenu contextMenu;
    private MenuItem itemAddState;
    private MenuItem itemRemoveState;
    private MenuItem itemAddTransition;
    private MenuItem itemRemoveTransition;
    private MenuItem itemToggleIsFinal;
    private AutomatonState rightClickedState;
    private AutomatonTransition rightClickedTransition;
    private Point2D rightClickedPoint;

    public CreateAutomatonActivity(GraphCanvasFX canvas, Automaton automaton) {
        super(canvas, automaton);
    }

    @Override
    public void onEnteredRegexp(String text) {
        super.onEnteredRegexp(text);
        ensureNotHybridAutomaton(this.automaton, this.canvas);
    }

    @Override
    public void onGraphFileImport(GraphExportFile file) {
        super.onGraphFileImport(file);
    }

    @Override
    public void onStarted() {
        // FIXME: when the canvas is blank, maybe automaton should = null?
        if (this.canvas.getNumNodes() == 0) {
            // Initial startup: the automaton will have just the initial state
            GraphNode n = this.canvas.addNode(this.automaton.getStartState()
                    .getId(), 100, 100);
            this.canvas.setNodeUseStartStyle(n, true);
        }
    }

    private void createContextMenu() {
        if (contextMenu != null) {
            return;
        }
        contextMenu = new ContextMenu();
    }

    /**
     * Update the context menu items, based what was right-clicked
     */
    private void updateContextMenuItems() {
        contextMenu.getItems().clear();

        if (this.rightClickedTransition != null) {
            itemRemoveTransition = new MenuItem("Remove transition");
            itemRemoveTransition.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    onRemoveTransition(event, rightClickedTransition);
                }
            });
            contextMenu.getItems().add(itemRemoveTransition);
        } else if (this.rightClickedState != null) {
            itemRemoveState = new MenuItem("Remove state");
            itemRemoveState.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    onRemoveState(event, rightClickedState);
                }
            });
            contextMenu.getItems().add(itemRemoveState);
            // Don't allow removal of the initial state
            if (this.rightClickedState == this.automaton.getStartState()) {
                itemRemoveState.setDisable(true);
            } else {
                itemRemoveState.setDisable(false);
            }

            itemAddTransition = new MenuItem("Add transition");
            itemAddTransition.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    onAddTransition(event, rightClickedState);
                }
            });
            contextMenu.getItems().add(itemAddTransition);

            String itemStr = this.rightClickedState.isFinal() ?
                    "Make state non-final" : "Make state final";
            itemToggleIsFinal = new MenuItem(itemStr);
            itemToggleIsFinal.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    onToggleIsFinal(event, rightClickedState);
                }
            });
            contextMenu.getItems().add(itemToggleIsFinal);
        } else {
            itemAddState = new MenuItem("Create new state");
            itemAddState.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    onAddState(event);
                }
            });
            contextMenu.getItems().add(itemAddState);
        }
    }

    private void onAddState(ActionEvent event) {
        AutomatonState newState = this.automaton.createNewState();
        AddStateCommand cmd = new AddStateCommand(this.automaton, newState);
        AddStateUICommand uiCmd = new AddStateUICommand(this.canvas, cmd,
                this.rightClickedPoint);
        super.executeNewUICommand(uiCmd);
    }

    private void onToggleIsFinal(ActionEvent event,
            AutomatonState state) {
        SetIsFinalCommand cmd = new SetIsFinalCommand(this.automaton, state,
                !state.isFinal());
        super.executeNewCommand(cmd);
    }

    private void onAddTransition(ActionEvent event,
            AutomatonState state) {
        GraphNode n = this.canvas.lookupNode(state.getId());
        this.canvas.startCreateEdgeMode(n);
    }

    private void onRemoveState(ActionEvent event,
            AutomatonState state) {
        RemoveStateCleanlyCommand cmd = new RemoveStateCleanlyCommand(
                this.automaton, state);
        RemoveStateCleanlyUICommand uiCmd = new RemoveStateCleanlyUICommand(
                this.canvas, cmd);
        super.executeNewUICommand(uiCmd);
    }

    private void onRemoveTransition(ActionEvent event,
            AutomatonTransition trans) {
        RemoveTransitionCommand cmd = new RemoveTransitionCommand(
                this.automaton, trans);
        RemoveTransitionUICommand uiCmd = new RemoveTransitionUICommand(
                this.canvas, cmd);
        super.executeNewUICommand(uiCmd);
    }

    @Override
    public void onCreatedEdge(GraphCanvasEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Enter transition regular expression:");
        Optional<String> optResult = dialog.showAndWait();
        if (!optResult.isPresent()) {
            return;
        }
        String result = optResult.get();
        // Replace percentage signs with epsilons, makes entering epsilons
        // easier. Similar to Stefan's Java applet.
        result = result.replaceAll("%",
                ((Character) BasicRegexp.EPSILON_CHAR).toString());
        BasicRegexp re = null;
        try {
            re = BasicRegexp.parseRegexp(result);
        } catch (InvalidRegexpException e) {
            Alert alert = new Alert(AlertType.ERROR,
                    "Error: invalid regexp entered. Details: \n\n"
                            + e.getMessage());
            alert.showAndWait();
            return;
        }

        AutomatonState stateFrom = this.automaton.getAutomatonStateById(event
                .getTargetEdge().getFrom().getId());
        AutomatonState stateTo = this.automaton.getAutomatonStateById(event
                .getTargetEdge().getTo().getId());

        AutomatonTransition newTrans = this.automaton.createNewTransition(
                stateFrom, stateTo, re);

        AddTransitionCommand cmd = new AddTransitionCommand(this.automaton,
                newTrans);
        AddTransitionUICommand uiCmd = new AddTransitionUICommand(this.canvas,
                cmd);
        super.executeNewUICommand(uiCmd);
    }

    @Override
    public void onContextMenuRequested(ContextMenuEvent event) {
        createContextMenu();
        this.rightClickedPoint = new Point2D(event.getX(), event.getY());
        // Query which node / edge the context menu hit
        GraphEdge edgeHit = canvas.findEdgeLabelHit(event.getX(), event.getY());
        GraphNode nodeHit = canvas.findNodeHit(event.getX(), event.getY());

        if (nodeHit == null) {
            this.rightClickedState = null;
        } else {
            this.rightClickedState = automaton.getStateById(nodeHit
                    .getId());
        }

        if (edgeHit == null) {
            this.rightClickedTransition = null;
        } else {
            this.rightClickedTransition = automaton.getTransitionById(edgeHit
                    .getId());
        }

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
