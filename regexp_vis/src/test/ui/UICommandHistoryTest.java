package test.ui;

import static org.junit.Assert.*;
import model.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import test.model.CommandHistoryTest;
import ui.AddStateUICommand;
import ui.AddTransitionUICommand;
import ui.Graph;
import ui.GraphState;
import ui.GraphTransition;
import ui.SetIsFinalUICommand;
import ui.UICommand;

/**
 * Tests {@link CommandHistory} when using instances of {@link UICommand}. Based
 * heavily on {@link CommandHistoryTest}.
 * 
 * @author sp611
 *
 */
public class UICommandHistoryTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGraphHistoryComplex() {
		Graph graph = new Graph();
		CommandHistory history = new CommandHistory();

		// Create the states we are going to use
		GraphState start = graph.createNewState("0");
		graph.setStartState(start);
		GraphState b = graph.createNewState("1");
		GraphState c = graph.createNewState("2");
		GraphState d = graph.createNewState("3");
		GraphState e = graph.createNewState("4");
		GraphState f = graph.createNewState("5");

		// Create the transitions we are going to use
		GraphTransition start_b_0 = graph.createNewTransition(start, b, "1");

		GraphTransition b_c_0 = graph.createNewTransition(b, c, "2");
		GraphTransition c_d_0 = graph.createNewTransition(c, d, "3");
		GraphTransition d_e_0 = graph.createNewTransition(d, e, "4");
		GraphTransition e_b_0 = graph.createNewTransition(e, b, "5");

		GraphTransition c_f_0 = graph.createNewTransition(c, f, "!");

		// Build the graph we want through a series of commands
		history.executeNewCommand(new AddStateUICommand(graph, b));
		history.executeNewCommand(new AddTransitionUICommand(graph, start_b_0));

		history.executeNewCommand(new AddStateUICommand(graph, c));
		history.executeNewCommand(new AddTransitionUICommand(graph, b_c_0));

		history.executeNewCommand(new AddStateUICommand(graph, d));
		history.executeNewCommand(new AddStateUICommand(graph, f));
		history.executeNewCommand(new AddTransitionUICommand(graph, c_d_0));
		history.executeNewCommand(new AddTransitionUICommand(graph, c_f_0));
		history.executeNewCommand(new SetIsFinalUICommand(graph, f, true));

		history.executeNewCommand(new AddStateUICommand(graph, e));
		history.executeNewCommand(new AddTransitionUICommand(graph, d_e_0));
		history.executeNewCommand(new AddTransitionUICommand(graph, e_b_0));

		// Test that all the states we expect to see exist
		assertTrue(graph.containsState(start));
		assertTrue(graph.containsState(b));
		assertTrue(graph.containsState(c));
		assertTrue(graph.containsState(d));
		assertTrue(graph.containsState(e));
		assertTrue(graph.containsState(f));

		// Test that state "f" is final
		assertTrue(f.isFinal());

		// Test undo, transition shouldn't exist after
		assertTrue(graph.containsTransition(e_b_0));
		history.prev();
		assertFalse(graph.containsTransition(e_b_0));

		// Test undo to after first command executed
		history.seekIdx(1);
		assertTrue(graph.containsState(start));
		assertTrue(graph.containsState(b));
		assertFalse(graph.containsState(c));
		assertFalse(graph.containsState(d));
		assertFalse(graph.containsState(e));
		assertFalse(graph.containsState(f));
		assertEquals(graph.getStateTransitions(start).size(), 0);
		assertEquals(graph.getStateTransitions(b).size(), 0);

		// Test only start state exists
		history.prev();
		assertTrue(graph.containsState(start));
		assertFalse(graph.containsState(b));
		assertFalse(graph.containsState(c));
		assertFalse(graph.containsState(d));
		assertFalse(graph.containsState(e));
		assertFalse(graph.containsState(f));
		assertEquals(graph.getStateTransitions(start).size(), 0);

		// Finally, test we can replay to the final command
		history.seekIdx(history.getHistorySize());
		assertTrue(graph.containsState(start));
		assertTrue(graph.containsState(b));
		assertTrue(graph.containsState(c));
		assertTrue(graph.containsState(d));
		assertTrue(graph.containsState(e));
		assertTrue(graph.containsState(f));
		assertTrue(graph.containsTransition(e_b_0));
	}

}
