package controller;

import view.GraphCanvasEvent;
import view.GraphCanvasFX;
import view.GraphEdge;
import model.Automaton;
import model.AutomatonTransition;
import model.BreakdownCommand;
import model.TranslationTools;

/**
 * 
 * @author sp611
 *
 */
public class RegexpBreakdownActivity extends Activity<GraphCanvasEvent> {

    public RegexpBreakdownActivity(GraphCanvasFX canvas, Automaton automaton) {
        super(canvas, automaton);
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
        UICommand uiCmd = UICommand.fromCommand(this.canvas, cmd);
        if (uiCmd != null) {
            uiCmd.redo();
            // TODO: Add to history
        }
    }

}