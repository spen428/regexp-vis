package test.view;

import static org.junit.Assert.*;
import model.AddStateCommand;
import model.AddTransitionCommand;
import model.Automaton;
import model.AutomatonState;
import model.AutomatonTransition;
import model.CommandHistory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ui.Graph;

public class GraphTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public final void testGraph() {
        new Graph();
    }

    @Test
    public final void testGraphAutomaton() {
        Automaton automaton = new Automaton();
        AutomatonState state0 = automaton.createNewState();
        AutomatonState state1 = automaton.createNewState();
        AutomatonTransition transition0 = automaton.createNewTransition(state0,
                state1, "transition0");
        AutomatonTransition transition1 = automaton.createNewTransition(state0,
                state1, "transition1");
        CommandHistory history = new CommandHistory();
        history.executeNewCommand(new AddStateCommand(automaton, state0));
        history.executeNewCommand(new AddStateCommand(automaton, state1));
        history.executeNewCommand(new AddTransitionCommand(automaton,
                transition0));
        history.executeNewCommand(new AddTransitionCommand(automaton,
                transition1));

        Graph graph = new Graph(automaton);
        assertEquals(automaton, graph.getAutomaton());
        assertEquals(5, graph.getSize());
        assertTrue(graph.containsState(state0));
        assertTrue(graph.containsState(state1));
        assertTrue(graph.containsTransition(transition0));
        assertTrue(graph.containsTransition(transition1));
    }

    @Test
    public final void testGetAutomaton() {
        Automaton automaton = new Automaton();
        Graph graph = new Graph(automaton);
        assertEquals(automaton, graph.getAutomaton());
    }

    @Test
    public final void testAddContainsRemoveState() {
        Graph graph = new Graph();
        AutomatonState state0 = new AutomatonState(0);
        AutomatonState state1 = new AutomatonState(1);

        graph.addState(state0);
        assertTrue(graph.containsState(state0));
        assertFalse(graph.containsState(state1));
        assertFalse(graph.containsState(null));

        graph.addState(state0);

        boolean caught = false;
        try {
            graph.addState(null);
        } catch (IllegalArgumentException ex) {
            caught = true;
        }
        assertTrue(caught);

        graph.removeState(state0);
        assertFalse(graph.containsState(state0));

        graph.removeState(state0);

        graph.removeState(null);
    }

    @Test
    public final void testAddContainsRemoveTransition() {
        Graph graph = new Graph();
        AutomatonState state0 = new AutomatonState(0);
        AutomatonState state1 = new AutomatonState(1);
        AutomatonTransition transition = new AutomatonTransition(0, state0,
                state1, "transition0");

        assertFalse(graph.containsTransition(transition));
        graph.addState(state0);
        graph.addState(state1);
        graph.addTransition(transition);
        assertTrue(graph.containsTransition(transition));
        /* Try to add it when it exists */
        graph.addTransition(transition);

        graph.removeTransition(transition);
        assertFalse(graph.containsTransition(transition));
        /* Try to remove it when it exists */
        graph.removeTransition(transition);

        boolean caught = false;
        try {
            graph.addTransition(null);
        } catch (IllegalArgumentException ex) {
            caught = true;
        }
        assertTrue(caught);

        graph.removeTransition(null);
    }

    @Test
    public final void testAddStateWithTransitions() {
        Graph graph = new Graph();
        Automaton automaton = new Automaton();
        AutomatonState state0 = automaton.createNewState();
        AutomatonState state1 = automaton.createNewState();
        AutomatonTransition transition0 = automaton.createNewTransition(state0,
                state1, "transition0");
        AutomatonTransition transition1 = automaton.createNewTransition(state1,
                state0, "transition1");
        CommandHistory history = new CommandHistory();
        history.executeNewCommand(new AddStateCommand(automaton, state0));
        history.executeNewCommand(new AddStateCommand(automaton, state1));
        history.executeNewCommand(new AddTransitionCommand(automaton,
                transition0));
        history.executeNewCommand(new AddTransitionCommand(automaton,
                transition1));

        graph.addState(state0);
        graph.addStateWithTransitions(state1, automaton.removeState(state1));
    }

    @Test
    public final void testRemoveStateWithoutTransitions() {
        /* This should remove the state but not its transitions */
        Graph graph = new Graph();
        AutomatonState state0 = new AutomatonState(0);
        AutomatonState state1 = new AutomatonState(1);
        AutomatonTransition transition0 = new AutomatonTransition(0, state0,
                state1, "transition0");
        AutomatonTransition transition1 = new AutomatonTransition(0, state0,
                state1, "transition1");
        graph.addState(state0);
        graph.addState(state1);
        graph.addTransition(transition0);
        graph.addTransition(transition1);

        graph.removeState(state0, false);
        assertFalse(graph.containsState(state0));
        assertTrue(graph.containsState(state1));
        assertTrue(graph.containsTransition(transition0));
        assertTrue(graph.containsTransition(transition1));

        graph.removeState(state1, false);
        assertFalse(graph.containsState(state0));
        assertFalse(graph.containsState(state1));
        assertTrue(graph.containsTransition(transition0));
        assertTrue(graph.containsTransition(transition1));

        //

        graph = new Graph();
        state0 = new AutomatonState(0);
        state1 = new AutomatonState(1);
        transition0 = new AutomatonTransition(0, state0, state1, "transition0");
        transition1 = new AutomatonTransition(0, state0, state1, "transition1");
        graph.addState(state0);
        graph.addState(state1);
        graph.addTransition(transition0);
        graph.addTransition(transition1);

        graph.removeState(state0, true);
        assertFalse(graph.containsState(state0));
        assertTrue(graph.containsState(state1));
        assertFalse(graph.containsTransition(transition0));
        assertFalse(graph.containsTransition(transition1));
    }

    @Test
    public final void testGetNumStateTransitions() {
        Graph graph = new Graph();
        AutomatonState state0 = new AutomatonState(0);
        AutomatonState state1 = new AutomatonState(1);
        AutomatonTransition transition0 = new AutomatonTransition(0, state0,
                state1, "transition0");
        AutomatonTransition transition1 = new AutomatonTransition(0, state0,
                state1, "transition1");

        assertEquals(0, graph.getNumStateTransitions(null));
        assertEquals(0, graph.getNumStateTransitions(state0));
        graph.addState(state0);
        graph.addState(state1);
        assertEquals(0, graph.getNumStateTransitions(state0));
        graph.addTransition(transition0);
        assertEquals(1, graph.getNumStateTransitions(state0));
        graph.addTransition(transition1);
        assertEquals(2, graph.getNumStateTransitions(state0));
        graph.removeTransition(transition0);
        assertEquals(1, graph.getNumStateTransitions(state0));
    }

    @Test
    public final void testGetSetStartState() {
        Graph graph = new Graph();
        AutomatonState startState = new AutomatonState(0);

        /* Should not initialise as null (it did previously, but that has been changed) */
        assertNotEquals(null, graph.getStartState());

        /* Set start state normally */
        graph.addState(startState);
        graph.setStartState(startState);
        assertEquals(startState, graph.getStartState());

        /* Set start state to a state that doesn't exist yet */
        startState = new AutomatonState(1);
        graph.setStartState(startState);
        assertEquals(startState, graph.getStartState());

        /* Try to set to null */
        graph.setStartState(null);
        assertEquals(null, graph.getStartState());
    }

    @Test
    public final void testSetFinal() {
        Graph graph = new Graph();
        AutomatonState stateNf = new AutomatonState(0);
        assertFalse(stateNf.isFinal());

        graph.addState(stateNf);
        graph.setFinal(stateNf, true);
        assertTrue(stateNf.isFinal());

        graph.setFinal(stateNf, true);
        assertTrue(stateNf.isFinal());

        graph.setFinal(stateNf, false);
        assertFalse(stateNf.isFinal());

        AutomatonState stateF = new AutomatonState(1, true);
        assertTrue(stateF.isFinal());

        graph.addState(stateF);
        graph.setFinal(stateF, false);
        assertFalse(stateF.isFinal());

        graph.setFinal(stateF, false);
        assertFalse(stateF.isFinal());
    }

    @Test
    public final void testGetSizeAndClear() {
        Graph graph = new Graph();
        AutomatonState state0 = new AutomatonState(0);
        AutomatonState state1 = new AutomatonState(1);
        AutomatonTransition transition0 = new AutomatonTransition(0, state0,
                state1, "transition0");

        assertEquals(1, graph.getSize()); // Graph starts with a "start state"
        graph.addState(state0);
        assertEquals(2, graph.getSize());
        graph.addState(state1);
        assertEquals(3, graph.getSize());
        graph.addTransition(transition0);
        assertEquals(4, graph.getSize());
        graph.clear();
        assertEquals(1, graph.getSize()); // When cleared, still has a
                                          // "start state"
    }

}
