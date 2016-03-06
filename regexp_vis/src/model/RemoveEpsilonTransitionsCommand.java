package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

/**
 * Command which removes the out-going epsilon transitions for a state.
 * For the NFA to DFA conversion process.
 */
public class RemoveEpsilonTransitionsCommand extends CompositeCommand {
    private final AutomatonState mTargetState;

    public RemoveEpsilonTransitionsCommand(Automaton automaton, AutomatonState s)
    {
        super(automaton);
        mTargetState = s;

        Set<AutomatonState> reachable = TranslationTools
                .calcEpsilonReachableStates(automaton, mTargetState);

        // Check if we can reach a final state, in which case this state needs
        // to be made final as well
        for (AutomatonState s2 : reachable) {
            if (s2.isFinal()) {
                super.commands.add(new SetIsFinalCommand(automaton,
                        mTargetState, true));
            }
        }

        // Remove all out-going epsilon transitions
        List<AutomatonTransition> outgoingTrans = automaton
                .getStateTransitions(mTargetState);
        for (AutomatonTransition t : outgoingTrans) {
            if (t.getData().getChar() == BasicRegexp.EPSILON_CHAR) {
                super.commands.add(new RemoveTransitionCommand(automaton, t));
            }
        }

        // Find all transitions that we can do in the epsilon closure of this
        // state
        ArrayList<AutomatonTransition> trans = new ArrayList<>();
        for (AutomatonState s2 : reachable) {
            for (AutomatonTransition t : automaton.getStateTransitions(s2)) {
                if (t.getData().getChar() != BasicRegexp.EPSILON_CHAR) {
                    trans.add(t);
                }
            }
        }

        // Sort as the ordering for BasicRegexp defines but with additional
        // comparisons:
        //   * First: sort by which state the transitions go to
        //   * If the BasicRegexp's of two transitions are equal then the
        //     transitions which come from the original state are ordered first.
        //   * This is such that when we remove duplicates, we don't remove the
        //     transitions for the target state
        Collections.sort(trans, new Comparator<AutomatonTransition>() {
            @Override
            public int compare(AutomatonTransition t1, AutomatonTransition t2) {
                if (t1.getTo().getId() < t2.getTo().getId()) {
                    return -1;
                } else if (t1.getTo().getId() > t2.getTo().getId()) {
                    return 1;
                }

                int result = t1.getData().compareTo(t2.getData());
                if (result == 0 && t1.getFrom() != t2.getFrom()) {
                    if (t1.getFrom() == mTargetState) {
                        return -1;
                    } else if (t2.getFrom() == mTargetState) {
                        return 1;
                    } else {
                        return 0;
                    }
                } else {
                    return result;
                }
            }
        });

        // Remove duplicate transitions, keeping the transitions which originate
        // from the target state
        Iterator<AutomatonTransition> it = trans.iterator();
        AutomatonTransition prev = null;
        while (it.hasNext()) {
            AutomatonTransition t = it.next();
            // Read: previous transition has the same transition expression and
            // target state, but isn't from "mTargetState"
            if (prev != null && prev.getTo() == t.getTo()
                    && prev.getData().equals(t.getData())
                    && t.getFrom() != mTargetState) {
                it.remove();
            } else {
                prev = t;
            }
        }

        // Remove transitions that originate from the target state, otherwise we
        // would be adding duplicates
        it = trans.iterator();
        while (it.hasNext()) {
            AutomatonTransition t = it.next();
            if (t.getFrom() == mTargetState) {
                it.remove();
            }
        }

        // Create the transitions, but now always from the target state
        for (AutomatonTransition t : trans) {
            AutomatonTransition newTrans = automaton.createNewTransition(
                    mTargetState, t.getTo(), t.getData());
            super.commands.add(new AddTransitionCommand(automaton, newTrans));
        }
    }

    /**
     * @return The state of which we are removing out-going epsilon transitions.
     */
    public AutomatonState getTargetState()
    {
        return mTargetState;
    }

}
