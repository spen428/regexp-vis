package controller;

import java.util.LinkedList;
import java.util.List;

import model.AddStateCommand;
import model.Automaton;
import model.AutomatonState;
import model.AutomatonTransition;
import model.BreakdownCommand;
import model.Command;
import model.RemoveStateCommand;

/**
 * Some static methods that provide information about breakdown commands.
 * 
 * @author sp611
 *
 */
public class BreakdownUITools {

    /**
     * @return The number of occurrences of {@link AddStateCommand} in the given
     *         {@link Command}.
     */
    public static int getNumAddedStates(BreakdownCommand cmd) {
        int count = 0;
        for (Command c : cmd.getCommands()) {
            if (c instanceof AddStateCommand) {
                count++;
            }
        }
        return count;
    }

    /**
     * @return The number of occurrences of {@link RemoveStateCommand} in the
     *         given {@link Command}.
     */
    public static int getNumRemovedStates(BreakdownCommand cmd) {
        int count = 0;
        for (Command c : cmd.getCommands()) {
            if (c instanceof RemoveStateCommand) {
                count++;
            }
        }
        return count;
    }

    /**
     * Checks whether the original transition of the given
     * {@link BreakdownCommand} was one of a number of choices that go from
     * transition.from to transition.to.
     * 
     * @param cmd
     *            the command
     * @return true if the transition was part of a choice
     */
    public static boolean wasChoiceTransition(BreakdownCommand cmd) {
        final AutomatonTransition transition = cmd.getOriginalTransition();
        final List<AutomatonTransition> transitions = getTransitionsBetween(
                cmd.getAutomaton(), transition.getFrom(), transition.getTo());
        return (transitions.size() > 1 && transitions.contains(transition));
    }

    /**
     * Returns a list of all {@link AutomatonTransition} that go directly
     * between two given {@link AutomatonState}.
     * 
     * @param automaton
     *            the {@link Automaton} that the states and transitions belong
     *            to
     * @param from
     *            the 'from' state
     * @param to
     *            the 'to' state
     * @return the list
     */
    public static List<AutomatonTransition> getTransitionsBetween(
            Automaton automaton, AutomatonState from, AutomatonState to) {
        final List<AutomatonTransition> fromTrs = automaton
                .getStateTransitions(from);
        final List<AutomatonTransition> toTrs = automaton
                .getIngoingTransition(to);

        final List<AutomatonTransition> between = new LinkedList<AutomatonTransition>();
        for (AutomatonTransition f : fromTrs) {
            if (toTrs.contains(f)) {
                between.add(f);
            }
        }

        return between;
    }

}
