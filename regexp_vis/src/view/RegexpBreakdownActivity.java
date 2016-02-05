package view;

import java.util.ArrayList;

import controller.BreakdownSequenceUICommand;
import controller.UICommand;
import model.Automaton;
import model.AutomatonTransition;
import model.BreakdownCommand;
import model.BreakdownSequenceCommand;
import model.Command;
import model.TranslationTools;

/**
 * 
 * @author sp611
 *
 */
public class RegexpBreakdownActivity extends Activity<GraphCanvasEvent> {

    public RegexpBreakdownActivity(GraphCanvasFX canvas, Automaton automaton) {
        super(canvas, automaton);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void processEvent(GraphCanvasEvent event) {
        if (event.getMouseEvent().getClickCount() == 2) {
            onEdgeDoubleClick(event);
        }
    }

    private void onEdgeDoubleClick(GraphCanvasEvent event) {
        GraphEdge edge = event.getTargetEdge();
        if (edge == null) {
            return;
        }

        AutomatonTransition trans = this.automaton
                .getAutomatonTransitionById(edge.getId());

        if (trans == null) {
            System.err
                    .println("Could not find an edge with id " + edge.getId());
            return;
        }

        BreakdownCommand cmd = TranslationTools
                .createBreakdownCommand(this.automaton, trans);
        ArrayList<Command> newCommands = new ArrayList<>();

        if (cmd instanceof BreakdownSequenceCommand) {
            BreakdownSequenceUICommand newCmd = new BreakdownSequenceUICommand(
                    this.canvas, (BreakdownSequenceCommand) cmd);
            newCommands.addAll(newCmd.debugGetUICommands());
        } else {
            for (Command oldCmd : cmd.getCommands()) {
                newCommands.add(UICommand.fromCommand(this.canvas, oldCmd));
            }
        }

        for (Command c : newCommands) {
            c.redo();
        }
    }

}
