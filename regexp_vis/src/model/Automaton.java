package model;

import java.util.*;

public class Automaton {
    // Essentially this represents the graph, the key is the automaton
    // state and the values are the transitions from this state
    // TODO(mjn33): maybe a Set instead of a LinkedList?
    private HashMap<AutomatonState, LinkedList<AutomatonTransition>> mGraph;
    // The state to start in, not stating the end state, since that
    // will be handled by isFinal() in AutomatonState
    private AutomatonState mStartState;
    // Counter to give AutomatonStates unique IDs
    private int mCounter;

    public Automaton()
    {
        mGraph = new HashMap<>();
        mStartState = new AutomatonState(mCounter++);
        mGraph.put(mStartState, new LinkedList<AutomatonTransition>());
    }

    public AutomatonState getStartState()
    {
        return mStartState;
    }

    public AutomatonState addState()
    {
        AutomatonState newState = new AutomatonState(mCounter++);
        mGraph.put(newState, new LinkedList<AutomatonTransition>());
        return newState;
    }

    public AutomatonState setStateIsFinal(AutomatonState state, boolean isFinal)
    {
        if (state.isFinal() == isFinal)
            return state; // No op

        LinkedList<AutomatonTransition> transitions = mGraph.remove(state);
        AutomatonState newState = new AutomatonState(state.getId(), isFinal);
        mGraph.put(newState, transitions);
        return newState;
    }

    /**
     * Removes the specified state and all the incoming and outgoing
     * transitions
     *
     * TODO(mjn33): Document more
     *
     * Relatively expensive operation, O(s+t)? s = states, t =
     * transitions? Not yet benchmarked...
     */
    public void removeStateOLD(AutomatonState state)
    {
        // TODO(mjn33): Implement
    }

    /**
     * Add a transition from one state to another
     *
     * @param from The state the transition is from
     * @param to   The state the transition is to
     * @param data TODO(mjn33): WIP, extra data for the transition
     *
     * @return The newly created transition object, null otherwise if
     * such a transition already existed
     */
    public AutomatonTransition addTransition(AutomatonState from, AutomatonState to, Object data)
    {
        LinkedList<AutomatonTransition> transitions = mGraph.get(from);
        if (transitions == null)
            throw new RuntimeException("The specified state \"from\" is not part of this automaton, cannot add " +
                                       "transition");

        // Check a transition doesn't already exist
        boolean alreadyExists = false;
        for (AutomatonTransition t : transitions) {
            // FIXME(mjn33): Implement .equals()?
            if (t.getFrom() == from && t.getTo() == to && t.getData() == data) {
                alreadyExists = true;
                break;
            }
        }
        if (alreadyExists)
            return null;

        AutomatonTransition newTransition = new AutomatonTransition(from, to, data);
        transitions.addLast(newTransition);
        return newTransition;
    }

    /**
     * Remove the specified transition which goes from one state to another
     *
     * @param from The state the transition is from
     * @param to   The state the transition is to
     *
     * @return True if there was previously a transition there,
     * otherwise false
     */
    public boolean removeTransition(AutomatonState from, AutomatonState to)
    {
        LinkedList<AutomatonTransition> transitions = mGraph.get(from);
        if (transitions == null)
            throw new RuntimeException("The specified state \"from\" is not part of this automaton, cannot remove " +
                                       "transition");

        // Check if transition exists and remove it if it does
        Iterator<AutomatonTransition> it = transitions.iterator();
        while (it.hasNext()) {
            AutomatonTransition t = it.next();
            if (t.getFrom() == from && t.getTo() == to) {
                it.remove();
                return true;
            }
        }

        return false;
    }

    public void debugPrint()
    {
        System.out.println("Automaton {");
        System.out.println("    counter = " + mCounter);
        System.out.println("    start state (id) " + mStartState.getId());
        for (Map.Entry<AutomatonState, LinkedList<AutomatonTransition>> e : mGraph.entrySet()) {
            AutomatonState state = e.getKey();
            LinkedList<AutomatonTransition> transitions = e.getValue();
            System.out.println("    AutomatonState {");
            System.out.println("        id = " +  state.getId());
            System.out.println("        is final = " +  state.isFinal());
            System.out.println("        transitions = [");
            for (AutomatonTransition t : transitions) {
                System.out.println("            AutomatonTransition {");
                System.out.println("                from (id) = " + t.getFrom().getId());
                System.out.println("                to (id) = " + t.getTo().getId());
                System.out.println("                data = " + t.getData().toString());
                System.out.println("            },");
            }
            System.out.println("        ]");
            System.out.println("    }");
        }
        System.out.println("}");
    }

    // Methods for modifying via "Command"

    public AutomatonState createNewState()
    {
        return new AutomatonState(mCounter++);
    }

    public AutomatonTransition createNewTransition(AutomatonState from, AutomatonState to, Object data)
    {
        return new AutomatonTransition(from, to, data);
    }

    void addStateWithTransitions(AutomatonState state, LinkedList<AutomatonTransition> transitions)
    {
        if (mGraph.containsKey(state))
            throw new RuntimeException("The specified state already exists");

        mGraph.put(state, transitions);
    }

    LinkedList<AutomatonTransition> removeState(AutomatonState state)
    {
        if (!mGraph.containsKey(state))
            throw new RuntimeException("The specified state doesn't exist");

        return mGraph.remove(state);
    }

    void addTransition(AutomatonTransition transition)
    {
        AutomatonState from = transition.getFrom();
        AutomatonState to = transition.getTo();
        Object data = transition.getData();
        LinkedList<AutomatonTransition> transitions = mGraph.get(from);
        if (transitions == null)
            throw new RuntimeException("The specified state \"from\" is not part of this automaton, cannot add " +
                                       "transition");

        // Check a transition doesn't already exist
        for (AutomatonTransition t : transitions) {
            // FIXME(mjn33): Implement .equals()?
            if (t.getFrom() == from && t.getTo() == to && t.getData() == data) {
                throw new RuntimeException("The specified transitions already exists in this automaton");
            }
        }

        transitions.addLast(transition);
    }

    void removeTransition(AutomatonTransition transition)
    {
        AutomatonState from = transition.getFrom();
        AutomatonState to = transition.getTo();
        Object data = transition.getData();
        LinkedList<AutomatonTransition> transitions = mGraph.get(from);
        if (transitions == null)
            throw new RuntimeException("The specified state \"from\" is not part of this automaton, cannot remove " +
                                       "transition");

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

    // NOTE(mjn33): Note to self: Ideas, some may be useful, others
    // not:
    // * removeUnreachableTransitions
    // Maybe here, maybe somewhere else
}
