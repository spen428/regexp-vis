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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Test;

import model.Automaton;
import model.AutomatonState;
import model.RemoveNonDeterminismContext;

@SuppressWarnings({ "unused", "static-method" })
public class RemoveNonDeterminismContextTest {

    @Test
    public void test00() throws Throwable {
        Automaton automaton0 = new Automaton();
        RemoveNonDeterminismContext removeNonDeterminismContext0 = new RemoveNonDeterminismContext(
                automaton0);
        LinkedHashSet<AutomatonState> linkedHashSet0 = new LinkedHashSet<>();
        removeNonDeterminismContext0.putStateBinding((AutomatonState) null,
                linkedHashSet0);
        Set<AutomatonState> set0 = removeNonDeterminismContext0
                .lookupStateBinding((AutomatonState) null);
        assertEquals(0, set0.size());
    }

    @Test
    public void test01() throws Throwable {
        Automaton automaton0 = new Automaton();
        RemoveNonDeterminismContext removeNonDeterminismContext0 = new RemoveNonDeterminismContext(
                automaton0);
        AutomatonState automatonState0 = automaton0.getStartState();
        LinkedHashSet<AutomatonState> linkedHashSet0 = new LinkedHashSet<>();
        removeNonDeterminismContext0.putStateBinding(automatonState0,
                linkedHashSet0);
        AutomatonState automatonState1 = removeNonDeterminismContext0
                .findStateFromSet(linkedHashSet0);
        assertFalse(automatonState1.isFinal());
    }

    @Test
    public void test02() throws Throwable {
        Automaton automaton0 = new Automaton();
        RemoveNonDeterminismContext removeNonDeterminismContext0 = new RemoveNonDeterminismContext(
                automaton0);
        LinkedHashSet<AutomatonState> linkedHashSet0 = new LinkedHashSet<>();
        AutomatonState automatonState0 = new AutomatonState(2368, true);
        removeNonDeterminismContext0.putStateBinding(automatonState0,
                linkedHashSet0);
        AutomatonState automatonState1 = removeNonDeterminismContext0
                .findStateFromSet(linkedHashSet0);
        assertSame(automatonState1, automatonState0);
    }

    @Test
    public void test03() throws Throwable {
        Automaton automaton0 = new Automaton();
        RemoveNonDeterminismContext removeNonDeterminismContext0 = new RemoveNonDeterminismContext(
                automaton0);
        // Undeclared exception!
        try {
            removeNonDeterminismContext0
                    .reachableToSet((Set<AutomatonState>) null);
            fail("Expecting exception: NullPointerException");

        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //

        }
    }

    @Test
    public void test04() throws Throwable {
        Automaton automaton0 = new Automaton();
        RemoveNonDeterminismContext removeNonDeterminismContext0 = new RemoveNonDeterminismContext(
                automaton0);
        removeNonDeterminismContext0.putStateBinding((AutomatonState) null,
                (Set<AutomatonState>) null);
        // Undeclared exception!
        try {
            removeNonDeterminismContext0
                    .findStateFromSet((Set<AutomatonState>) null);
            fail("Expecting exception: NullPointerException");

        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //

        }
    }

    @Test
    public void test05() throws Throwable {
        RemoveNonDeterminismContext removeNonDeterminismContext0 = null;
        try {
            removeNonDeterminismContext0 = new RemoveNonDeterminismContext(
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
        AutomatonState automatonState0 = automaton0.createNewState();
        RemoveNonDeterminismContext removeNonDeterminismContext0 = new RemoveNonDeterminismContext(
                automaton0);
        LinkedHashSet<AutomatonState> linkedHashSet0 = new LinkedHashSet<>();
        linkedHashSet0.add(automatonState0);
        removeNonDeterminismContext0.putStateBinding(automatonState0,
                linkedHashSet0);
        Set<AutomatonState> set0 = removeNonDeterminismContext0
                .reachableToSet(linkedHashSet0);
        assertEquals(1, set0.size());
    }

    @Test
    public void test07() throws Throwable {
        Automaton automaton0 = new Automaton();
        RemoveNonDeterminismContext removeNonDeterminismContext0 = new RemoveNonDeterminismContext(
                automaton0);
        LinkedHashSet<AutomatonState> linkedHashSet0 = new LinkedHashSet<>();
        AutomatonState automatonState0 = automaton0.getStartState();
        linkedHashSet0.add(automatonState0);
        Set<AutomatonState> set0 = removeNonDeterminismContext0
                .reachableToSet(linkedHashSet0);
        assertFalse(set0.isEmpty());
    }

    @Test
    public void test08() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.createNewState();
        RemoveNonDeterminismContext removeNonDeterminismContext0 = new RemoveNonDeterminismContext(
                automaton0);
        removeNonDeterminismContext0.putStateBinding(automatonState0,
                (Set<AutomatonState>) null);
        LinkedHashSet<AutomatonState> linkedHashSet0 = new LinkedHashSet<>();
        AutomatonState automatonState1 = removeNonDeterminismContext0
                .findStateFromSet(linkedHashSet0);
        assertNull(automatonState1);
    }

    @Test
    public void test09() throws Throwable {
        Automaton automaton0 = new Automaton();
        RemoveNonDeterminismContext removeNonDeterminismContext0 = new RemoveNonDeterminismContext(
                automaton0);
        removeNonDeterminismContext0.removeStateBinding((AutomatonState) null);
    }

    @Test
    public void test10() throws Throwable {
        Automaton automaton0 = new Automaton();
        RemoveNonDeterminismContext removeNonDeterminismContext0 = new RemoveNonDeterminismContext(
                automaton0);
        LinkedHashSet<AutomatonState> linkedHashSet0 = new LinkedHashSet<>();
        AutomatonState automatonState0 = new AutomatonState((-566));
        removeNonDeterminismContext0.putStateBinding(automatonState0,
                linkedHashSet0);
        AutomatonState automatonState1 = removeNonDeterminismContext0
                .findStateFromSet(linkedHashSet0);
        assertSame(automatonState1, automatonState0);
    }

    @Test
    public void test11() throws Throwable {
        Automaton automaton0 = new Automaton();
        RemoveNonDeterminismContext removeNonDeterminismContext0 = new RemoveNonDeterminismContext(
                automaton0);
        Set<AutomatonState> set0 = removeNonDeterminismContext0
                .lookupStateBinding((AutomatonState) null);
        assertNull(set0);
    }

    @Test
    public void test12() throws Throwable {
        Automaton automaton0 = new Automaton();
        RemoveNonDeterminismContext removeNonDeterminismContext0 = new RemoveNonDeterminismContext(
                automaton0);
        Automaton automaton1 = removeNonDeterminismContext0.getAutomaton();
        assertSame(automaton0, automaton1);
    }
}
