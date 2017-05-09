/*
 * Copyright (c) 2015, 2016 Matthew J. Nicholls, Samuel Pengelly,
 * Parham Ghassemi, William R. Dix
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package test.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.Automaton;
import model.AutomatonState;
import model.AutomatonTransition;
import model.BasicRegexp;

public class AutomatonTest {
    private Automaton mAutomaton;
    private AutomatonState mState1;
    private AutomatonState mState2;
    private AutomatonState mState3;

    @Before
    public void setUp() throws Exception {
        mAutomaton = new Automaton();
        mState1 = mAutomaton.createNewState();
        mState2 = mAutomaton.createNewState();
        mState3 = mAutomaton.createNewState();

        mAutomaton.addStateWithTransitions(mState1,
                new LinkedList<AutomatonTransition>());
        mAutomaton.addStateWithTransitions(mState2,
                new LinkedList<AutomatonTransition>());
        mAutomaton.addStateWithTransitions(mState3,
                new LinkedList<AutomatonTransition>());

        mState3.setFinal(true);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testStateExists() {
        // Test the start state exists
        assertTrue(mAutomaton.stateExists(mAutomaton.getStartState()));

        // Test mState2 exists
        assertTrue(mAutomaton.stateExists(mState2));
    }

    @Test
    public void testHasOutgoingTransition() {
        // Test that we start off with no transitions initially
        assertFalse(mAutomaton.hasOutgoingTransition(mState1));
        assertFalse(mAutomaton.hasOutgoingTransition(mState2));
        assertFalse(mAutomaton.hasOutgoingTransition(mState3));

        AutomatonTransition t1 = mAutomaton.createNewTransition(mState1,
                mState2, BasicRegexp.EPSILON_EXPRESSION);
        AutomatonTransition t2 = mAutomaton.createNewTransition(mState2,
                mState3, BasicRegexp.EPSILON_EXPRESSION);

        mAutomaton.addTransition(t1);
        mAutomaton.addTransition(t2);

        // Test that we have the expected result
        assertTrue(mAutomaton.hasOutgoingTransition(mState1));
        assertTrue(mAutomaton.hasOutgoingTransition(mState2));
        assertFalse(mAutomaton.hasOutgoingTransition(mState3));
    }

    @Test
    public void testHasIngoingTransition() {
        // Test that we start off with no transitions initially
        assertFalse(mAutomaton.hasIngoingTransition(mState1));
        assertFalse(mAutomaton.hasIngoingTransition(mState2));
        assertFalse(mAutomaton.hasIngoingTransition(mState3));

        AutomatonTransition t1 = mAutomaton.createNewTransition(mState1,
                mState2, BasicRegexp.EPSILON_EXPRESSION);
        AutomatonTransition t2 = mAutomaton.createNewTransition(mState2,
                mState3, BasicRegexp.EPSILON_EXPRESSION);

        mAutomaton.addTransition(t1);
        mAutomaton.addTransition(t2);

        // Test that we have the expected result
        assertFalse(mAutomaton.hasIngoingTransition(mState1));
        assertTrue(mAutomaton.hasIngoingTransition(mState2));
        assertTrue(mAutomaton.hasIngoingTransition(mState3));
    }

    @Test
    public void testHasIngoingTransition_duplicate() {
        AutomatonTransition t1 = mAutomaton.createNewTransition(mState1,
                mState2, BasicRegexp.EPSILON_EXPRESSION);
        AutomatonTransition t2 = mAutomaton.createNewTransition(mState1,
                mState2, BasicRegexp.EPSILON_EXPRESSION);

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
    public void testGetIngoingTransition() {
        // Test that we start off with no transitions initially
        assertTrue(mAutomaton.getIngoingTransition(mState1).isEmpty());
        assertTrue(mAutomaton.getIngoingTransition(mState2).isEmpty());
        assertTrue(mAutomaton.getIngoingTransition(mState3).isEmpty());

        AutomatonTransition t1 = mAutomaton.createNewTransition(mState1,
                mState2, BasicRegexp.EPSILON_EXPRESSION);
        AutomatonTransition t2 = mAutomaton.createNewTransition(mState2,
                mState3, BasicRegexp.EPSILON_EXPRESSION);

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
    public void testGetIngoingTransition_duplicate() {
        AutomatonTransition t1 = mAutomaton.createNewTransition(mState1,
                mState2, BasicRegexp.EPSILON_EXPRESSION);
        AutomatonTransition t2 = mAutomaton.createNewTransition(mState1,
                mState2, BasicRegexp.EPSILON_EXPRESSION);

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
    public void testGetStateTransitions() {
        // Test that we start off with no transitions initially
        assertTrue(mAutomaton.getStateTransitions(mState1).isEmpty());
        assertTrue(mAutomaton.getStateTransitions(mState2).isEmpty());
        assertTrue(mAutomaton.getStateTransitions(mState3).isEmpty());

        AutomatonTransition t1 = mAutomaton.createNewTransition(mState1,
                mState2, BasicRegexp.EPSILON_EXPRESSION);
        AutomatonTransition t2 = mAutomaton.createNewTransition(mState2,
                mState3, BasicRegexp.EPSILON_EXPRESSION);

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
    public void testGetStateTransitions_duplicate() {
        AutomatonTransition t1 = mAutomaton.createNewTransition(mState1,
                mState2, BasicRegexp.EPSILON_EXPRESSION);

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
    public void testCreateNewState() {
        AutomatonState state1 = mAutomaton.createNewState();
        AutomatonState state2 = mAutomaton.createNewState();

        // Test we get different IDs
        assertNotEquals(state1.getId(), state2.getId());
    }

    @Test
    public void testCreateNewTransition() {
        BasicRegexp obj = BasicRegexp.EPSILON_EXPRESSION;
        AutomatonTransition state1 = mAutomaton.createNewTransition(mState1,
                mState2, obj);

        assertEquals(state1.getFrom(), mState1);
        assertEquals(state1.getTo(), mState2);
        assertEquals(state1.getData(), obj);
    }

    @Test
    public void testRemoveState() {
        // Test removed state doesn't exist
        mAutomaton.removeState(mState3);
        assertFalse(mAutomaton.stateExists(mState3));
    }

    @Test
    public void testRemoveState_initial() {
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
            AutomatonState duplicate = new AutomatonState(
                    mAutomaton.getStartState().getId());
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
    public void testRemoveState_nonExistent() {
        boolean caught = false;

        try {
            mAutomaton.removeState(mAutomaton.createNewState());
        } catch (RuntimeException e) {
            caught = true;
        }

        assertTrue(caught);
    }

    @Test
    public void testRemoveState_duplicate() {
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
    public void testAddStateWithTransitions_duplicate() {
        AutomatonState a = mAutomaton.createNewState();
        AutomatonState b = new AutomatonState(a.getId());

        mAutomaton.addStateWithTransitions(a,
                new LinkedList<AutomatonTransition>());
        boolean caught = false;
        try {
            // Should throw, duplicate ID
            mAutomaton.addStateWithTransitions(b,
                    new LinkedList<AutomatonTransition>());
        } catch (RuntimeException e) {
            caught = true;
        }

        assertTrue(caught);

        // Test that the state was indeed not added
        assertFalse(mAutomaton.stateExists(b));
    }

    @Test
    public void testAddTransition_duplicate() {
        AutomatonTransition t1 = mAutomaton.createNewTransition(mState1,
                mState2, BasicRegexp.EPSILON_EXPRESSION);

        mAutomaton.addTransition(t1);

        AutomatonState s = new AutomatonState(mState1.getId());
        // Test that adding a transition through a duplicate state
        // throws
        AutomatonTransition t2 = mAutomaton.createNewTransition(s, mState2,
                BasicRegexp.EPSILON_EXPRESSION);

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
    public void testAddTransition_duplicateTrans() {
        AutomatonTransition t1 = mAutomaton.createNewTransition(mState1,
                mState2, BasicRegexp.EPSILON_EXPRESSION);

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
    public void testRemoveTransition() {
        AutomatonTransition t1 = mAutomaton.createNewTransition(mState1,
                mState2, BasicRegexp.EPSILON_EXPRESSION);

        mAutomaton.addTransition(t1);
        mAutomaton.removeTransition(t1);

        // Check the transition was removed
        assertFalse(mAutomaton.getStateTransitions(mState1).contains(t1));
    }

    @Test
    public void testRemoveTransition_nonExistentTrans() {
        AutomatonTransition t1 = mAutomaton.createNewTransition(mState1,
                mState2, BasicRegexp.EPSILON_EXPRESSION);

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
    public void testRemoveTransition_duplicateTrans() {
        BasicRegexp tmpObj = BasicRegexp.EPSILON_EXPRESSION;
        AutomatonTransition t1 = mAutomaton.createNewTransition(mState1,
                mState2, tmpObj);
        AutomatonTransition t2 = mAutomaton.createNewTransition(mState1,
                mState2, tmpObj);

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

    @Test
    public void test01() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.getStartState();
        automatonState0.setFinal(true);
        assertEquals(0, automatonState0.getId());
    }

    @Test
    public void test02() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.getStartState();
        AutomatonTransition automatonTransition0 = new AutomatonTransition(63,
                automatonState0, automatonState0, (BasicRegexp) null);
        automaton0.addTransition(automatonTransition0);
        AutomatonTransition automatonTransition1 = automaton0
                .getAutomatonTransitionById(63);
        assertEquals(63, automatonTransition1.getId());
    }

    @Test
    public void test03() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.getAutomatonStateById(0);
        automatonState0.setFinal(true);
        assertEquals(0, automatonState0.getId());
    }

    @Test
    public void test04() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = new AutomatonState(0);
        automaton0.createNewTransition(automatonState0, automatonState0,
                (BasicRegexp) null);
        AutomatonTransition automatonTransition0 = automaton0
                .createNewTransition(automatonState0, automatonState0,
                        (BasicRegexp) null);
        assertEquals(1, automatonTransition0.getId());
    }

    @Test
    public void test05() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.createNewState();
        automatonState0.setFinal(true);
        assertEquals(1, automatonState0.getId());
    }

    @Test
    public void test06() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.getStartState();
        AutomatonTransition automatonTransition0 = new AutomatonTransition(1466,
                automatonState0, automatonState0, (BasicRegexp) null);
        automaton0.addTransition(automatonTransition0);
        AutomatonTransition automatonTransition1 = automaton0
                .getAutomatonTransitionById(0);
        assertNull(automatonTransition1);
    }

    @Test
    public void test07() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.getStartState();
        AutomatonState automatonState1 = new AutomatonState(394);
        AutomatonTransition automatonTransition0 = automaton0
                .createNewTransition(automatonState0, automatonState1,
                        (BasicRegexp) null);
        assertEquals(0, automatonTransition0.getId());
    }

    @Test
    public void test08() throws Throwable {
        Automaton automaton0 = new Automaton();
        // Undeclared exception!
        try {
            automaton0.stateExists((AutomatonState) null);
            fail("Expecting exception: NullPointerException");

        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //

        }
    }

    @Test
    public void test09() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonTransition automatonTransition0 = new AutomatonTransition(1,
                (AutomatonState) null, (AutomatonState) null,
                (BasicRegexp) null);
        // Undeclared exception!
        try {
            automaton0.removeTransition(automatonTransition0);
            fail("Expecting exception: NullPointerException");

        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //

        }
    }

    @Test
    public void test10() throws Throwable {
        Automaton automaton0 = new Automaton();
        // Undeclared exception!
        try {
            automaton0.removeState((AutomatonState) null);
            fail("Expecting exception: NullPointerException");

        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //

        }
    }

    @Test
    public void test11() throws Throwable {
        Automaton automaton0 = new Automaton();
        // Undeclared exception!
        try {
            automaton0.hasOutgoingTransition((AutomatonState) null);
            fail("Expecting exception: NullPointerException");

        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //

        }
    }

    @Test
    public void test12() throws Throwable {
        Automaton automaton0 = new Automaton();
        // Undeclared exception!
        try {
            automaton0.hasIngoingTransition((AutomatonState) null);
            fail("Expecting exception: NullPointerException");

        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //

        }
    }

    @Test
    public void test13() throws Throwable {
        Automaton automaton0 = new Automaton();
        // Undeclared exception!
        try {
            automaton0.getStateTransitions((AutomatonState) null);
            fail("Expecting exception: NullPointerException");

        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //

        }
    }

    @Test
    public void test14() throws Throwable {
        Automaton automaton0 = new Automaton();
        // Undeclared exception!
        try {
            automaton0.getIngoingTransition((AutomatonState) null);
            fail("Expecting exception: NullPointerException");

        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //

        }
    }

    @Test
    public void test15() throws Throwable {
        Automaton automaton0 = new Automaton();
        // Undeclared exception!
        try {
            automaton0.addTransition((AutomatonTransition) null);
            fail("Expecting exception: NullPointerException");

        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //

        }
    }

    @Test
    public void test16() throws Throwable {
        Automaton automaton0 = new Automaton();
        LinkedList<AutomatonTransition> linkedList0 = new LinkedList<>();
        // Undeclared exception!
        try {
            automaton0.addStateWithTransitions((AutomatonState) null,
                    linkedList0);
            fail("Expecting exception: NullPointerException");

        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //

        }
    }

    @Test
    public void test17() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.getStartState();
        boolean boolean0 = automaton0.stateExists(automatonState0);
        assertTrue(boolean0);
    }

    @Test
    public void test18() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.createNewState();
        boolean boolean0 = automaton0.stateExists(automatonState0);
        assertEquals(1, automatonState0.getId());
        assertFalse(boolean0);
    }

    @Test
    public void test19() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.getStartState();
        AutomatonTransition automatonTransition0 = new AutomatonTransition(
                (-1217), automatonState0, automatonState0, (BasicRegexp) null);
        automaton0.addTransition(automatonTransition0);
        AutomatonTransition automatonTransition1 = automaton0
                .getAutomatonTransitionById((-1217));
        assertSame(automatonTransition1, automatonTransition0);
    }

    @Test
    public void test20() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.getStartState();
        AutomatonTransition automatonTransition0 = new AutomatonTransition(1466,
                automatonState0, automatonState0, (BasicRegexp) null);
        automaton0.addTransition(automatonTransition0);
        AutomatonTransition automatonTransition1 = automaton0
                .getAutomatonTransitionById(2879);
        assertNull(automatonTransition1);
    }

    @Test
    public void test21() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0
                .getAutomatonStateById((-1217));
        assertNull(automatonState0);
    }

    @Test
    public void test22() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.getStartState();
        AutomatonTransition automatonTransition0 = new AutomatonTransition(63,
                automatonState0, automatonState0, (BasicRegexp) null);
        automaton0.addTransition(automatonTransition0);
        automaton0.removeTransition(automatonTransition0);
        assertEquals(1, automaton0.getNumStates());
    }

    @Test
    public void test23() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.getStartState();
        AutomatonTransition automatonTransition0 = new AutomatonTransition(63,
                automatonState0, automatonState0, (BasicRegexp) null);
        // Undeclared exception!
        try {
            automaton0.removeTransition(automatonTransition0);
            fail("Expecting exception: RuntimeException");

        } catch (RuntimeException e) {
            //
            // The specified transition doesn't exist
            //

        }
    }

    @Test
    public void test24() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.getStartState();
        AutomatonTransition automatonTransition0 = new AutomatonTransition(63,
                automatonState0, automatonState0, (BasicRegexp) null);
        automaton0.addTransition(automatonTransition0);
        // Undeclared exception!
        try {
            automaton0.addTransition(automatonTransition0);
            fail("Expecting exception: RuntimeException");

        } catch (RuntimeException e) {
            //
            // The specified transitions already exists in this automaton
            //

        }
    }

    @Test
    public void test25() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.getAutomatonStateById(0);
        // Undeclared exception!
        try {
            automaton0.removeState(automatonState0);
            fail("Expecting exception: RuntimeException");

        } catch (RuntimeException e) {
            //
            // Cannot remove the start state.
            //

        }
    }

    @Test
    public void test26() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = new AutomatonState(0);
        // Undeclared exception!
        try {
            automaton0.removeState(automatonState0);
            fail("Expecting exception: RuntimeException");

        } catch (RuntimeException e) {
            //
            // The specified state doesn't exist
            //

        }
    }

    @Test
    public void test27() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.createNewState();
        LinkedList<AutomatonTransition> linkedList0 = new LinkedList<>();
        automaton0.addStateWithTransitions(automatonState0, linkedList0);
        automaton0.removeState(automatonState0);
        assertEquals(1, automatonState0.getId());
    }

    @Test
    public void test28() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.createNewState();
        // Undeclared exception!
        try {
            automaton0.getStateTransitions(automatonState0);
            fail("Expecting exception: RuntimeException");

        } catch (RuntimeException e) {
            //
            // The specified state doesn't exist, cannot get transitions.
            //

        }
    }

    @Test
    public void test29() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.getStateById(0);
        List<AutomatonTransition> list0 = automaton0
                .getStateTransitions(automatonState0);
        assertEquals(0, list0.size());
    }

    @Test
    public void test30() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.createNewState();
        LinkedList<AutomatonTransition> linkedList0 = new LinkedList<>();
        AutomatonState automatonState1 = new AutomatonState(1807, false);
        AutomatonTransition automatonTransition0 = new AutomatonTransition(1807,
                automatonState1, automatonState1, (BasicRegexp) null);
        linkedList0.add(automatonTransition0);
        automaton0.addStateWithTransitions(automatonState0, linkedList0);
        List<AutomatonTransition> list0 = automaton0
                .getIngoingTransition(automatonState0);
        assertTrue(list0.isEmpty());
        assertEquals(1, automatonState0.getId());
    }

    @Test
    public void test31() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.createNewState();
        LinkedList<AutomatonTransition> linkedList0 = new LinkedList<>();
        AutomatonTransition automatonTransition0 = new AutomatonTransition(1807,
                automatonState0, automatonState0, (BasicRegexp) null);
        linkedList0.add(automatonTransition0);
        automaton0.addStateWithTransitions(automatonState0, linkedList0);
        List<AutomatonTransition> list0 = automaton0
                .getIngoingTransition(automatonState0);
        assertEquals(1, automatonState0.getId());
        assertFalse(list0.isEmpty());
    }

    @Test
    public void test32() throws Throwable {
        Automaton automaton0 = new Automaton();
        Automaton automaton1 = new Automaton();
        AutomatonState automatonState0 = automaton1.getAutomatonStateById(0);
        // Undeclared exception!
        try {
            automaton0.getIngoingTransition(automatonState0);
            fail("Expecting exception: RuntimeException");

        } catch (RuntimeException e) {
            //
            // The specified state \"state\" is not part of this automaton.
            //

        }
    }

    @Test
    public void test33() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.getStartState();
        AutomatonTransition automatonTransition0 = new AutomatonTransition(1466,
                automatonState0, automatonState0, (BasicRegexp) null);
        automaton0.addTransition(automatonTransition0);
        boolean boolean0 = automaton0.hasIngoingTransition(automatonState0);
        assertTrue(boolean0);
    }

    @Test
    public void test34() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.getStartState();
        boolean boolean0 = automaton0.hasIngoingTransition(automatonState0);
        assertFalse(boolean0);
    }

    @Test
    public void test35() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.getStateById(0);
        AutomatonTransition automatonTransition0 = automaton0
                .createNewTransition(automatonState0, automatonState0,
                        (BasicRegexp) null);
        automaton0.addTransition(automatonTransition0);
        assertEquals(0, automatonTransition0.getId());

        boolean boolean0 = automaton0.hasOutgoingTransition(automatonState0);
        assertTrue(boolean0);
    }

    @Test
    public void test36() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = new AutomatonState(0);
        // Undeclared exception!
        try {
            automaton0.hasOutgoingTransition(automatonState0);
            fail("Expecting exception: RuntimeException");

        } catch (RuntimeException e) {
            //
            // The specified state \"state\" is not part of this automaton.
            //

        }
    }

    @Test
    public void test37() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.getStateById(0);
        boolean boolean0 = automaton0.hasOutgoingTransition(automatonState0);
        assertFalse(boolean0);
    }

    @Test
    public void test38() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.getStartState();
        AutomatonTransition automatonTransition0 = new AutomatonTransition(63,
                automatonState0, automatonState0, (BasicRegexp) null);
        automaton0.addTransition(automatonTransition0);
        AutomatonTransition automatonTransition1 = automaton0
                .getTransitionById(63);
        assertSame(automatonTransition1, automatonTransition0);
    }

    @Test
    public void test39() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.getStartState();
        AutomatonTransition automatonTransition0 = new AutomatonTransition(1466,
                automatonState0, automatonState0, (BasicRegexp) null);
        automaton0.addTransition(automatonTransition0);
        AutomatonTransition automatonTransition1 = automaton0
                .getTransitionById(1);
        assertNull(automatonTransition1);
    }

    @Test
    public void test40() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.getStateById((-1));
        assertNull(automatonState0);
    }

    @Test
    public void test41() throws Throwable {
        Automaton automaton0 = new Automaton();
        Automaton automaton1 = new Automaton();
        AutomatonState automatonState0 = automaton1.getStartState();
        AutomatonTransition automatonTransition0 = automaton0
                .createNewTransition(automatonState0, automatonState0,
                        (BasicRegexp) null);
        // Undeclared exception!
        try {
            automaton0.addTransition(automatonTransition0);
            fail("Expecting exception: RuntimeException");

        } catch (RuntimeException e) {
            //
            // The specified state \"from\" is not part of this automaton,
            // cannot add transition
            //

        }
    }

    @Test
    public void test42() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = new AutomatonState(1);
        AutomatonTransition automatonTransition0 = new AutomatonTransition(1,
                automatonState0, automatonState0, (BasicRegexp) null);
        // Undeclared exception!
        try {
            automaton0.removeTransition(automatonTransition0);
            fail("Expecting exception: RuntimeException");

        } catch (RuntimeException e) {
            //
            // The specified state \"from\" is not part of this automaton,
            // cannot remove transition
            //

        }
    }

    @Test
    public void test43() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.getStateById(0);
        assertNotNull(automatonState0);

        automaton0.clear();
        boolean boolean0 = automaton0.stateExists(automatonState0);
        assertFalse(boolean0);
    }

    @Test
    public void test44() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.getStartState();
        AutomatonTransition automatonTransition0 = new AutomatonTransition(1466,
                automatonState0, automatonState0, (BasicRegexp) null);
        automaton0.addTransition(automatonTransition0);
        // Undeclared exception!
        try {
            automaton0.debugPrint();
            fail("Expecting exception: NullPointerException");

        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //

        }
    }

    @Test
    public void test45() throws Throwable {
        Automaton automaton0 = new Automaton();
        automaton0.debugPrint();
        assertEquals(1, automaton0.getNumStates());
    }

    @Test
    public void test46() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.getAutomatonStateById(0);
        LinkedList<AutomatonTransition> linkedList0 = new LinkedList<>();
        // Undeclared exception!
        try {
            automaton0.addStateWithTransitions(automatonState0, linkedList0);
            fail("Expecting exception: RuntimeException");

        } catch (RuntimeException e) {
            //
            // Attempted to insert duplicate state
            //

        }
    }

    @Test
    public void test47() throws Throwable {
        Automaton automaton0 = new Automaton();
        Iterator<Automaton.StateTransitionsPair> iterator0 = automaton0
                .graphIterator();
        assertNotNull(iterator0);
    }

    @Test
    public void test48() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.createNewState();
        // Undeclared exception!
        try {
            automaton0.hasIngoingTransition(automatonState0);
            fail("Expecting exception: RuntimeException");

        } catch (RuntimeException e) {
            //
            // The specified state \"state\" is not part of this automaton.
            //

        }
    }

    @Test
    public void test49() throws Throwable {
        Automaton automaton0 = new Automaton();
        int int0 = automaton0.getNumStates();
        assertEquals(1, int0);
    }

    @Test
    public void test50() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.getStartState();
        AutomatonTransition automatonTransition0 = automaton0
                .createNewTransition(automatonState0, automatonState0,
                        (BasicRegexp) null);
        automaton0.addTransition(automatonTransition0);
        AutomatonTransition automatonTransition1 = automaton0
                .getTransitionById(63);
        assertNull(automatonTransition1);
    }
}
