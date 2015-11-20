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
        Graph graph = new Graph();
    }

    @Test
    public final void testGraphAutomaton() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public final void testGetAutomaton() {
        Automaton automaton = new Automaton();
        Graph graph = new Graph(automaton);
        assertEquals(automaton, graph.getAutomaton());
    }

    @Test
    public final void testGetNumStateTransitions() {
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
    public final void testCreateStylesheet() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public final void testSetFinalAutomatonStateBoolean() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public final void testSetFinalMxCellBoolean() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public final void testAddState() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public final void testAddStateWithTransitions() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public final void testRemoveStateAutomatonState() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public final void testRemoveStateAutomatonStateBoolean() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public final void testAddTransition() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public final void testRemoveTransition() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public final void testContainsState() {
        Graph graph = new Graph();
        AutomatonState state0 = new AutomatonState(0);
        AutomatonState state1 = new AutomatonState(1);

        graph.addState(state0);
        assertTrue(graph.containsState(state0));
        assertFalse(graph.containsState(state1));
        assertFalse(graph.containsState(null));
    }

    @Test
    public final void testContainsTransition() {
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

    @Test
    public final void testToString() {
        fail("Not yet implemented"); // TODO
    }

}
