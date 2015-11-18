package model;

import java.util.*;

/**
 * Class which represents a non-deterministic finite automaton (NFA)
 * or deterministic finite automaton.
 */
public class Automaton {
    // Essentially this represents the graph, the key is the automaton
    // state and the values are the transitions from this state
    // IDEA(mjn33): maybe a Set instead of a LinkedList?
    private HashMap<AutomatonState, LinkedList<AutomatonTransition>> mGraph;
    // The state to start in, not stating the end state, since that
    // will be handled by isFinal() in AutomatonState
    private AutomatonState mStartState;
    // Counter to give AutomatonState(s) unique IDs
    private int mCounter;
    // Counter to give AutomatonTransition(s) unique IDs
    private int mTransCounter;

    public Automaton()
    {
        mGraph = new HashMap<>();
        mStartState = new AutomatonState(mCounter++);
        mGraph.put(mStartState, new LinkedList<AutomatonTransition>());
    }

    /**
     * @return The start state for this automaton.
     */
    public AutomatonState getStartState()
    {
        return mStartState;
    }

    /**
     * For debugging purposes, prints out the current state of the
     * Automaton.
     */
    public void debugPrint()
    {
        System.out.println("Automaton {");
        System.out.println("    counter = " + mCounter);
        System.out.println("    start state (id) " + mStartState.getId());
        for (Map.Entry<AutomatonState, LinkedList<AutomatonTransition>> e :
                 mGraph.entrySet()) {
            AutomatonState state = e.getKey();
            LinkedList<AutomatonTransition> transitions = e.getValue();
            System.out.println("    AutomatonState {");
            System.out.println("        id = " +  state.getId());
            System.out.println("        is final = " +  state.isFinal());
            System.out.println("        transitions = [");
            for (AutomatonTransition t : transitions) {
                int fromId = t.getFrom().getId();
                int toId = t.getTo().getId();
                String strData = t.getData().toString();
                System.out.println("            AutomatonTransition {");
                System.out.println("                from (id) = " + fromId);
                System.out.println("                to (id) = " + toId);
                System.out.println("                data = " + strData);
                System.out.println("            },");
            }
            System.out.println("        ]");
            System.out.println("    }");
        }
        System.out.println("}");
    }

    /**
     * @param state The state to check
     * @return Whether the state exists as part of this automaton
     */
    public boolean stateExists(AutomatonState state)
    {
        return mGraph.containsKey(state);
    }

    /**
     * @param state The state to check
     * @return true if this state has out-going transitions, false
     * otherwise
     */
    public boolean hasOutgoingTransition(AutomatonState state)
    {
        LinkedList<AutomatonTransition> transitions = mGraph.get(state);
        if (transitions == null) {
            throw new RuntimeException(
                "The specified state \"state\" is not part of this " +
                "automaton.");
        }

        return !transitions.isEmpty();
    }

    /**
     * @param state The state to check
     * @return true if this state has in-going transitions, false
     * otherwise
     */
    public boolean hasIngoingTransition(AutomatonState state)
    {
        for (Map.Entry<AutomatonState, LinkedList<AutomatonTransition>> e :
            mGraph.entrySet()) {
            LinkedList<AutomatonTransition> transitions = e.getValue();

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
        LinkedList<AutomatonTransition> ret = new LinkedList<>();
        for (Map.Entry<AutomatonState, LinkedList<AutomatonTransition>> e :
            mGraph.entrySet()) {
            LinkedList<AutomatonTransition> transitions = e.getValue();

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
     * @return The out-going transitions for the specified state, as
     * an unmodifiable list.
     */
    public List<AutomatonTransition> getStateTransitions(AutomatonState state)
    {
        LinkedList<AutomatonTransition> transitions = mGraph.get(state);
        if (transitions == null) {
            throw new RuntimeException("The specified state doesn't exist, " +
                                       "cannot get transitions.");
        }

        // Returning an unmodifiable list as the returned list really
        // shouldn't be modified separately, change this if it becomes
        // an issue
        return Collections.unmodifiableList(transitions);
    }

    /**
     * Create a new automaton state object for this Automaton, with a
     * unique ID
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
        AutomatonState to, Object data)
    {
        return new AutomatonTransition(mTransCounter++, from, to, data);
    }

    /**
     * Adds the specified state with the specified
     * transitions. Doesn't copy the specified LinkedList.
     *
     * @param state The state to add
     * @param transitions The outgoing transitions for this state
     */
    public void addStateWithTransitions(AutomatonState state,
        LinkedList<AutomatonTransition> transitions)
    {
        if (mGraph.containsKey(state)) {
            throw new RuntimeException("The specified state already exists");
        }

        mGraph.put(state, transitions);
    }

    /**
     * Removes the specified state from the automaton
     *
     * @param state The state to remove
     *
     * @return The transitions previously associated with this state,
     * that have also been removed
     */
    public LinkedList<AutomatonTransition> removeState(AutomatonState state)
    {
        if (!mGraph.containsKey(state)) {
            throw new RuntimeException("The specified state doesn't exist");
        }

        return mGraph.remove(state);
    }

    /**
     * Add a transition from one state to another
     *
     * @param transition The transition to add
     */
    public void addTransition(AutomatonTransition transition)
    {
        AutomatonState from = transition.getFrom();
        AutomatonState to = transition.getTo();
        Object data = transition.getData();
        LinkedList<AutomatonTransition> transitions = mGraph.get(from);
        if (transitions == null) {
            throw new RuntimeException(
                "The specified state \"from\" is not part of this automaton, " +
                "cannot add transition");
        }

        // Check a transition doesn't already exist
        for (AutomatonTransition t : transitions) {
            // FIXME(mjn33): Implement .equals()?
            if (t.getFrom() == from && t.getTo() == to && t.getData() == data) {
                throw new RuntimeException(
                    "The specified transitions already exists in this " +
                    "automaton");
            }
        }

        transitions.addLast(transition);
    }

    /**
     * Add a transition from one state to another
     *
     * @param transition The transition to remove
     */
    public void removeTransition(AutomatonTransition transition)
    {
        AutomatonState from = transition.getFrom();
        AutomatonState to = transition.getTo();
        Object data = transition.getData();
        LinkedList<AutomatonTransition> transitions = mGraph.get(from);
        if (transitions == null) {
            throw new RuntimeException(
                "The specified state \"from\" is not part of this automaton, " +
                "cannot remove transition");
        }

        // Check if transition exists and remove it if it does
        Iterator<AutomatonTransition> it = transitions.iterator();
        while (it.hasNext()) {
            AutomatonTransition t = it.next();
            // FIXME(mjn33): Implement .equals()? Or use reference ==?
            if (t.getFrom() == from && t.getTo() == to && t.getData() == data) {
                it.remove();
                return;
            }
        }

        // If were here, we didn't find the transition
        throw new RuntimeException("The specified transition doesn't exist");
    }
}
