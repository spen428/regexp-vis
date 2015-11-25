package model;

import java.util.*;

/**
 * Command which breaks down an optional expression (e.g. "a?") into a set of
 * simpler transitions. For the regular expression to NFA conversion process.
 */
public class BreakdownOptionCommand extends BreakdownCommand {
    /**
     * @param automaton The automaton for the transition
     * @param t The transition to break down
     * @throws IllegalArgumentException if the transition expression isn't
     * OPTION
     */
    public BreakdownOptionCommand(Automaton automaton, AutomatonTransition t)
    {
        super(automaton, t);
        mCommands = new LinkedList<>();

        BasicRegexp re = (BasicRegexp)t.getData();
        AutomatonState from = t.getFrom();
        AutomatonState to = t.getTo();

        if (re.getOperator() != BasicRegexp.RegexpOperator.OPTION) {
            throw new IllegalArgumentException(
                "BreakdownOptionCommand must be passed an option " +
                "operator, i.e. \"a?\"");
        }

        // Same as choice between epsilon or the expression
        mCommands.add(new RemoveTransitionCommand(automaton, t));

        BasicRegexp operand = re.getOperands().get(0);
        AutomatonTransition epsilonForwardTrans = automaton.createNewTransition(
            from, to, BasicRegexp.EPSILON_EXPRESSION);
        AutomatonTransition subexprTrans = automaton.createNewTransition(from,
            to, operand);

        mCommands.add(new AddTransitionCommand(automaton, epsilonForwardTrans));
        mCommands.add(new AddTransitionCommand(automaton, subexprTrans));
    }
}
