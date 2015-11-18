package test.model;

import static org.junit.Assert.*;
import model.*;
import java.util.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AutomatonTest {
    private Automaton mAutomaton;
    private AutomatonState mState1;
    private AutomatonState mState2;
    private AutomatonState mState3;

    @Before
    public void setUp() throws Exception
    {
        mAutomaton = new Automaton();
        mState1 = mAutomaton.createNewState();
        mState2 = mAutomaton.createNewState();
        mState3 = mAutomaton.createNewState();

        mAutomaton.addStateWithTransitions(mState1, new LinkedList<>());
        mAutomaton.addStateWithTransitions(mState2, new LinkedList<>());
        mAutomaton.addStateWithTransitions(mState3, new LinkedList<>());

        mState3.setFinal(true);
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void testStateExists()
    {
        // Test the start state exists
        assertTrue(mAutomaton.stateExists(mAutomaton.getStartState()));

        // Test mState2 exists
        assertTrue(mAutomaton.stateExists(mState2));
    }

    @Test
    public void testHasOutgoingTransition()
    {
        // Test that we start off with no transitions initially
        assertFalse(mAutomaton.hasOutgoingTransition(mState1));
        assertFalse(mAutomaton.hasOutgoingTransition(mState2));
        assertFalse(mAutomaton.hasOutgoingTransition(mState3));

        AutomatonTransition t1 = mAutomaton.createNewTransition(
            mState1, mState2, new Object());
        AutomatonTransition t2 = mAutomaton.createNewTransition(
            mState2, mState3, new Object());

        mAutomaton.addTransition(t1);
        mAutomaton.addTransition(t2);

        // Test that we have the expected result
        assertTrue(mAutomaton.hasOutgoingTransition(mState1));
        assertTrue(mAutomaton.hasOutgoingTransition(mState2));
        assertFalse(mAutomaton.hasOutgoingTransition(mState3));
    }

    @Test
    public void testHasIngoingTransition()
    {
        // Test that we start off with no transitions initially
        assertFalse(mAutomaton.hasIngoingTransition(mState1));
        assertFalse(mAutomaton.hasIngoingTransition(mState2));
        assertFalse(mAutomaton.hasIngoingTransition(mState3));

        AutomatonTransition t1 = mAutomaton.createNewTransition(
            mState1, mState2, new Object());
        AutomatonTransition t2 = mAutomaton.createNewTransition(
            mState2, mState3, new Object());

        mAutomaton.addTransition(t1);
        mAutomaton.addTransition(t2);

        // Test that we have the expected result
        assertFalse(mAutomaton.hasIngoingTransition(mState1));
        assertTrue(mAutomaton.hasIngoingTransition(mState2));
        assertTrue(mAutomaton.hasIngoingTransition(mState3));
    }

    @Test
    public void testGetIngoingTransition()
    {
        // Test that we start off with no transitions initially
        assertTrue(mAutomaton.getIngoingTransition(mState1).isEmpty());
        assertTrue(mAutomaton.getIngoingTransition(mState2).isEmpty());
        assertTrue(mAutomaton.getIngoingTransition(mState3).isEmpty());

        AutomatonTransition t1 = mAutomaton.createNewTransition(
            mState1, mState2, new Object());
        AutomatonTransition t2 = mAutomaton.createNewTransition(
            mState2, mState3, new Object());

        mAutomaton.addTransition(t1);
        mAutomaton.addTransition(t2);

        // Test that we have the expected result
        assertTrue(mAutomaton.getIngoingTransition(mState1).isEmpty());
        assertEquals(mAutomaton.getIngoingTransition(mState2).size(), 1);
        assertTrue(mAutomaton.getIngoingTransition(mState2).contains(t1));
        assertEquals(mAutomaton.getIngoingTransition(mState3).size(), 1);
        assertTrue(mAutomaton.getIngoingTransition(mState3).contains(t2));
    }

    @Test
    public void testGetStateTransitions()
    {
        // Test that we start off with no transitions initially
        assertTrue(mAutomaton.getStateTransitions(mState1).isEmpty());
        assertTrue(mAutomaton.getStateTransitions(mState2).isEmpty());
        assertTrue(mAutomaton.getStateTransitions(mState3).isEmpty());

        AutomatonTransition t1 = mAutomaton.createNewTransition(
            mState1, mState2, new Object());
        AutomatonTransition t2 = mAutomaton.createNewTransition(
            mState2, mState3, new Object());

        mAutomaton.addTransition(t1);
        mAutomaton.addTransition(t2);

        // Test that we have the expected result
        assertEquals(mAutomaton.getStateTransitions(mState1).size(), 1);
        assertTrue(mAutomaton.getStateTransitions(mState1).contains(t1));
        assertEquals(mAutomaton.getStateTransitions(mState2).size(), 1);
        assertTrue(mAutomaton.getStateTransitions(mState2).contains(t2));
        assertTrue(mAutomaton.getStateTransitions(mState3).isEmpty());
    }

    @Test
    public void testCreateNewState()
    {
        AutomatonState state1 = mAutomaton.createNewState();
        AutomatonState state2 = mAutomaton.createNewState();

        // Test we get different IDs
        assertNotEquals(state1.getId(), state2.getId());
    }

    @Test
    public void testCreateNewTransition()
    {
        Object obj = new Object();
        AutomatonTransition state1 = mAutomaton.createNewTransition(mState1, mState2, obj);

        assertEquals(state1.getFrom(), mState1);
        assertEquals(state1.getTo(), mState2);
        assertEquals(state1.getData(), obj);
    }

    @Test
    public void testRemoveState()
    {
        // Test removed state doesn't exist
        mAutomaton.removeState(mState3);
        assertFalse(mAutomaton.stateExists(mState3));
    }

    @Test
    public void testRemoveState_initial()
    {
        boolean caught = false;
        AutomatonState startState = mAutomaton.getStartState();
        try {
            mAutomaton.removeState(startState);
        } catch (RuntimeException e) {
            caught = true;
        }

        // Shouldn't be able to remove start state
        assertEquals(mAutomaton.getStartState(), startState);
        assertTrue(mAutomaton.stateExists(startState));
        assertTrue(caught);
    }

    @Test
    public void testRemoveState_nonExistant()
    {
        boolean caught = false;

        try {
            mAutomaton.removeState(mAutomaton.createNewState());
        } catch (RuntimeException e) {
            caught = true;
        }

        assertTrue(caught);
    }
}
