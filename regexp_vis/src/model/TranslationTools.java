package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class TranslationTools {
    /**
     * Factory function, creates the appropriate breakdown command for a
     * transition.
     *
     * @param automaton The automaton the transition belongs to
     * @param transition The transition to break down
     * @return The command to break down this transition, or null if this
     * transition cannot be broken down further
     */
    public static BreakdownCommand createBreakdownCommand(Automaton automaton,
        AutomatonTransition transition)
    {
        if (automaton == null) {
            throw new IllegalArgumentException("Automaton cannot be null");
        } else if (transition == null) {
            throw new IllegalArgumentException(
                "AutomatonTransition cannot be null");
        }

        BasicRegexp re = (BasicRegexp)transition.getData();
        switch (re.getOperator()) {
        case NONE:
            return null; // No operator to breakdown, nothing to do
        case STAR:
        case PLUS:
            return new BreakdownIterationCommand(automaton, transition,
                    BreakdownIterationCommand.calcBestIsolationLevel(automaton,
                            transition));
        case OPTION:
            return new BreakdownOptionCommand(automaton, transition);
        case SEQUENCE:
            return new BreakdownSequenceCommand(automaton, transition);
        case CHOICE:
            return new BreakdownChoiceCommand(automaton, transition);
        default:
            throw new UnsupportedOperationException(
                "BUG: Translation not implemented for operator "
                            + re.getOperator().toString());
        }
    }

    /**
     * Creates BreakdownCommand(s) for all transitions in this automaton. After
     * executing these commands there may be more transitions to breakdown. Null
     * is returned if there are no more transitions to breakdown.
     *
     * @param automaton The automaton to breakdown the transitions for
     * @return The list of BreakdownCommands to breakdown all transitions
     */
    public static List<Command> breakdownAllTransitions(Automaton automaton)
    {
        ArrayList<AutomatonTransition> allTodoTrans = new ArrayList<>();

        Iterator<Automaton.StateTransitionsPair> it = automaton.graphIterator();
        while (it.hasNext()) {
            Automaton.StateTransitionsPair pair = it.next();
            List<AutomatonTransition> stateTrans = pair.getTransitions();
            for (AutomatonTransition t : stateTrans) {
                if (!t.getData().isSingleChar()) {
                    allTodoTrans.add(t);
                }
            }
        }

        if (allTodoTrans.isEmpty()) {
            return null;
        }

        ArrayList<Command> commands = new ArrayList<>();
        for (AutomatonTransition t : allTodoTrans) {
            commands.add(createBreakdownCommand(automaton, t));
        }

        return commands;
    }

    /**
     * @param automaton The automaton of the state
     * @param state The state in question
     * @return True if a state has out-going epsilon transitions, false
     * otherwise
     */
    public static boolean stateHasEpsilonTransitions(Automaton automaton,
            AutomatonState state)
    {
        List<AutomatonTransition> stateTrans = automaton
                .getStateTransitions(state);
        for (AutomatonTransition t : stateTrans) {
            BasicRegexp re = t.getData();
            if (re.isSingleChar() && re.getChar() == BasicRegexp.EPSILON_CHAR) {
                return true;
            }
        }

        return false;
    }

    /**
     * @param automaton
     * @return True if the automaton has any epsilon transitions, false
     * otherwise
     */
    public static boolean automatonHasEpsilonTransitions(Automaton automaton)
    {
        Iterator<Automaton.StateTransitionsPair> it = automaton.graphIterator();
        while (it.hasNext()) {
            Automaton.StateTransitionsPair pair = it.next();
            List<AutomatonTransition> stateTrans = pair.getTransitions();
            for (AutomatonTransition t : stateTrans) {
                BasicRegexp re = t.getData();
                if (re.isSingleChar()
                        && re.getChar() == BasicRegexp.EPSILON_CHAR) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Part of calcEpsilonReachableStates(), for a state it discovers epsilon
     * reachable states we haven't visited yet.
     *
     * @param automaton The automaton the state belongs to
     * @param state The state to use to look for adjacent nodes connected
     * through epsilon transitions
     * @param newDiscovered The set which this method will add newly discovered
     * states to
     * @param visited The set of states which we have already visited, and thus
     * will not be added to the "newDiscovered" set
     */
    private static void discoverEpsilonReachableStates(Automaton automaton,
            AutomatonState state, Set<AutomatonState> newDiscovered,
            Set<AutomatonState> visited)
    {
        List<AutomatonTransition> transitions = automaton
                .getStateTransitions(state);
        for (AutomatonTransition t : transitions) {
            BasicRegexp re = (BasicRegexp) t.getData();
            AutomatonState to = t.getTo();
            if (re.getChar() == BasicRegexp.EPSILON_CHAR
                    && !visited.contains(to)) {
                newDiscovered.add(t.getTo());
            }
        }
    }

    /**
     * @param automaton The automaton the state belongs to
     * @param state The state which we want to find the epsilon closure of
     * @return The set of states which are in this state's epsilon closure
     * (including the state itself)
     */
    public static Set<AutomatonState> calcEpsilonReachableStates(
            Automaton automaton, AutomatonState state)
    {
        Set<AutomatonState> visited = new HashSet<>();
        Set<AutomatonState> discovered = new HashSet<>();
        discovered.add(state);
        Set<AutomatonState> newDiscovered = new HashSet<>();

        while (discovered.size() > 0) {
            visited.addAll(discovered);

            for (AutomatonState s2 : discovered) {
                discoverEpsilonReachableStates(automaton, s2, newDiscovered,
                        visited);
            }

            // Efficiency trick, just swap the references and clear the old set
            // for reuse as the new set
            Set<AutomatonState> tmpSwap = discovered;
            discovered = newDiscovered;
            newDiscovered = tmpSwap;
            newDiscovered.clear();

        }

        return visited;
    }
}
