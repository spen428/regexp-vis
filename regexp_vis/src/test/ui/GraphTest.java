package test.ui;

import static org.junit.Assert.*;
import model.Automaton;
import model.AutomatonState;
import model.AutomatonTransition;

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
        automaton.addTransition(transition0);
        automaton.addTransition(transition1);

        Graph graph = new Graph(automaton);
        assertEquals(automaton, graph.getAutomaton());
        assertEquals(4, graph.getSize());
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
        graph.addState(null);

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

        graph.addTransition(null);
        graph.removeTransition(null);
    }

    @Test
    public final void testAddStateWithTransitions() {
        fail("Not yet implemented"); // TODO
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

        assertEquals(0, graph.getNumStateTransitions(null));
        fail("Not yet implemented"); // TODO
    }

    @Test
    public final void testGetSetStartState() {
        Graph graph = new Graph();
        AutomatonState startState = new AutomatonState(0);

        /* Should initialise as null */
        assertEquals(null, graph.getStartState());

        /* Set start state normally */
        graph.addState(startState);
        graph.setStartState(startState);
        assertEquals(startState, graph.getStartState());

        /* Set start state to a state that doesn't exist yet */
        startState = new AutomatonState(1);
        graph.setStartState(startState);
        assertEquals(startState, graph.getStartState());

        /* Set to null */
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

        assertEquals(0, graph.getSize());
        graph.addState(state0);
        assertEquals(1, graph.getSize());
        graph.addState(state1);
        assertEquals(2, graph.getSize());
        graph.addTransition(transition0);
        assertEquals(3, graph.getSize());
        graph.clear();
        assertEquals(0, graph.getSize());
    }

    @Test
    public final void testEqualsAndHashCode() {
        Graph graphA = new Graph();
        AutomatonState state0a = new AutomatonState(0);
        AutomatonState state1a = new AutomatonState(1);
        AutomatonTransition transition0a = new AutomatonTransition(0, state0a,
                state1a, "transition0");

        Graph graphB = new Graph();
        Graph graphC = new Graph();
        AutomatonState state0b = new AutomatonState(0);
        AutomatonState state1b = new AutomatonState(1);
        AutomatonTransition transition0b = new AutomatonTransition(0, state0b,
                state1b, "transition0");

        /* Both A and B are empty, should be equal */
        assertEquals(graphA.hashCode(), graphB.hashCode());
        assertTrue(graphA.equals(graphB));
        assertTrue(graphB.equals(graphA));

        graphA.addState(state0a);
        graphA.addState(state1a);
        graphA.addTransition(transition0a);

        /* Graph B is empty and A is not, should not be equal */
        assertNotEquals(graphA.hashCode(), graphB.hashCode());
        assertFalse(graphA.equals(graphB));
        assertFalse(graphB.equals(graphA));

        graphB.addState(state0b);
        assertNotEquals(graphA.hashCode(), graphB.hashCode()); // Sanity check
        graphB.addState(state1b);
        graphB.addTransition(transition0b);

        graphC.addState(state0b);
        assertNotEquals(graphA.hashCode(), graphB.hashCode()); // Sanity check
        graphC.addState(state1b);
        graphC.addTransition(transition0b);

        assertTrue(graphA.equals(graphB));
        assertTrue(graphB.equals(graphC));
        assertTrue(graphC.equals(graphB));
        assertTrue(graphB.equals(graphA));
        assertTrue(graphA.equals(graphC));
        assertTrue(graphC.equals(graphA));

        /* B and C contain identical states and transitions, should be equal */
        assertEquals(graphB.hashCode(), graphC.hashCode());
        /* A and B contain equvilant states and transitions, should be equal */
        assertEquals(graphA.hashCode(), graphB.hashCode());
    }

}
