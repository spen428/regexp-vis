package view;

import model.Automaton;

/**
 * 
 * @author sp611
 * 
 */
public class NfaToDfaActivity extends Activity<GraphCanvasEvent> {

    public NfaToDfaActivity(GraphCanvasFX canvas, Automaton automaton) {
        super(canvas, automaton);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void processEvent(GraphCanvasEvent event) {
        // TODO Auto-generated method stub

    }

    public class RemoveEpsilonTransitionsActivity
            extends Activity<GraphCanvasEvent> {

        public RemoveEpsilonTransitionsActivity(GraphCanvasFX canvas,
                Automaton automaton) {
            super(canvas, automaton);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void processEvent(GraphCanvasEvent event) {
            // TODO Auto-generated method stub

        }

    }

    public class RemoveNonDeterminismActivity
            extends Activity<GraphCanvasEvent> {

        public RemoveNonDeterminismActivity(GraphCanvasFX canvas,
                Automaton automaton) {
            super(canvas, automaton);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void processEvent(GraphCanvasEvent event) {
            // TODO Auto-generated method stub

        }

    }

}
