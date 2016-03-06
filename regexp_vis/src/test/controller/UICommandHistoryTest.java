package test.controller;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import controller.AddStateUICommand;
import controller.AddTransitionUICommand;
import controller.UICommand;
import model.AddStateCommand;
import model.AddTransitionCommand;
import model.Automaton;
import model.AutomatonState;
import model.AutomatonTransition;
import model.BasicRegexp;
import model.CommandHistory;
import test.model.CommandHistoryTest;
import view.GraphCanvasFX;

/**
 * Tests {@link CommandHistory} when using instances of {@link UICommand}. Based
 * heavily on {@link CommandHistoryTest}.
 * 
 * @author sp611
 *
 */
public class UICommandHistoryTest {

    private static boolean containsState(GraphCanvasFX graph,
            AutomatonState state) {
        return (graph.lookupNode(state.getId()) != null);
    }

    private static boolean containsTransition(GraphCanvasFX graph,
            AutomatonTransition transition) {
        return (graph.lookupEdge(transition.getId()) != null);
    }

    @Test
    public void testGraphHistoryComplex() {
        final Automaton automaton = new Automaton();
        final GraphCanvasFX graph = new GraphCanvasFX();
        final CommandHistory history = new CommandHistory();

        // Create the states we are going to use
        AddStateCommand s = new AddStateCommand(automaton,
                automaton.getStartState());
        AddStateCommand b = new AddStateCommand(automaton,
                automaton.createNewState());
        AddStateCommand c = new AddStateCommand(automaton,
                automaton.createNewState());
        AddStateCommand d = new AddStateCommand(automaton,
                automaton.createNewState());
        AddStateCommand e = new AddStateCommand(automaton,
                automaton.createNewState());
        AddStateCommand f = new AddStateCommand(automaton,
                automaton.createNewState());

        // Create the transitions we are going to use
        AddTransitionCommand s_b_0 = new AddTransitionCommand(automaton,
                automaton.createNewTransition(s.getState(), b.getState(), BasicRegexp.EPSILON_EXPRESSION));
        AddTransitionCommand b_c_0 = new AddTransitionCommand(automaton,
                automaton.createNewTransition(b.getState(), c.getState(), BasicRegexp.EPSILON_EXPRESSION));
        AddTransitionCommand c_d_0 = new AddTransitionCommand(automaton,
                automaton.createNewTransition(c.getState(), d.getState(), BasicRegexp.EPSILON_EXPRESSION));
        AddTransitionCommand d_e_0 = new AddTransitionCommand(automaton,
                automaton.createNewTransition(d.getState(), e.getState(), BasicRegexp.EPSILON_EXPRESSION));
        AddTransitionCommand e_b_0 = new AddTransitionCommand(automaton,
                automaton.createNewTransition(e.getState(), b.getState(), BasicRegexp.EPSILON_EXPRESSION));
        AddTransitionCommand c_f_0 = new AddTransitionCommand(automaton,
                automaton.createNewTransition(c.getState(), f.getState(), BasicRegexp.EPSILON_EXPRESSION));

        // Build the graph we want through a series of commands
        history.executeNewCommand(new AddStateUICommand(graph, b, 0, 1));
        history.executeNewCommand(new AddTransitionUICommand(graph, s_b_0));

        history.executeNewCommand(new AddStateUICommand(graph, c, 2, 3));
        history.executeNewCommand(new AddTransitionUICommand(graph, b_c_0));

        history.executeNewCommand(new AddStateUICommand(graph, d, 4, 5));
        history.executeNewCommand(new AddStateUICommand(graph, f, 6, 7));
        history.executeNewCommand(new AddTransitionUICommand(graph, c_d_0));
        history.executeNewCommand(new AddTransitionUICommand(graph, c_f_0));

        history.executeNewCommand(new AddStateUICommand(graph, e, 8, 9));
        history.executeNewCommand(new AddTransitionUICommand(graph, d_e_0));
        history.executeNewCommand(new AddTransitionUICommand(graph, e_b_0));

        // Test that all the states we expect to see exist
        assertTrue(containsState(graph, s.getState()));
        assertTrue(containsState(graph, b.getState()));
        assertTrue(containsState(graph, c.getState()));
        assertTrue(containsState(graph, d.getState()));
        assertTrue(containsState(graph, e.getState()));
        assertTrue(containsState(graph, f.getState()));

        // Test that state "f" is final
        assertTrue(f.getState().isFinal());

        // Test undo, transition shouldn't exist after
        assertTrue(containsTransition(graph, e_b_0.getTransition()));
        history.prev();
        assertFalse(containsTransition(graph, e_b_0.getTransition()));

        // Test undo to after first command executed
        history.seekIdx(2); // +1 because of necessary SetStartStateUICommand
        assertTrue(containsState(graph, s.getState()));
        assertTrue(containsState(graph, b.getState()));
        assertFalse(containsState(graph, c.getState()));
        assertFalse(containsState(graph, d.getState()));
        assertFalse(containsState(graph, e.getState()));
        assertFalse(containsState(graph, f.getState()));
        // assertEquals(graph.getNumStateTransitions(s.getState()), 0);
        // assertEquals(graph.getNumStateTransitions(b.getState()), 0);

        // Test only start state exists
        history.prev();
        assertTrue(containsState(graph, s.getState()));
        assertFalse(containsState(graph, b.getState()));
        assertFalse(containsState(graph, c.getState()));
        assertFalse(containsState(graph, d.getState()));
        assertFalse(containsState(graph, e.getState()));
        assertFalse(containsState(graph, f.getState()));
        // assertEquals(graph.getNumStateTransitions(s.getState()), 0);

        // Finally, test we can replay to the final command
        history.seekIdx(history.getHistorySize());
        assertTrue(containsState(graph, s.getState()));
        assertTrue(containsState(graph, b.getState()));
        assertTrue(containsState(graph, c.getState()));
        assertTrue(containsState(graph, d.getState()));
        assertTrue(containsState(graph, e.getState()));
        assertTrue(containsState(graph, f.getState()));
        assertTrue(containsTransition(graph, e_b_0.getTransition()));
    }

}
