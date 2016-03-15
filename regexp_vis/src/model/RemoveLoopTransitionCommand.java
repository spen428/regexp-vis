package model;

import java.util.ArrayList;

/**
 * Command to a single loop for a state, this is done by prepending the loop
 * expression wrapped in a STAR operator. Part of the NFA to Regexp translation.
 *
 * Note this Command assumes the only loop transition is the given one.
 */
public class RemoveLoopTransitionCommand extends CompositeCommand {

    private final AutomatonTransition mTransition;

    public RemoveLoopTransitionCommand(Automaton automaton,
            AutomatonTransition trans)
    {
        super(automaton);
        mTransition = trans;

        AutomatonState state = mTransition.getFrom();
        // Wrap the loop regular expression into a STAR operator
        BasicRegexp loopRe = mTransition.getData();
        loopRe = new BasicRegexp(loopRe, BasicRegexp.RegexpOperator.STAR);

        // Actually remove the loop
        super.commands.add(new RemoveTransitionCommand(automaton, mTransition));

        for (AutomatonTransition t : automaton.getStateTransitions(state)) {
            if (t == mTransition) {
                continue;
            }
            // Form the new regexp by prepending the loop regular expression
            // onto the transition regexp
            ArrayList<BasicRegexp> operands = new ArrayList<>();
            operands.add(loopRe);
            operands.add(t.getData());
            BasicRegexp newRe = new BasicRegexp(operands,
                    BasicRegexp.RegexpOperator.SEQUENCE);
            // Do very low depth optimisation
            newRe = newRe.optimise(BasicRegexp.OPTIMISE_ALL, 1);
            AutomatonTransition newTrans = automaton.createNewTransition(state,
                    t.getTo(), newRe);
            // Remove the old and add the new
            super.commands.add(new RemoveTransitionCommand(automaton, t));
            super.commands.add(new AddTransitionCommand(automaton, newTrans));
        }
    }

    /**
     * @return Returns the loop transition which is removed.
     */
    public AutomatonTransition getLoopTransition()
    {
        return mTransition;
    }

}
