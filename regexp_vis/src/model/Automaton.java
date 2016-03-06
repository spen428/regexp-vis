package model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class which represents a non-deterministic finite automaton (NFA) or
 * deterministic finite automaton.
 */
public class Automaton {

    private static final Logger LOGGER = Logger
            .getLogger(Automaton.class.toString());

    public static class StateTransitionsPair {
        private final AutomatonState mState;
        private final LinkedList<AutomatonTransition> mTransitions;

        private StateTransitionsPair(AutomatonState s)
        {
            mState = s;
            mTransitions = new LinkedList<>();
        }

        private StateTransitionsPair(AutomatonState s,
            LinkedList<AutomatonTransition> transitions)
        {
            mState = s;
            mTransitions = transitions;
        }

        /**
         * @return The automaton state
         */
        public AutomatonState getState()
        {
            return mState;
        }

        /**
         * @return The transitions for this state, as an unmodifiable list
         */
        public List<AutomatonTransition> getTransitions()
        {
            return Collections.unmodifiableList(mTransitions);
        }
    }

    private class GraphIterator implements Iterator<StateTransitionsPair>
    {
        private Iterator<Map.Entry<Integer, StateTransitionsPair>> mEntrySetIterator;

        private GraphIterator()
        {
            mEntrySetIterator = mGraph.entrySet().iterator();
        }

        public boolean hasNext()
        {
            return mEntrySetIterator.hasNext();
        }

        public StateTransitionsPair next()
        {
            Map.Entry<Integer,StateTransitionsPair> entry =
                mEntrySetIterator.next();
            return entry.getValue();
        }

        public void remove()
        {
            throw new UnsupportedOperationException("remove() not supported.");
        }
    }

    // Essentially this represents the graph, the key is the ID of the
    // AutomatonState and the values are a pair containing the state and its
    // out-going transitions
    private HashMap<Integer, StateTransitionsPair> mGraph;
    // The state to start in, not stating the end state, since that will be
    // handled by isFinal() in AutomatonState
    private AutomatonState mStartState;
    // Counter to give AutomatonState(s) unique IDs
    private int mCounter;
    // Counter to give AutomatonTransition(s) unique IDs
    private int mTransCounter;

    public Automaton()
    {
        clear();
    }

    /**
     * Clear all states and transitions, and reset counters.
     */
    public void clear() {
        mTransCounter = 0;
        mCounter = 0;
        mGraph = new HashMap<>();
        mStartState = new AutomatonState(mCounter++);
        mGraph.put(mStartState.getId(), new StateTransitionsPair(mStartState));
    }

    /**
     * @return The start state for this automaton.
     */
    public AutomatonState getStartState()
    {
        return mStartState;
    }

    /**
     * @return The number of states in this automaton.
     */
    public int getNumStates()
    {
        return mGraph.size();
    }

    /**
     * For debugging purposes, prints out the current state of the Automaton.
     */
    public void debugPrint()
    {
        LOGGER.log(Level.FINE, "Automaton {");
        LOGGER.log(Level.FINE, "    counter = " + mCounter);
        LOGGER.log(Level.FINE, "    start state (id) " + mStartState.getId());
        for (Map.Entry<Integer, StateTransitionsPair> e : mGraph.entrySet()) {
            AutomatonState state = e.getValue().mState;
            LinkedList<AutomatonTransition> transitions = e.getValue().mTransitions;
            LOGGER.log(Level.FINE, "    AutomatonState {");
            LOGGER.log(Level.FINE, "        id = " +  state.getId());
            LOGGER.log(Level.FINE, "        is final = " +  state.isFinal());
            LOGGER.log(Level.FINE, "        transitions = [");
            for (AutomatonTransition t : transitions) {
                int fromId = t.getFrom().getId();
                int toId = t.getTo().getId();
                String strData = t.getData().toString();
                LOGGER.log(Level.FINE, "            AutomatonTransition {");
                LOGGER.log(Level.FINE, "                from (id) = " + fromId);
                LOGGER.log(Level.FINE, "                to (id) = " + toId);
                LOGGER.log(Level.FINE, "                data = " + strData);
                LOGGER.log(Level.FINE, "            },");
            }
            LOGGER.log(Level.FINE, "        ]");
            LOGGER.log(Level.FINE, "    }");
        }
        LOGGER.log(Level.FINE, "}");
    }

    /**
     * @param state The state to check
     * @return Whether the state exists as part of this automaton
     */
    public boolean stateExists(AutomatonState state)
    {
        StateTransitionsPair pair = mGraph.get(state.getId());
        return pair != null && pair.mState == state;
    }

    /**
     * Utility function to be used in preference to mGraph.get(), checks that
     * StateTransitionsPair.mState == state to prevent performing operations
     * using foreign duplicate states. See the "_duplicate" unit tests of the
     * AutomatonTest class.
     *
     * @param state The state to lookup
     * @return The corresponding StateTransitionPair for the state, null if this
     * state doesn't exist
     */
    private StateTransitionsPair lookupState(AutomatonState state)
    {
        StateTransitionsPair pair = mGraph.get(state.getId());
        if (pair != null && pair.mState == state) {
            return pair;
        } else {
            return null;
        }
    }

    /**
     * @param id The ID of the state to find
     * @return The AutomatonState with the given ID, or null if no such state
     * exists
     */
    public AutomatonState getStateById(int id)
    {
        StateTransitionsPair pair = mGraph.get(id);
        if (pair != null) {
            return pair.mState;
        } else {
            return null;
        }
    }

    /**
     * @param id The ID of the transition to find
     * @return The AutomatonTransition with the given ID, or null if no such
     * transition exists
     */
    public AutomatonTransition getTransitionById(int id)
    {
        // TODO: improve efficiency, possibly using a map to lookup based on ID
        for (StateTransitionsPair pair : mGraph.values()) {
            for (AutomatonTransition t : pair.mTransitions) {
                if (t.getId() == id) {
                    return t;
                }
            }
        }
        return null;
    }

    /**
     * @param state The state to check
     * @return true if this state has out-going transitions, false otherwise
     */
    public boolean hasOutgoingTransition(AutomatonState state)
    {
        StateTransitionsPair pair = lookupState(state);
        if (pair == null) {
            throw new RuntimeException(
                "The specified state \"state\" is not part of this " +
                "automaton.");
        }

        return !pair.mTransitions.isEmpty();
    }

    /**
     * @param state The state to check
     * @return true if this state has in-going transitions, false otherwise
     */
    public boolean hasIngoingTransition(AutomatonState state)
    {
        if (!stateExists(state)) {
            throw new RuntimeException(
                "The specified state \"state\" is not part of this " +
                "automaton.");
        }

        for (Map.Entry<Integer, StateTransitionsPair> e : mGraph.entrySet()) {
            LinkedList<AutomatonTransition> transitions =
                e.getValue().mTransitions;

            for (AutomatonTransition t : transitions) {
                if (t.getTo() == state) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * @param state The state in question
     * @return The in-going transitions for the specified state
     */
    public List<AutomatonTransition> getIngoingTransition(AutomatonState state)
    {
        if (!stateExists(state)) {
            throw new RuntimeException(
                "The specified state \"state\" is not part of this " +
                "automaton.");
        }

        LinkedList<AutomatonTransition> ret = new LinkedList<>();
        for (Map.Entry<Integer, StateTransitionsPair> e : mGraph.entrySet()) {
            LinkedList<AutomatonTransition> transitions =
                    e.getValue().mTransitions;

            for (AutomatonTransition t : transitions) {
                if (t.getTo() == state) {
                    ret.add(t);
                }
            }
        }

        return ret;
    }

    /**
     * @param state The state in question
     * @return The out-going transitions for the specified state, as an
     * unmodifiable list.
     */
    public List<AutomatonTransition> getStateTransitions(AutomatonState state)
    {
        StateTransitionsPair pair = lookupState(state);
        if (pair == null) {
            throw new RuntimeException("The specified state doesn't exist, " +
                                       "cannot get transitions.");
        }

        // Returning an unmodifiable list as the returned list really shouldn't
        // be modified separately, change this if it becomes an issue
        return Collections.unmodifiableList(pair.mTransitions);
    }

    /**
     * Create a new automaton state object for this Automaton, with a unique ID
     *
     * @return The newly created automaton state object
     */
    public AutomatonState createNewState()
    {
        return new AutomatonState(mCounter++);
    }

    /**
     * Create a new transition object for this Automaton
     *
     * @param from The state the transition is from
     * @param to The state the transition is to
     * @param data The data associated with this transition
     *
     * @return The newly created transition object
     */
    public AutomatonTransition createNewTransition(AutomatonState from,
        AutomatonState to, BasicRegexp data)
    {
        return new AutomatonTransition(mTransCounter++, from, to, data);
    }

    /**
     * Adds the specified state with the specified transitions. Doesn't copy the
     * specified LinkedList.
     *
     * @param state The state to add
     * @param transitions The outgoing transitions for this state
     */
    public void addStateWithTransitions(AutomatonState state,
        LinkedList<AutomatonTransition> transitions)
    {
        // Need to check the ID, otherwise we would overwrite the previous value
        if (mGraph.containsKey(state.getId())) {
            throw new RuntimeException("Attempted to insert duplicate state");
        }

        StateTransitionsPair pair = new StateTransitionsPair(state, transitions);
        mGraph.put(state.getId(), pair);
    }

    /**
     * Removes the specified state from the automaton
     *
     * @param state The state to remove
     *
     * @return The transitions previously associated with this state, that have
     * also been removed
     */
    public LinkedList<AutomatonTransition> removeState(AutomatonState state)
    {
        if (!stateExists(state)) {
            throw new RuntimeException("The specified state doesn't exist");
        }

        if (state.getId() == mStartState.getId()) {
            throw new RuntimeException("Cannot remove the start state.");
        }

        return mGraph.remove(state.getId()).mTransitions;
    }

    /**
     * Add a transition from one state to another
     *
     * @param transition The transition to add
     */
    public void addTransition(AutomatonTransition transition)
    {
        AutomatonState from = transition.getFrom();
        StateTransitionsPair pair = lookupState(from);
        if (pair == null) {
            throw new RuntimeException(
                "The specified state \"from\" is not part of this automaton, " +
                "cannot add transition");
        }

        // Check a transition doesn't already exist
        if (pair.mTransitions.contains(transition)) {
            throw new RuntimeException(
                "The specified transitions already exists in this automaton");
        }

        pair.mTransitions.addLast(transition);
    }

    /**
     * Add a transition from one state to another
     *
     * @param transition The transition to remove
     */
    public void removeTransition(AutomatonTransition transition)
    {
        AutomatonState from = transition.getFrom();
        StateTransitionsPair pair = lookupState(from);
        if (pair == null) {
            throw new RuntimeException(
                "The specified state \"from\" is not part of this automaton, " +
                "cannot remove transition");
        }

        if (!pair.mTransitions.remove(transition)) {
            // If were here, we didn't find the transition
            throw new RuntimeException(
                "The specified transition doesn't exist");
        }
    }

    /**
     * Provides an iterator over the graph, containing all state + transitions
     * pairs. Modification will result in an exception being thrown.
     *
     * @return The iterator
     */
    public Iterator<StateTransitionsPair> graphIterator()
    {
        return new GraphIterator();
    }

    public AutomatonState getAutomatonStateById(int id)
    {
        StateTransitionsPair pair = mGraph.get(id);
        if (pair == null) {
            return null;
        }

        return pair.getState();
    }

    public AutomatonTransition getAutomatonTransitionById(int id)
    {
        for (StateTransitionsPair pair : mGraph.values()) {
            for (AutomatonTransition t : pair.getTransitions()) {
                if (t.getId() == id) {
                    return t;
                }
            }
        }

        return null;
    }
}
