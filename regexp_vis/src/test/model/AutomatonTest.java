package test.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.Automaton;
import model.AutomatonState;
import model.AutomatonTransition;

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

        mAutomaton.addStateWithTransitions(mState1, new LinkedList<AutomatonTransition>());
        mAutomaton.addStateWithTransitions(mState2, new LinkedList<AutomatonTransition>());
        mAutomaton.addStateWithTransitions(mState3, new LinkedList<AutomatonTransition>());

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
    public void testHasIngoingTransition_duplicate()
    {
        AutomatonTransition t1 = mAutomaton.createNewTransition(
            mState1, mState2, new Object());
        AutomatonTransition t2 = mAutomaton.createNewTransition(
            mState1, mState2, new Object());

        mAutomaton.addTransition(t1);
        mAutomaton.addTransition(t2);

        AutomatonState s = new AutomatonState(mState2.getId());
        // Test that querying through a duplicate state throws
        boolean caught = false;
        try {
            mAutomaton.hasIngoingTransition(s);
        } catch (RuntimeException e) {
            caught = true;
        }
        assertTrue(caught);
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
    public void testGetIngoingTransition_duplicate()
    {
        AutomatonTransition t1 = mAutomaton.createNewTransition(
            mState1, mState2, new Object());
        AutomatonTransition t2 = mAutomaton.createNewTransition(
            mState1, mState2, new Object());

        mAutomaton.addTransition(t1);
        mAutomaton.addTransition(t2);

        AutomatonState s = new AutomatonState(mState2.getId());
        // Test that querying through a duplicate state throws
        boolean caught = false;
        try {
            mAutomaton.getIngoingTransition(s);
        } catch (RuntimeException e) {
            caught = true;
        }
        assertTrue(caught);
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
    public void testGetStateTransitions_duplicate()
    {
        AutomatonTransition t1 = mAutomaton.createNewTransition(
            mState1, mState2, new Object());

        mAutomaton.addTransition(t1);

        AutomatonState s = new AutomatonState(mState1.getId());
        // Test that accessing through mState1 gives the transitions,
        // and that accessing through "s" instead throws
        assertEquals(mAutomaton.getStateTransitions(mState1).size(), 1);
        assertTrue(mAutomaton.getStateTransitions(mState1).contains(t1));

        boolean caught = false;
        try {
            mAutomaton.getStateTransitions(s);
        } catch (RuntimeException e) {
            caught = true;
        }
        assertTrue(caught);
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

        caught = false;
        try {
            AutomatonState duplicate = new AutomatonState(mAutomaton.getStartState().getId());
            mAutomaton.removeState(duplicate);
        } catch (RuntimeException e) {
            caught = true;
        }

        // Shouldn't be able to remove start state via a duplicate
        assertEquals(mAutomaton.getStartState(), startState);
        assertTrue(mAutomaton.stateExists(startState));
        assertTrue(caught);
    }

    @Test
    public void testRemoveState_nonExistent()
    {
        boolean caught = false;

        try {
            mAutomaton.removeState(mAutomaton.createNewState());
        } catch (RuntimeException e) {
            caught = true;
        }

        assertTrue(caught);
    }

    @Test
    public void testRemoveState_duplicate()
    {
        AutomatonState s = new AutomatonState(mState3.getId());
        // Test that removing a state through a duplicate state throws

        boolean caught = false;
        try {
            mAutomaton.removeState(s);
        } catch (RuntimeException e) {
            caught = true;
        }
        assertTrue(caught);
        // Test actual state still exists
        assertTrue(mAutomaton.stateExists(mState3));
    }

    @Test
    public void testAddStateWithTransitions_duplicate()
    {
        AutomatonState a = mAutomaton.createNewState();
        AutomatonState b = new AutomatonState(a.getId());

        mAutomaton.addStateWithTransitions(a, new LinkedList<AutomatonTransition>());
        boolean caught = false;
        try {
            // Should throw, duplicate ID
            mAutomaton.addStateWithTransitions(b, new LinkedList<AutomatonTransition>());
        } catch (RuntimeException e) {
            caught = true;
        }

        assertTrue(caught);

        // Test that the state was indeed not added
        assertFalse(mAutomaton.stateExists(b));
    }

    @Test
    public void testAddTransition_duplicate()
    {
        AutomatonTransition t1 = mAutomaton.createNewTransition(
            mState1, mState2, new Object());

        mAutomaton.addTransition(t1);

        AutomatonState s = new AutomatonState(mState1.getId());
        // Test that adding a transition through a duplicate state
        // throws
        AutomatonTransition t2 = mAutomaton.createNewTransition(
                s, mState2, new Object());

        boolean caught = false;
        try {
            mAutomaton.addTransition(t2);
        } catch (RuntimeException e) {
            caught = true;
        }
        assertTrue(caught);

        // Test that the transition was indeed not added
        assertEquals(mAutomaton.getStateTransitions(mState1).size(), 1);
        assertTrue(mAutomaton.getStateTransitions(mState1).contains(t1));
        assertFalse(mAutomaton.getStateTransitions(mState1).contains(t2));
    }

    @Test
    public void testAddTransition_duplicateTrans()
    {
        AutomatonTransition t1 = mAutomaton.createNewTransition(
                mState1, mState2, new Object());

        mAutomaton.addTransition(t1);

        // Test that adding a duplicate transition throws
        boolean caught = false;
        try {
            mAutomaton.addTransition(t1);
        } catch (RuntimeException e) {
            caught = true;
        }
        assertTrue(caught);

        // Check the transition was not added twice
        assertEquals(mAutomaton.getStateTransitions(mState1).size(), 1);
    }

    @Test
    public void testRemoveTransition()
    {
        AutomatonTransition t1 = mAutomaton.createNewTransition(
                mState1, mState2, new Object());

        mAutomaton.addTransition(t1);
        mAutomaton.removeTransition(t1);

        // Check the transition was removed
        assertFalse(mAutomaton.getStateTransitions(mState1).contains(t1));
    }

    @Test
    public void testRemoveTransition_nonExistentTrans()
    {
        AutomatonTransition t1 = mAutomaton.createNewTransition(
                mState1, mState2, new Object());

        // Test that removing a non-existent transition throws
        boolean caught = false;
        try {
            mAutomaton.removeTransition(t1);
        } catch (RuntimeException e) {
            caught = true;
        }
        assertTrue(caught);
    }

    @Test
    public void testRemoveTransition_duplicateTrans()
    {
        Object tmpObj = new Object();
        AutomatonTransition t1 = mAutomaton.createNewTransition(
                mState1, mState2, tmpObj);
        AutomatonTransition t2 = mAutomaton.createNewTransition(
                mState1, mState2, tmpObj);

        mAutomaton.addTransition(t1);

        // Test that removing via a duplicate transition throws
        // (transition doesn't exist)
        boolean caught = false;
        try {
            mAutomaton.removeTransition(t2);
        } catch (RuntimeException e) {
            caught = true;
        }
        assertTrue(caught);

        // Check the transition was not removed
        assertTrue(mAutomaton.getStateTransitions(mState1).contains(t1));
    }
}
