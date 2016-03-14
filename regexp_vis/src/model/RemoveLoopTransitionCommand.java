package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

/**
 * Command to a single loop for a state, this is done by prepending the loop
 * expression wrapped in a STAR operator. Part of the NFA to Regexp translation.
 *
 * Note this Command assumes the only loop transition is the given one.
 */
public class RemoveLoopTransitionCommand extends Command {
    private final ArrayList<Command> mCommands;
    private final AutomatonTransition mTransition;

    public RemoveLoopTransitionCommand(Automaton automaton,
            AutomatonTransition trans)
    {
        super(automaton);
        mCommands = new ArrayList<>();
        mTransition = trans;

        AutomatonState state = mTransition.getFrom();
        // Wrap the loop regular expression into a STAR operator
        BasicRegexp loopRe = mTransition.getData();
        loopRe = new BasicRegexp(loopRe, BasicRegexp.RegexpOperator.STAR);

        // Actually remove the loop
        mCommands.add(new RemoveTransitionCommand(automaton, mTransition));

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
            mCommands.add(new RemoveTransitionCommand(automaton, t));
            mCommands.add(new AddTransitionCommand(automaton, newTrans));
        }
    }

    /**
     * @return Returns the loop transition which is removed.
     */
    public AutomatonTransition getLoopTransition()
    {
        return mTransition;
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
