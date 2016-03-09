package test.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import model.AddStateCommand;
import model.AddTransitionCommand;
import model.Automaton;
import model.AutomatonState;
import model.AutomatonTransition;
import model.BasicRegexp;
import model.Command;
import model.CommandHistory;
import model.RemoveEpsilonTransitionsCommand;
import model.SetIsFinalCommand;

@SuppressWarnings("static-method")
public class CommandHistoryTest {

    @Test
    public void testAutomatonHistoryComplex() {
        // A more "complex" test, not strictly a unit test as we
        // aren't using stubs / mock objects.
        Automaton automaton = new Automaton();
        CommandHistory history = new CommandHistory();

        // Create the states we are going to use
        AutomatonState start = automaton.getStartState();
        AutomatonState b = automaton.createNewState();
        AutomatonState c = automaton.createNewState();
        AutomatonState d = automaton.createNewState();
        AutomatonState e = automaton.createNewState();
        AutomatonState f = automaton.createNewState();

        // Create the transitions we are going to use
        AutomatonTransition start_b_0 = automaton.createNewTransition(start, b,
                BasicRegexp.EPSILON_EXPRESSION);

        AutomatonTransition b_c_0 = automaton.createNewTransition(b, c,
                BasicRegexp.EPSILON_EXPRESSION);
        AutomatonTransition c_d_0 = automaton.createNewTransition(c, d,
                BasicRegexp.EPSILON_EXPRESSION);
        AutomatonTransition d_e_0 = automaton.createNewTransition(d, e,
                BasicRegexp.EPSILON_EXPRESSION);
        AutomatonTransition e_b_0 = automaton.createNewTransition(e, b,
                BasicRegexp.EPSILON_EXPRESSION);

        AutomatonTransition c_f_0 = automaton.createNewTransition(c, f,
                BasicRegexp.EPSILON_EXPRESSION);

        // Build the automaton we want through a series of commands
        history.executeNewCommand(new AddStateCommand(automaton, b));
        history.executeNewCommand(
                new AddTransitionCommand(automaton, start_b_0));

        history.executeNewCommand(new AddStateCommand(automaton, c));
        history.executeNewCommand(new AddTransitionCommand(automaton, b_c_0));

        history.executeNewCommand(new AddStateCommand(automaton, d));
        history.executeNewCommand(new AddStateCommand(automaton, f));
        history.executeNewCommand(new AddTransitionCommand(automaton, c_d_0));
        history.executeNewCommand(new AddTransitionCommand(automaton, c_f_0));
        history.executeNewCommand(new SetIsFinalCommand(automaton, f, true));

        history.executeNewCommand(new AddStateCommand(automaton, e));
        history.executeNewCommand(new AddTransitionCommand(automaton, d_e_0));
        history.executeNewCommand(new AddTransitionCommand(automaton, e_b_0));

        // Test that all the states we expect to see exist
        assertTrue(automaton.stateExists(start));
        assertTrue(automaton.stateExists(b));
        assertTrue(automaton.stateExists(c));
        assertTrue(automaton.stateExists(d));
        assertTrue(automaton.stateExists(e));
        assertTrue(automaton.stateExists(f));

        // Test that state "f" is final
        assertTrue(f.isFinal());

        // Test undo, transition shouldn't exist after
        assertTrue(automaton.getStateTransitions(e).contains(e_b_0));
        history.prev();
        assertFalse(automaton.getStateTransitions(e).contains(e_b_0));

        // Test undo to after first command executed
        history.seekIdx(1);
        assertTrue(automaton.stateExists(start));
        assertTrue(automaton.stateExists(b));
        assertFalse(automaton.stateExists(c));
        assertFalse(automaton.stateExists(d));
        assertFalse(automaton.stateExists(e));
        assertFalse(automaton.stateExists(f));
        assertEquals(automaton.getStateTransitions(start).size(), 0);
        assertEquals(automaton.getStateTransitions(b).size(), 0);

        // Test only start state exists
        history.prev();
        assertTrue(automaton.stateExists(start));
        assertFalse(automaton.stateExists(b));
        assertFalse(automaton.stateExists(c));
        assertFalse(automaton.stateExists(d));
        assertFalse(automaton.stateExists(e));
        assertFalse(automaton.stateExists(f));
        assertEquals(automaton.getStateTransitions(start).size(), 0);

        // Finally, test we can replay to the final command
        history.seekIdx(history.getHistorySize());
        assertTrue(automaton.stateExists(start));
        assertTrue(automaton.stateExists(b));
        assertTrue(automaton.stateExists(c));
        assertTrue(automaton.stateExists(d));
        assertTrue(automaton.stateExists(e));
        assertTrue(automaton.stateExists(f));
        assertTrue(automaton.getStateTransitions(e).contains(e_b_0));
    }

    @Test
    public void test00() throws Throwable {
        CommandHistory commandHistory0 = new CommandHistory();
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.getStartState();
        RemoveEpsilonTransitionsCommand removeEpsilonTransitionsCommand0 = new RemoveEpsilonTransitionsCommand(
                automaton0, automatonState0);
        commandHistory0.executeNewCommand(removeEpsilonTransitionsCommand0);
        int int0 = commandHistory0.getHistorySize();
        assertEquals(1, int0);
    }

    @Test
    public void test01() throws Throwable {
        CommandHistory commandHistory0 = new CommandHistory();
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.getStartState();
        RemoveEpsilonTransitionsCommand removeEpsilonTransitionsCommand0 = new RemoveEpsilonTransitionsCommand(
                automaton0, automatonState0);
        commandHistory0.executeNewCommand(removeEpsilonTransitionsCommand0);
        int int0 = commandHistory0.getHistoryIdx();
        assertEquals(1, int0);
    }

    @Test
    public void test02() throws Throwable {
        CommandHistory commandHistory0 = new CommandHistory();
        commandHistory0.setClobbered(false);
        boolean boolean0 = commandHistory0.isClobbered();
        assertFalse(boolean0);
    }

    @Test
    public void test03() throws Throwable {
        CommandHistory commandHistory0 = new CommandHistory();
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.getStartState();
        RemoveEpsilonTransitionsCommand removeEpsilonTransitionsCommand0 = new RemoveEpsilonTransitionsCommand(
                automaton0, automatonState0);
        commandHistory0.executeNewCommand(removeEpsilonTransitionsCommand0);
        commandHistory0.executeNewCommand(removeEpsilonTransitionsCommand0);
        commandHistory0.seekIdx(1);
        assertEquals(1, commandHistory0.getHistoryIdx());
    }

    @Test
    public void test04() throws Throwable {
        CommandHistory commandHistory0 = new CommandHistory();
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.getStartState();
        RemoveEpsilonTransitionsCommand removeEpsilonTransitionsCommand0 = new RemoveEpsilonTransitionsCommand(
                automaton0, automatonState0);
        commandHistory0.executeNewCommand(removeEpsilonTransitionsCommand0);
        commandHistory0.executeNewCommand(removeEpsilonTransitionsCommand0);
        commandHistory0.prev();
        commandHistory0.executeNewCommand(removeEpsilonTransitionsCommand0);
        assertEquals(2, commandHistory0.getHistoryIdx());
    }

    @Test
    public void test05() throws Throwable {
        CommandHistory commandHistory0 = new CommandHistory();
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.createNewState();
        AddStateCommand addStateCommand0 = new AddStateCommand(automaton0,
                automatonState0);
        commandHistory0.executeNewCommand(addStateCommand0);
        addStateCommand0.undo();
        // Undeclared exception!
        try {
            commandHistory0.seekIdx(0);
            fail("Expecting exception: RuntimeException");

        } catch (RuntimeException e) {
            //
            // The specified state doesn't exist
            //

        }
    }

    @Test
    public void test06() throws Throwable {
        CommandHistory commandHistory0 = new CommandHistory();
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.createNewState();
        AddStateCommand addStateCommand0 = new AddStateCommand(automaton0,
                automatonState0);
        commandHistory0.executeNewCommand(addStateCommand0);
        commandHistory0.seekIdx(0);
        addStateCommand0.redo();
        // Undeclared exception!
        try {
            commandHistory0.next();
            fail("Expecting exception: RuntimeException");

        } catch (RuntimeException e) {
            //
            // Attempted to insert duplicate state
            //

        }
    }

    @Test
    public void test07() throws Throwable {
        CommandHistory commandHistory0 = new CommandHistory();
        // Undeclared exception!
        try {
            commandHistory0.executeNewCommand((Command) null);
            fail("Expecting exception: NullPointerException");

        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //

        }
    }

    @Test
    public void test08() throws Throwable {
        CommandHistory commandHistory0 = new CommandHistory();
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.getStartState();
        RemoveEpsilonTransitionsCommand removeEpsilonTransitionsCommand0 = new RemoveEpsilonTransitionsCommand(
                automaton0, automatonState0);
        commandHistory0.executeNewCommand(removeEpsilonTransitionsCommand0);
        commandHistory0.setClobbered(false);
        commandHistory0.prev();
        // Undeclared exception!
        try {
            commandHistory0.executeNewCommand(removeEpsilonTransitionsCommand0);
            fail("Expecting exception: RuntimeException");

        } catch (RuntimeException e) {
            //
            // Cannot execute new command while not and the end of the command
            // list
            //

        }
    }

    @Test
    public void test09() throws Throwable {
        CommandHistory commandHistory0 = new CommandHistory();
        // Undeclared exception!
        try {
            commandHistory0.seekIdx(4);
            fail("Expecting exception: IndexOutOfBoundsException");

        } catch (IndexOutOfBoundsException e) {
            //
            // Specified history idx cannot be greater than history length
            //

        }
    }

    @Test
    public void test10() throws Throwable {
        CommandHistory commandHistory0 = new CommandHistory();
        // Undeclared exception!
        try {
            commandHistory0.seekIdx((-2832));
            fail("Expecting exception: IndexOutOfBoundsException");

        } catch (IndexOutOfBoundsException e) {
            //
            // Specified history idx cannot be negative
            //

        }
    }

    @Test
    public void test11() throws Throwable {
        CommandHistory commandHistory0 = new CommandHistory();
        commandHistory0.next();
        assertTrue(commandHistory0.isClobbered());
    }

    @Test
    public void test12() throws Throwable {
        CommandHistory commandHistory0 = new CommandHistory();
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.createNewState();
        AddStateCommand addStateCommand0 = new AddStateCommand(automaton0,
                automatonState0);
        commandHistory0.executeNewCommand(addStateCommand0);
        commandHistory0.seekIdx(0);
        commandHistory0.next();
        assertEquals(1, commandHistory0.getHistoryIdx());
    }

    @Test
    public void test13() throws Throwable {
        CommandHistory commandHistory0 = new CommandHistory();
        commandHistory0.prev();
        assertTrue(commandHistory0.isClobbered());
    }

    @Test
    public void test14() throws Throwable {
        CommandHistory commandHistory0 = new CommandHistory();
        Automaton automaton0 = new Automaton();
        AutomatonState automatonState0 = automaton0.getStartState();
        RemoveEpsilonTransitionsCommand removeEpsilonTransitionsCommand0 = new RemoveEpsilonTransitionsCommand(
                automaton0, automatonState0);
        commandHistory0.executeNewCommand(removeEpsilonTransitionsCommand0);
        commandHistory0.prev();
        commandHistory0.seekIdx(1);
        assertEquals(1, commandHistory0.getHistoryIdx());
    }

    @Test
    public void test15() throws Throwable {
        CommandHistory commandHistory0 = new CommandHistory();
        commandHistory0.getCommands();
        assertEquals(0, commandHistory0.getHistoryIdx());
        assertTrue(commandHistory0.isClobbered());
    }

    @Test
    public void test16() throws Throwable {
        CommandHistory commandHistory0 = new CommandHistory();
        commandHistory0.clear();
        assertTrue(commandHistory0.isClobbered());
        assertEquals(0, commandHistory0.getHistoryIdx());
    }

    @Test
    public void test17() throws Throwable {
        CommandHistory commandHistory0 = new CommandHistory();
        int int0 = commandHistory0.getHistoryIdx();
        assertTrue(commandHistory0.isClobbered());
        assertEquals(0, int0);
    }

    @Test
    public void test18() throws Throwable {
        CommandHistory commandHistory0 = new CommandHistory();
        boolean boolean0 = commandHistory0.isClobbered();
        assertTrue(boolean0);
        assertEquals(0, commandHistory0.getHistoryIdx());
    }

    @Test
    public void test19() throws Throwable {
        CommandHistory commandHistory0 = new CommandHistory();
        commandHistory0.getHistorySize();
        assertEquals(0, commandHistory0.getHistoryIdx());
        assertTrue(commandHistory0.isClobbered());
    }

}
