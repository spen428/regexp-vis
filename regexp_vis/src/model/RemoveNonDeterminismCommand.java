package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

/**
 * Command which removes the a non-deterministic transition for a state. For the
 * NFA to DFA conversion process.
 */
public class RemoveNonDeterminismCommand extends CompositeCommand {
    private final RemoveNonDeterminismContext mCtx;
    private final AutomatonState mState;
    private final char mChar;
    private final AddStateCommand mNewStateCommand;
    private final Set<AutomatonState> mReachableSet;

    public RemoveNonDeterminismCommand(RemoveNonDeterminismContext ctx,
            AutomatonState state, char c)
    {
        super(ctx.getAutomaton());
        mCtx = ctx;
        mState = state;
        mChar = c;

        Automaton automaton = getAutomaton();

        ArrayList<AutomatonTransition> oldTrans = new ArrayList<>();
        mReachableSet = new HashSet<>();
        for (AutomatonTransition t : automaton.getStateTransitions(mState)) {
            if (t.getData().getChar() == mChar) {
                oldTrans.add(t);
                mReachableSet.add(t.getTo());
            }
        }

        for (AutomatonTransition t : oldTrans) {
            super.commands.add(new RemoveTransitionCommand(automaton, t));
        }

        // ppSet = pre-processed set
        Set<AutomatonState> ppSet = mCtx.reachableToSet(mReachableSet);
        AutomatonState newState = mCtx.findStateFromSet(ppSet);
        boolean shouldAddState = false;
        if (newState == null) {
            newState = automaton.createNewState();
            mCtx.putStateBinding(newState, ppSet);
            shouldAddState = true;
        } else if (!automaton.stateExists(newState)) {
            shouldAddState = true;
        }

        // The new state which represents the set of states reachable through
        // the transition
        if (shouldAddState) {
            mNewStateCommand = new AddStateCommand(automaton, newState);
            super.commands.add(mNewStateCommand);
            // If we could reach a final state, we need to make this state final
            // as well
            for (AutomatonState s2 : ppSet) {
                if (s2.isFinal()) {
                    super.commands.add(new SetIsFinalCommand(automaton,
                            newState, true));
                    break;
                }
            }
        } else {
            mNewStateCommand = null;
        }

        // Link up this new state
        AutomatonTransition newTrans = automaton.createNewTransition(mState,
                newState, new BasicRegexp(mChar));
        super.commands.add(new AddTransitionCommand(automaton, newTrans));

        if (!shouldAddState) {
            // A state already exists for this set of states, no need add
            // anything more
            return;
        }

        // Collect out-going transitions for the set of states we can reach with
        // this transition
        ArrayList<AutomatonTransition> outgoingTrans = new ArrayList<>();
        for (AutomatonState s2 : mReachableSet) {
            for (AutomatonTransition t : automaton.getStateTransitions(s2)) {
                outgoingTrans.add(t);
            }
        }

        // Sort as the ordering for BasicRegexp defines but with additional
        // comparisons:
        //   * First: sort by which state the transitions go to
        //   * Then sort using the BasicRegexp(s) of the transitions
        //
        // This allows us to remove duplicate transitions easily
        Collections.sort(outgoingTrans, new Comparator<AutomatonTransition>() {
            @Override
            public int compare(AutomatonTransition t1, AutomatonTransition t2) {
                if (t1.getTo().getId() < t2.getTo().getId()) {
                    return -1;
                } else if (t1.getTo().getId() > t2.getTo().getId()) {
                    return 1;
                }

                return t1.getData().compareTo(t2.getData());
            }
        });

        // Remove duplicate transitions
        Iterator<AutomatonTransition> it = outgoingTrans.iterator();
        AutomatonTransition prev = null;
        while (it.hasNext()) {
            AutomatonTransition t = it.next();
            // Read: previous transition has the same transition expression and
            // destination state, this is a duplicate so remove it
            if (prev != null && prev.getTo() == t.getTo()
                && prev.getData().equals(t.getData())) {
                it.remove();
            } else {
                prev = t;
            }
        }

        // Create the transitions, but now always from the state we just created
        for (AutomatonTransition t : outgoingTrans) {
            AutomatonTransition tmp = automaton.createNewTransition(newState,
                    t.getTo(), t.getData());
            super.commands.add(new AddTransitionCommand(automaton, tmp));
        }
    }

    public RemoveNonDeterminismContext getCtx()
    {
        return mCtx;
    }

    /**
     * @return The state of which we are removing non-determinism from.
     */
    public AutomatonState getState()
    {
        return mState;
    }

    /**
     * @return The character which we are removing non-determinism for.
     */
    public char getChar()
    {
        return mChar;
    }

    /**
     * @return The command which adds a new state, or null if no state is added.
     */
    public AddStateCommand getNewStateCommand()
    {
        return mNewStateCommand;
    }

    /**
     * @return The set of reachable states via the non-deterministic transition
     * this command removes, as an unmodifiable set.
     */
    public Set<AutomatonState> getReachableSet()
    {
        return Collections.unmodifiableSet(mReachableSet);
    }

}
