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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.Test;

import model.Automaton;
import model.AutomatonState;
import model.AutomatonTransition;
import model.RemoveEpsilonTransitionsContext;

@SuppressWarnings({ "unused", "static-method" })
public class RemoveEpsilonTransitionsContextTest {

    @Test
    public void test00() throws Throwable {
        Automaton automaton0 = new Automaton();
        RemoveEpsilonTransitionsContext removeEpsilonTransitionsContext0 = new RemoveEpsilonTransitionsContext(
                automaton0);
        AutomatonState automatonState0 = automaton0.getStartState();
        Set<AutomatonState> set0 = removeEpsilonTransitionsContext0
                .getEquivalentStates(automatonState0);
        LinkedList<AutomatonState> linkedList0 = new LinkedList<>(set0);
        HashMap<AutomatonState, Set<AutomatonState>> hashMap0 = new HashMap<>();
        hashMap0.put(automatonState0, set0);
        AutomatonState automatonState1 = automaton0.createNewState();
        linkedList0.add(automatonState1);
        hashMap0.put(automatonState1, set0);
        Set<AutomatonState> set1 = RemoveEpsilonTransitionsContext
                .calcEquivalentStates(linkedList0, hashMap0);
        assertEquals(0, linkedList0.size());
        assertFalse(set0.equals(set1));
    }

    @Test
    public void test01() throws Throwable {
        Automaton automaton0 = new Automaton();
        RemoveEpsilonTransitionsContext removeEpsilonTransitionsContext0 = new RemoveEpsilonTransitionsContext(
                automaton0);
        AutomatonState automatonState0 = automaton0.createNewState();
        // Undeclared exception!
        try {
            removeEpsilonTransitionsContext0.equivalentStatesExist(automaton0,
                    automatonState0);
            fail("Expecting exception: NullPointerException");

        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //

        }
    }

    @Test
    public void test02() throws Throwable {
        Automaton automaton0 = new Automaton();
        RemoveEpsilonTransitionsContext removeEpsilonTransitionsContext0 = new RemoveEpsilonTransitionsContext(
                automaton0);
        // Undeclared exception!
        try {
            removeEpsilonTransitionsContext0
                    .equivalentStatesExist((Automaton) null);
            fail("Expecting exception: NullPointerException");

        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //

        }
    }

    @Test
    public void test03() throws Throwable {
        ArrayList<AutomatonState> arrayList0 = new ArrayList<>();
        HashMap<AutomatonState, Set<AutomatonState>> hashMap0 = new HashMap<>();
        // Undeclared exception!
        try {
            RemoveEpsilonTransitionsContext.calcEquivalentStates(arrayList0,
                    hashMap0);
            fail("Expecting exception: NoSuchElementException");

        } catch (NoSuchElementException e) {
            //
            // no message in exception (getMessage() returned null)
            //

        }
    }

    @Test
    public void test04() throws Throwable {
        ArrayList<AutomatonState> arrayList0 = new ArrayList<>();
        List<AutomatonState> list0 = arrayList0.subList(0, 0);
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.createNewState();
        arrayList0.add(automatonState0);
        HashMap<AutomatonState, Set<AutomatonState>> hashMap0 = new HashMap<>();
        // Undeclared exception!
        try {
            RemoveEpsilonTransitionsContext.calcEquivalentStates(list0,
                    hashMap0);
            fail("Expecting exception: ConcurrentModificationException");

        } catch (ConcurrentModificationException e) {
            //
            // no message in exception (getMessage() returned null)
            //

        }
    }

    @Test
    public void test05() throws Throwable {
        RemoveEpsilonTransitionsContext removeEpsilonTransitionsContext0 = null;
        try {
            removeEpsilonTransitionsContext0 = new RemoveEpsilonTransitionsContext(
                    (Automaton) null);
            fail("Expecting exception: NullPointerException");

        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //

        }
    }

    @Test
    public void test06() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = new AutomatonState(20, false);
        LinkedList<AutomatonTransition> linkedList0 = new LinkedList<>();
        automaton0.addStateWithTransitions(automatonState0, linkedList0);
        RemoveEpsilonTransitionsContext removeEpsilonTransitionsContext0 = new RemoveEpsilonTransitionsContext(
                automaton0);
        AutomatonState automatonState1 = new AutomatonState(20, false);
        boolean boolean0 = removeEpsilonTransitionsContext0
                .areStatesEquivalent(automatonState1, automatonState0);
        assertFalse(boolean0);
    }

    @Test
    public void test07() throws Throwable {
        Automaton automaton0 = new Automaton();
        RemoveEpsilonTransitionsContext removeEpsilonTransitionsContext0 = new RemoveEpsilonTransitionsContext(
                automaton0);
        AutomatonState automatonState0 = automaton0.createNewState();
        boolean boolean0 = removeEpsilonTransitionsContext0
                .areStatesEquivalent(automatonState0, automatonState0);
        assertFalse(boolean0);
    }

    @Test
    public void test08() throws Throwable {
        Automaton automaton0 = new Automaton();
        RemoveEpsilonTransitionsContext removeEpsilonTransitionsContext0 = new RemoveEpsilonTransitionsContext(
                automaton0);
        AutomatonState automatonState0 = automaton0.getStartState();
        boolean boolean0 = removeEpsilonTransitionsContext0
                .areStatesEquivalent(automatonState0, automatonState0);
        assertTrue(boolean0);
    }

    @Test
    public void test09() throws Throwable {
        Automaton automaton0 = new Automaton();
        RemoveEpsilonTransitionsContext removeEpsilonTransitionsContext0 = new RemoveEpsilonTransitionsContext(
                automaton0);
        Automaton automaton1 = new Automaton();
        boolean boolean0 = removeEpsilonTransitionsContext0
                .equivalentStatesExist(automaton1);
        assertFalse(boolean0);
    }

    @Test
    public void test10() throws Throwable {
        Automaton automaton0 = new Automaton();
        RemoveEpsilonTransitionsContext removeEpsilonTransitionsContext0 = new RemoveEpsilonTransitionsContext(
                automaton0);
        boolean boolean0 = removeEpsilonTransitionsContext0
                .equivalentStatesExist(automaton0);
        assertFalse(boolean0);
    }

    @Test
    public void test11() throws Throwable {
        Automaton automaton0 = new Automaton();
        RemoveEpsilonTransitionsContext removeEpsilonTransitionsContext0 = new RemoveEpsilonTransitionsContext(
                automaton0);
        AutomatonState automatonState0 = automaton0.getStartState();
        boolean boolean0 = removeEpsilonTransitionsContext0
                .equivalentStatesExist(automaton0, automatonState0);
        assertFalse(boolean0);
    }

    @Test
    public void test12() throws Throwable {
        Automaton automaton0 = new Automaton();
        RemoveEpsilonTransitionsContext removeEpsilonTransitionsContext0 = new RemoveEpsilonTransitionsContext(
                automaton0);
        AutomatonState automatonState0 = automaton0.createNewState();
        Set<AutomatonState> set0 = removeEpsilonTransitionsContext0
                .getEquivalentStates(automatonState0);
        assertNull(set0);
    }

    @Test
    public void test13() throws Throwable {
        Automaton automaton0 = new Automaton();
        RemoveEpsilonTransitionsContext removeEpsilonTransitionsContext0 = new RemoveEpsilonTransitionsContext(
                automaton0);
        AutomatonState automatonState0 = automaton0.getStartState();
        Set<AutomatonState> set0 = removeEpsilonTransitionsContext0
                .getEquivalentStates(automatonState0);
        assertNotNull(set0);

        LinkedList<AutomatonState> linkedList0 = new LinkedList<>(set0);
        linkedList0.push((AutomatonState) null);
        HashMap<AutomatonState, Set<AutomatonState>> hashMap0 = new HashMap<>();
        hashMap0.put(automatonState0, set0);
        RemoveEpsilonTransitionsContext.calcEquivalentStates(linkedList0,
                hashMap0);
        assertEquals(1, linkedList0.size());
    }

    @Test
    public void test14() throws Throwable {
        Automaton automaton0 = new Automaton();
        RemoveEpsilonTransitionsContext removeEpsilonTransitionsContext0 = new RemoveEpsilonTransitionsContext(
                automaton0);
        AutomatonState automatonState0 = automaton0.getStartState();
        Set<AutomatonState> set0 = removeEpsilonTransitionsContext0
                .getEquivalentStates(automatonState0);
        LinkedList<AutomatonState> linkedList0 = new LinkedList<>(set0);
        linkedList0.push(automatonState0);
        HashMap<AutomatonState, Set<AutomatonState>> hashMap0 = new HashMap<>();
        // Undeclared exception!
        try {
            RemoveEpsilonTransitionsContext.calcEquivalentStates(linkedList0,
                    hashMap0);
            fail("Expecting exception: NullPointerException");

        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //

        }
    }
}
