package test.model;

import static org.junit.Assert.*;
import model.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CommandHistoryTest {
    @Before
    public void setUp() throws Exception
    {
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void testAutomatonHistoryComplex()
    {
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
        AutomatonTransition start_b_0 = automaton.createNewTransition(start, b, "1");

        AutomatonTransition b_c_0 = automaton.createNewTransition(b, c, "2");
        AutomatonTransition c_d_0 = automaton.createNewTransition(c, d, "3");
        AutomatonTransition d_e_0 = automaton.createNewTransition(d, e, "4");
        AutomatonTransition e_b_0 = automaton.createNewTransition(e, b, "5");

        AutomatonTransition c_f_0 = automaton.createNewTransition(c, f, "!");

        // Build the automaton we want through a series of commands
        history.executeNewCommand(new AddStateCommand(automaton, b));
        history.executeNewCommand(new AddTransitionCommand(automaton, start_b_0));

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
}
