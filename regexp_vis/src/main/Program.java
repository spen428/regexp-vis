package main;

import model.*;

/**
 * @author mjn33
 *
 */
public class Program {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Automaton automaton = new Automaton();
		CommandHistory history = new CommandHistory();

		AutomatonState start = automaton.getStartState();
		AutomatonState b = automaton.createNewState();
		AutomatonState c = automaton.createNewState();
		AutomatonState d = automaton.createNewState();
		AutomatonState e = automaton.createNewState();
		AutomatonState f = automaton.createNewState();

		AutomatonTransition start_b_0 = automaton.createNewTransition(start, b, "1");

		AutomatonTransition b_c_0 = automaton.createNewTransition(b, c, "2");
		AutomatonTransition c_d_0 = automaton.createNewTransition(c, d, "3");
		AutomatonTransition d_e_0 = automaton.createNewTransition(d, e, "4");
		AutomatonTransition e_b_0 = automaton.createNewTransition(e, b, "5");

		AutomatonTransition c_f_0 = automaton.createNewTransition(c, f, "!");

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

		automaton.debugPrint();
	}

}
