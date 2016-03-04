package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

/**
 * Command to remove a state and sequence the in-going transitions for that
 * state with the out-going transitions for that state. Part of the NFA to
 * Regexp translation.
 */
public class SequenceStateTransitionsCommand extends Command {
    private final ArrayList<Command> mCommands;
    private final AutomatonState mState;

    public SequenceStateTransitionsCommand(Automaton automaton,
            AutomatonState state)
    {
        super(automaton);
        mCommands = new ArrayList<>();
        mState = state;

        List<AutomatonTransition> ingoingTrans = automaton
                .getIngoingTransition(mState);
        List<AutomatonTransition> outgoingTrans = automaton
                .getStateTransitions(mState);

        // For every combination of pairing in-going with out-going transitions
        // for this state: combine the regexps of the transitions using sequence
        // via a transition which bypasses this state.
        for (AutomatonTransition t1 : ingoingTrans) {
            for (AutomatonTransition t2 : outgoingTrans) {
                ArrayList<BasicRegexp> operands = new ArrayList<>();
                operands.add(t1.getData());
                operands.add(t2.getData());
                BasicRegexp newRe = new BasicRegexp(operands,
                        BasicRegexp.RegexpOperator.SEQUENCE);
                AutomatonTransition newTrans = automaton
                        .createNewTransition(t1.getFrom(), t2.getTo(), newRe);
                mCommands.add(new AddTransitionCommand(automaton, newTrans));
            }
        }

        // Remove all in-going transitions
        for (AutomatonTransition t : ingoingTrans) {
            mCommands.add(new RemoveTransitionCommand(automaton, t));
        }
        // Remove all out-going transitions
        for (AutomatonTransition t : outgoingTrans) {
            mCommands.add(new RemoveTransitionCommand(automaton, t));
        }
        // Remove the state itself
        mCommands.add(new RemoveStateCommand(automaton, mState));
    }

    /**
     * @return The state which we are removing.
     */
    public AutomatonState getState()
    {
        return mState;
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
