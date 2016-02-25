package model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Class which manages the current state of removing non-determinism, currently
 * only stores mappings from a single state to a set of states and vice-versa.
 */
public class RemoveNonDeterminismContext {
    private final Automaton mAutomaton;
    private final Map<AutomatonState, Set<AutomatonState>> mStateSetMap;
    private final Set<AutomatonState> mOriginalStates;

    public RemoveNonDeterminismContext(Automaton automaton)
    {
        mAutomaton = automaton;
        mStateSetMap = new HashMap<>();
        mOriginalStates = new HashSet<>();

        // Set mOriginalStates to all the states we are starting off with
        Iterator<Automaton.StateTransitionsPair> it = mAutomaton.graphIterator();
        while (it.hasNext()) {
            Automaton.StateTransitionsPair pair = it.next();
            mOriginalStates.add(pair.getState());
        }
    }

    /**
     * Given a set of states, find the state which represents that set. Returns
     * null if no such state exists. It is expected that all states in the given
     * set are also in the mOriginalStates set.
     *
     * @param set The set of states in question
     * @return The state which represents the given set, or null if no such
     * state exists
     */
    public AutomatonState findStateFromSet(Set<AutomatonState> set)
    {
        for (Map.Entry<AutomatonState, Set<AutomatonState>> entry : mStateSetMap
                .entrySet()) {
            if (set.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * @return Get the automaton associated with this context
     */
    public Automaton getAutomaton()
    {
        return mAutomaton;
    }

    public Set<AutomatonState> lookupStateBinding(AutomatonState state)
    {
        return mStateSetMap.get(state);
    }

    /**
     * Turn a set of reachable states into a set containing only states that are
     * also in mOriginalStates. E.g. state 1 = {A, B}, if we can reach states
     * {1, C} that will be translated to {A, B, C}
     *
     * @param reachable A set of reachable states to process
     * @return The set in terms of only the original states
     */
    public Set<AutomatonState> reachableToSet(Set<AutomatonState> reachable)
    {
        HashSet<AutomatonState> ppSet = new HashSet<>();

        for (AutomatonState s : reachable) {
            Set<AutomatonState> tmp = mStateSetMap.get(s);
            if (tmp != null) {
                ppSet.addAll(tmp);
            } else {
                ppSet.add(s);
            }
        }

        return ppSet;
    }

    public void putStateBinding(AutomatonState s, Set<AutomatonState> set)
    {
        mStateSetMap.put(s, set);
    }

    public void removeStateBinding(AutomatonState s)
    {
        mStateSetMap.remove(s);
    }
}
