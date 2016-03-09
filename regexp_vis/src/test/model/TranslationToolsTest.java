package test.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.Test;

import model.Automaton;
import model.AutomatonState;
import model.AutomatonTransition;
import model.BasicRegexp;
import model.TranslationTools;

@SuppressWarnings({ "unused", "static-method" })
public class TranslationToolsTest {

    @Test
    public void testConstructor() throws Throwable {
        new TranslationTools();
    }

    @Test
    public void test00() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = new AutomatonState((-1435));
        // Undeclared exception!
        try {
            TranslationTools.stateHasNonDeterminism(automaton0,
                    automatonState0);
            fail("Expecting exception: RuntimeException");

        } catch (RuntimeException e) {
            //
            // The specified state doesn't exist, cannot get transitions.
            //
        }
    }

    @Test
    public void test01() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.createNewState();
        // Undeclared exception!
        try {
            TranslationTools.stateHasEpsilonTransitions(automaton0,
                    automatonState0);
            fail("Expecting exception: RuntimeException");

        } catch (RuntimeException e) {
            //
            // The specified state doesn't exist, cannot get transitions.
            //
        }
    }

    @Test
    public void test02() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.createNewState();
        // Undeclared exception!
        try {
            TranslationTools.calcReachableStates(automaton0, automatonState0,
                    (Predicate<AutomatonTransition>) null);
            fail("Expecting exception: RuntimeException");

        } catch (RuntimeException e) {
            //
            // The specified state doesn't exist, cannot get transitions.
            //
        }
    }

    @Test
    public void test03() throws Throwable {
        Automaton automaton0 = new Automaton();
        // Undeclared exception!
        try {
            TranslationTools.calcReachableStates(automaton0,
                    (AutomatonState) null,
                    (Predicate<AutomatonTransition>) null);
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
        AutomatonState automatonState0 = automaton0.createNewState();
        // Undeclared exception!
        try {
            TranslationTools.calcNonDeterministicTrans(automaton0,
                    automatonState0);
            fail("Expecting exception: RuntimeException");

        } catch (RuntimeException e) {
            //
            // The specified state doesn't exist, cannot get transitions.
            //
        }
    }

    @Test
    public void test05() throws Throwable {
        Automaton automaton0 = new Automaton();
        Automaton automaton1 = new Automaton();
        AutomatonState automatonState0 = automaton1.getAutomatonStateById(0);
        // Undeclared exception!
        try {
            TranslationTools.calcEpsilonReachableStates(automaton0,
                    automatonState0);
            fail("Expecting exception: RuntimeException");

        } catch (RuntimeException e) {
            //
            // The specified state doesn't exist, cannot get transitions.
            //
        }
    }

    @Test
    public void test06() throws Throwable {
        Automaton automaton0 = new Automaton();
        // Undeclared exception!
        try {
            TranslationTools.calcEpsilonReachableStates(automaton0,
                    (AutomatonState) null);
            fail("Expecting exception: NullPointerException");

        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }

    @Test
    public void test07() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.getStartState();
        AutomatonTransition automatonTransition0 = automaton0
                .createNewTransition(automatonState0, automatonState0,
                        (BasicRegexp) null);
        automaton0.addTransition(automatonTransition0);
        // Undeclared exception!
        try {
            TranslationTools.automatonHasNonDeterminism(automaton0);
            fail("Expecting exception: NullPointerException");

        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }

    @Test
    public void test08() throws Throwable {
        // Undeclared exception!
        try {
            TranslationTools.automatonCalcUnreachableStates((Automaton) null);
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
        AutomatonState automatonState0 = automaton0.getAutomatonStateById(0);
        Set<AutomatonState> set0 = TranslationTools.calcReachableStates(
                automaton0, automatonState0,
                (Predicate<AutomatonTransition>) null);
        assertFalse(set0.isEmpty());
    }

    @Test
    public void test10() throws Throwable {
        Automaton automaton0 = new Automaton();
        boolean boolean0 = TranslationTools
                .automatonHasNonDeterminism(automaton0);
        assertFalse(boolean0);
    }

    @Test
    public void test11() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.getStateById(0);
        boolean boolean0 = TranslationTools.stateHasNonDeterminism(automaton0,
                automatonState0);
        assertFalse(boolean0);
    }

    @Test
    public void test12() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.getStartState();
        AutomatonTransition automatonTransition0 = new AutomatonTransition(1534,
                automatonState0, automatonState0, (BasicRegexp) null);
        automaton0.addTransition(automatonTransition0);
        // Undeclared exception!
        try {
            TranslationTools.stateHasNonDeterminism(automaton0,
                    automatonState0);
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
        AutomatonState automatonState0 = automaton0.getStartState();
        List<Character> list0 = TranslationTools
                .calcNonDeterministicTrans(automaton0, automatonState0);
        assertTrue(list0.isEmpty());
    }

    @Test
    public void test14() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.getStartState();
        AutomatonTransition automatonTransition0 = automaton0
                .createNewTransition(automatonState0, automatonState0,
                        (BasicRegexp) null);
        automaton0.addTransition(automatonTransition0);
        // Undeclared exception!
        try {
            TranslationTools.calcNonDeterministicTrans(automaton0,
                    automatonState0);
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
        AutomatonState automatonState0 = automaton0.getStartState();
        AutomatonTransition automatonTransition0 = automaton0
                .createNewTransition(automatonState0, automatonState0,
                        (BasicRegexp) null);
        automaton0.addTransition(automatonTransition0);
        Set<AutomatonState> set0 = TranslationTools
                .automatonCalcUnreachableStates(automaton0);
        assertEquals(0, set0.size());
    }

    @Test
    public void test16() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.getStartState();
        AutomatonTransition automatonTransition0 = automaton0
                .createNewTransition(automatonState0, automatonState0,
                        (BasicRegexp) null);
        automaton0.addTransition(automatonTransition0);
        // Undeclared exception!
        try {
            TranslationTools.automatonHasEpsilonTransitions(automaton0);
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
        boolean boolean0 = TranslationTools
                .automatonHasEpsilonTransitions(automaton0);
        assertFalse(boolean0);
    }

    @Test
    public void test18() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.getStartState();
        boolean boolean0 = TranslationTools
                .stateHasEpsilonTransitions(automaton0, automatonState0);
        assertFalse(boolean0);
    }

    @Test
    public void test19() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.getStartState();
        AutomatonTransition automatonTransition0 = new AutomatonTransition(1572,
                automatonState0, automatonState0, (BasicRegexp) null);
        automaton0.addTransition(automatonTransition0);
        // Undeclared exception!
        try {
            TranslationTools.stateHasEpsilonTransitions(automaton0,
                    automatonState0);
            fail("Expecting exception: NullPointerException");

        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }

    @Test
    public void test20() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = new AutomatonState(1168);
        LinkedList<AutomatonTransition> linkedList0 = new LinkedList<>();
        linkedList0.add((AutomatonTransition) null);
        automaton0.addStateWithTransitions(automatonState0, linkedList0);
        // Undeclared exception!
        try {
            TranslationTools.getAllTransitionsToBreakdown(automaton0);
            fail("Expecting exception: NullPointerException");

        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }

    @Test
    public void test21() throws Throwable {
        Automaton automaton0 = new Automaton();
        List<AutomatonTransition> list0 = TranslationTools
                .getAllTransitionsToBreakdown(automaton0);
        assertNull(list0);
    }

    @Test
    public void test22() throws Throwable {
        Automaton automaton0 = new Automaton();
        // Undeclared exception!
        try {
            TranslationTools.createBreakdownCommand(automaton0,
                    (AutomatonTransition) null);
            fail("Expecting exception: IllegalArgumentException");

        } catch (IllegalArgumentException e) {
            //
            // AutomatonTransition cannot be null
            //
        }
    }

    @Test
    public void test23() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.getStartState();
        AutomatonTransition automatonTransition0 = new AutomatonTransition(
                (-595), automatonState0, automatonState0, (BasicRegexp) null);
        // Undeclared exception!
        try {
            TranslationTools.createBreakdownCommand((Automaton) null,
                    automatonTransition0);
            fail("Expecting exception: IllegalArgumentException");

        } catch (IllegalArgumentException e) {
            //
            // Automaton cannot be null
            //
        }
    }

    @Test
    public void test24() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.getAutomatonStateById(0);
        AutomatonTransition automatonTransition0 = automaton0
                .createNewTransition(automatonState0, automatonState0,
                        (BasicRegexp) null);
        // Undeclared exception!
        try {
            TranslationTools.createBreakdownCommand(automaton0,
                    automatonTransition0);
            fail("Expecting exception: NullPointerException");

        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }

    @Test
    public void test26() throws Throwable {
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.getAutomatonStateById(0);
        Set<AutomatonState> set0 = TranslationTools
                .calcEpsilonReachableStates(automaton0, automatonState0);
        assertEquals(1, set0.size());
    }
}
