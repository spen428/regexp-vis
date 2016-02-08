package model;

import java.util.*;

/**
 * Command which breaks down a sequence of expressions (e.g. "abcd") into a set
 * of simpler transitions. For the regular expression to NFA conversion process.
 */
public class BreakdownSequenceCommand extends BreakdownCommand {
    private int mNewTransitionsCount;
    
    /**
     * @param automaton The automaton for the transition
     * @param t The transition to break down
     * @throws IllegalArgumentException if the transition expression isn't
     * SEQUENCE
     */
    public BreakdownSequenceCommand(Automaton automaton, AutomatonTransition t)
    {
        super(automaton, t);

        BasicRegexp re = (BasicRegexp)t.getData();
        AutomatonState from = t.getFrom();
        AutomatonState to = t.getTo();

        if (re.getOperator() != BasicRegexp.RegexpOperator.SEQUENCE) {
            throw new IllegalArgumentException(
                "BreakdownSequenceCommand must be passed a SEQUENCE " +
                "transition (e.g. \"abcd\"");
        }

        mCommands.add(new RemoveTransitionCommand(automaton, t));
        mNewTransitionsCount = re.getOperands().size();

        AutomatonState prevState = from;
        Iterator<BasicRegexp> it = re.getOperands().iterator();
        while (it.hasNext()) {
            BasicRegexp operand = it.next();
            AutomatonState nextState;
            if (it.hasNext()) {
                nextState = automaton.createNewState();
                mCommands.add(new AddStateCommand(automaton, nextState));
            } else {
                nextState = to;
            }

            AutomatonTransition newTrans = automaton.createNewTransition(
                prevState, nextState, operand);
            mCommands.add(new AddTransitionCommand(automaton, newTrans));
            prevState = nextState;
        }
    }
    
    /**
     * @return The number of new transitions this breakdown will create, e.g. 
     * "abc" will breakdown into 3 transitions "a", "b" and "c"
     */
    public int getNewTransitionsCount()
    {
        return this.mNewTransitionsCount;
    }
}
