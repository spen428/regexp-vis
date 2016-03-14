package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

/**
 * Command to conjoin parallel transitions which come from one state and go to
 * another (could be the same state, in which case loops are being conjoined).
 * Part of the NFA to Regexp translation.
 */
public class ConjoinParallelTransitionsCommand extends Command {
    private final ArrayList<Command> mCommands;
    private final AutomatonState mFrom;
    private final AutomatonState mTo;

    public ConjoinParallelTransitionsCommand(Automaton automaton,
            AutomatonState from, AutomatonState to)
    {
        super(automaton);
        mCommands = new ArrayList<>();
        mFrom = from;
        mTo = to;

        // Create a list of all parallel transitions originating from this
        // state. Also create a list of the BasicRegexp of these operands.
        ArrayList<AutomatonTransition> parallelTrans = new ArrayList<>();
        ArrayList<BasicRegexp> operands = new ArrayList<>();
        for (AutomatonTransition t : automaton.getStateTransitions(mFrom)) {
            if (t.getTo() == mTo) {
                parallelTrans.add(t);
                mCommands.add(new RemoveTransitionCommand(automaton, t));
                operands.add(t.getData());
            }
        }

        BasicRegexp newRe = new BasicRegexp(operands,
                BasicRegexp.RegexpOperator.CHOICE);
        // Do very low depth optimisation
        newRe = newRe.optimise(BasicRegexp.OPTIMISE_ALL, 1);
        AutomatonTransition newTrans = automaton.createNewTransition(mFrom, mTo,
                newRe);
        mCommands.add(new AddTransitionCommand(automaton, newTrans));
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

    /**
     * @return the list of commands which this command executes, as an
     * unmodifiable list
     */
    public List<Command> getCommands()
    {
        return Collections.unmodifiableList(mCommands);
    }

    @Override
    public void undo()
    {
        ListIterator<Command> it = mCommands.listIterator(mCommands.size());
        while (it.hasPrevious()) {
            Command c = it.previous();
            c.undo();
        }
    }

    @Override
    public void redo()
    {
        for (Command c : mCommands) {
            c.redo();
        }
    }

}
