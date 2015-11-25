package model;

import java.util.*;

/**
 * Command which breaks down a choice of expressions (e.g. "a|b|c|d")
 * into a set of simpler transitions. For the regular expression to
 * NFA conversion process.
 */
public class BreakdownChoiceCommand extends BreakdownCommand {
    /**
     * @param automaton The automaton for the transition
     * @param t The transition to break down
     * @throws IllegalArgumentException if the transition expression 
     * isn't CHOICE
     */
    public BreakdownChoiceCommand(Automaton automaton, AutomatonTransition t)
    {
        super(automaton, t);
        mCommands = new LinkedList<>();

        BasicRegexp re = (BasicRegexp)t.getData();
        AutomatonState from = t.getFrom();
        AutomatonState to = t.getTo();

        if (re.getOperator() != BasicRegexp.RegexpOperator.OPTION) {
            throw new IllegalArgumentException(
                "BreakdownChoiceCommand must be passed a CHOICE " +
                "transition (e.g. \"a|b|c\")");
        }

        mCommands.add(new RemoveTransitionCommand(automaton, t));
        for (BasicRegexp operand : re.getOperands()) {
            AutomatonTransition newTrans = automaton.createNewTransition(from, to, operand);
            mCommands.add(new AddTransitionCommand(automaton, newTrans));
        }
    }
}
