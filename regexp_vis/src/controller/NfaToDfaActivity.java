package controller;

import view.GraphCanvasEvent;
import view.GraphCanvasFX;
import view.GraphEdge;
import model.Automaton;
import model.AutomatonTransition;
import model.BasicRegexp;
import model.RemoveTransitionCommand;

/**
 * 
 * @author sp611
 * 
 */
public class NfaToDfaActivity extends Activity {

    public NfaToDfaActivity(GraphCanvasFX canvas, Automaton automaton) {
        super(canvas, automaton);
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

    public class RemoveEpsilonTransitionsActivity extends Activity {

        public RemoveEpsilonTransitionsActivity(GraphCanvasFX canvas,
                Automaton automaton) {
            super(canvas, automaton);
        }

        @Override
        public void onNodeClicked(GraphCanvasEvent event) {

        }

        @Override
        public void onEdgeClicked(GraphCanvasEvent event) {
            if (event.getMouseEvent().getClickCount() == 2) {
                onEdgeDoubleClick(event);
            }
        }

        @Override
        public void onBackgroundClicked(GraphCanvasEvent event) {

        }

        private void onEdgeDoubleClick(GraphCanvasEvent event) {
            GraphEdge edge = event.getTargetEdge();
            AutomatonTransition transition = this.automaton
                    .getAutomatonTransitionById(edge.getId());
            BasicRegexp re = (BasicRegexp) transition.getData();
            if (re.getChar() == BasicRegexp.EPSILON_CHAR) {
                super.executeNewCommand(new RemoveTransitionCommand(automaton, transition));
            }
        }

    }

    public class RemoveNonDeterminismActivity extends Activity {

        public RemoveNonDeterminismActivity(GraphCanvasFX canvas,
                Automaton automaton) {
            super(canvas, automaton);
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

    }

}
