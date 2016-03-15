package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Command to conjoin parallel transitions which come from one state and go to
 * another (could be the same state, in which case loops are being conjoined).
 * Part of the NFA to Regexp translation.
 */
public class ConjoinParallelTransitionsCommand extends CompositeCommand {

    private final AutomatonState mFrom;
    private final AutomatonState mTo;
    private final ArrayList<AutomatonTransition> mParallelTrans;
    private final AutomatonTransition mNewTransition;

    public ConjoinParallelTransitionsCommand(Automaton automaton,
            AutomatonState from, AutomatonState to)
    {
        super(automaton);
        mFrom = from;
        mTo = to;

        // Create a list of all parallel transitions originating from this
        // state. Also create a list of the BasicRegexp of these operands.
        mParallelTrans = new ArrayList<>();
        ArrayList<BasicRegexp> operands = new ArrayList<>();
        for (AutomatonTransition t : automaton.getStateTransitions(mFrom)) {
            if (t.getTo() == mTo) {
                mParallelTrans.add(t);
                super.commands.add(new RemoveTransitionCommand(automaton, t));
                operands.add(t.getData());
            }
        }

        BasicRegexp newRe = new BasicRegexp(operands,
                BasicRegexp.RegexpOperator.CHOICE);
        // Do very low depth optimisation
        newRe = newRe.optimise(BasicRegexp.OPTIMISE_ALL, 1);
        mNewTransition = automaton.createNewTransition(mFrom, mTo,
                newRe);
        super.commands.add(new AddTransitionCommand(automaton, mNewTransition));
    }

    /**
     * @return The list of parallel transitions we are conjoining.
     */
    public List<AutomatonTransition> getParallelTransitions()
    {
        return Collections.unmodifiableList(mParallelTrans);
    }

    /**
     * @return The transition which is the the result of conjoining
     */
    public AutomatonTransition getNewTransition()
    {
        return mNewTransition;
    }

    /**
     * @return The state of which we are conjoining parallel transitions from.
     */
    public AutomatonState getStateFrom()
    {
        return mFrom;
    }

    /**
     * @return The state of which we are conjoining parallel transitions to.
     */
    public AutomatonState getStateTo()
    {
        return mTo;
    }

}
