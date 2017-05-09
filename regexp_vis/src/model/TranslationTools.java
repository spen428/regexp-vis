/*
 * Copyright (c) 2015, 2016 Matthew J. Nicholls, Samuel Pengelly,
 * Parham Ghassemi, William R. Dix
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

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
     * Creates a list of all transitions that need to be broken down for
     * converting from regexp to NFA. Null is returned if there are no more
     * transitions to break down.
     *
     * @param automaton The automaton to breakdown the transitions for
     * @return The list of AutomatonTransition(s) that need to be broken down
     */
    public static List<AutomatonTransition> getAllTransitionsToBreakdown(
            Automaton automaton)
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
        } else {
            return allTodoTrans;
        }
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
     * Part of calcReachableStates(), for a state it discovers reachable states
     * through transitions, with a given predicate on the transitions.
     *
     * @param automaton The automaton the state belongs to
     * @param state The state to use to look for adjacent nodes connected
     * through transitions
     * @param newDiscovered The set which this method will add newly discovered
     * states to
     * @param visited The set of states which we have already visited, and thus
     * will not be added to the "newDiscovered" set
     * @param pred A predicate which tests if we should follow a specific
     * transition or not
     */
    private static void discoverReachableStates(Automaton automaton,
            AutomatonState state, Set<AutomatonState> newDiscovered,
            Set<AutomatonState> visited, Predicate<AutomatonTransition> pred)
    {
        List<AutomatonTransition> transitions = automaton
                .getStateTransitions(state);
        for (AutomatonTransition t : transitions) {
            if (pred.test(t) && !visited.contains(t.getTo())) {
                newDiscovered.add(t.getTo());
            }
        }
    }

    /**
     *
     * @param automaton The automaton the state belongs to
     * @param state The state which we want to start the search from
     * @param pred A predicate which tests if we should follow a specific
     * transition or not
     * @return The set of states which are reachable through the transitions
     * subject to the given predicate
     */
    public static Set<AutomatonState> calcReachableStates(Automaton automaton,
            AutomatonState state, Predicate<AutomatonTransition> pred)
    {
        Set<AutomatonState> visited = new HashSet<>();
        Set<AutomatonState> discovered = new HashSet<>();
        discovered.add(state);
        Set<AutomatonState> newDiscovered = new HashSet<>();

        while (discovered.size() > 0) {
            visited.addAll(discovered);

            for (AutomatonState s2 : discovered) {
                discoverReachableStates(automaton, s2, newDiscovered,
                        visited, pred);
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

    /**
     * @param automaton The automaton in question
     * @return The set of unreachable states for this automaton, the set is
     * empty if all states are reachable
     */
    public static Set<AutomatonState> automatonCalcUnreachableStates(
            Automaton automaton)
    {
        Set<AutomatonState> allStates = new HashSet<>();
        Iterator<Automaton.StateTransitionsPair> it = automaton.graphIterator();
        while (it.hasNext()) {
            Automaton.StateTransitionsPair pair = it.next();
            allStates.add(pair.getState());
        }

        Set<AutomatonState> reachable = calcReachableStates(automaton,
                automaton.getStartState(), t -> true);

        allStates.removeAll(reachable);
        return allStates;
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
        return calcReachableStates(automaton, state,
                t -> t.getData().getChar() == BasicRegexp.EPSILON_CHAR);
    }

    /**
     * Returns the list of character transitions which are non-deterministic,
     * sorted.
     *
     * @param automaton The automaton the state belongs to
     * @param state The state to check for non-deterministic transitions
     * @return A list of characters for which transitions are non-deterministic,
     * if there is no non-determinism this list is empty.
     */
    public static List<Character> calcNonDeterministicTrans(Automaton a,
            AutomatonState state)
    {
        HashSet<Character> nonDetSet = new HashSet<>();
        HashSet<Character> found = new HashSet<>();
        for (AutomatonTransition t : a.getStateTransitions(state)) {
            if (found.contains(t.getData().getChar())) {
                nonDetSet.add(t.getData().getChar());
            } else {
                found.add(t.getData().getChar());
            }
        }

        // Turn the set into a list which we then sort (consistent ordering in
        // menu for example)
        ArrayList<Character> list = new ArrayList<>(nonDetSet);
        Collections.sort(list);
        return list;
    }

    /**
     * @param automaton The automaton the state belongs to
     * @param state The state to check for non-deterministic transitions
     * @return True if the state has out-going non-deterministic transition(s),
     * false otherwise
     */
    public static boolean stateHasNonDeterminism(Automaton automaton,
            AutomatonState state)
    {
        List<AutomatonTransition> trans = automaton.getStateTransitions(state);
        HashSet<Character> found = new HashSet<>();
        for (AutomatonTransition t : trans) {
            if (found.contains(t.getData().getChar())) {
                return true;
            } else {
                found.add(t.getData().getChar());
            }
        }

        return false;
    }

    /**
     * @param automaton The automaton in question
     * @return True if the automaton has any state which has out-going
     * non-deterministic transition(s), false otherwise
     */
    public static boolean automatonHasNonDeterminism(Automaton automaton)
    {
        Iterator<Automaton.StateTransitionsPair> it = automaton.graphIterator();
        while (it.hasNext()) {
            Automaton.StateTransitionsPair pair = it.next();
            if (stateHasNonDeterminism(automaton, pair.getState())) {
                return true;
            }
        }

        return false;
    }

    /**
     * @param automaton The automaton in question
     * @param from The state we are checking transitions from
     * @param to The state we are checking transitions to
     * @param c The character to check for transitions
     * @return Whether there exists a transition between the given states (only
     * single direction from "from")
     */
    public static boolean hasCharacterTrans(Automaton automaton,
            AutomatonState from, AutomatonState to, char c)
    {
        for (AutomatonTransition tmp : automaton.getStateTransitions(from)) {
            BasicRegexp tmpRe = (BasicRegexp)tmp.getData();
            if (tmp.getTo() == to && tmpRe.isSingleChar() &&
                    tmpRe.getChar() == c) {
                return true;
            }
        }

        return false;
    }

    public static boolean stateHasLoop(Automaton automaton,
            AutomatonState state)
    {
        return automaton.getStateTransitions(state)
                .stream()
                .filter(t -> t.getTo() == state)
                .findAny()
                .isPresent();
    }

    public static boolean automatonHasFinalState(Automaton automaton)
    {
        Iterator<Automaton.StateTransitionsPair> it = automaton.graphIterator();
        while (it.hasNext()) {
            Automaton.StateTransitionsPair pair = it.next();
            AutomatonState state = pair.getState();
            if (state.isFinal()) {
                return true;
            }
        }

        return false;
    }

    /**
     * @param automaton The automaton of the given state
     * @param state The state in question
     * @return Null if the given state doesn't have exactly one loop onto
     * itself, otherwise it returns the AutomatonTransition for the single loop.
     */
    public static AutomatonTransition getSingleLoop(Automaton automaton,
            AutomatonState state)
    {
        AutomatonTransition loopTrans = null;
        for (AutomatonTransition t : automaton.getStateTransitions(state)) {
            if (t.getTo() == state) {
                if (loopTrans == null) {
                    loopTrans = t;
                } else {
                    // Multiple looped transitions
                    return null;
                }
            }
        }

        return loopTrans;
    }

    /**
     * @param automaton The automaton of the given state
     * @param state The state in question
     * @param to A specific target state to check for parallel transitions. If
     * null, any state is considered.
     * @return True if there exists out-going parallel transitions from the
     * given state to the target state.
     */
    public static boolean stateHasParallelTrans(Automaton automaton,
            AutomatonState state, AutomatonState to)
    {
        HashSet<AutomatonState> found = new HashSet<>();
        for (AutomatonTransition t : automaton.getStateTransitions(state)) {
            if (found.contains(t.getTo())) {
                return true;
            } else if (to == null || to == t.getTo()) {
                found.add(t.getTo());
            }
        }

        return false;
    }
}
