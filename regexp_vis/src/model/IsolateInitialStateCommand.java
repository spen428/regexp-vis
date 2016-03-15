package model;

import java.util.ArrayList;
import java.util.List;

public class IsolateInitialStateCommand extends CompositeCommand {

    private final AutomatonState mStartStateCopy;
    private final AddStateCommand mNewStateCommand;

    public static IsolateInitialStateCommand create(Automaton automaton)
    {
        AutomatonState startState = automaton.getStartState();

        if (!automaton.hasIngoingTransition(startState)) {
            return null;
        }

        return new IsolateInitialStateCommand(automaton);
    }

    private IsolateInitialStateCommand(Automaton automaton)
    {
        super(automaton);
        AutomatonState startState = automaton.getStartState();

        List<AutomatonTransition> removeTransitions = new ArrayList<>();

        for (AutomatonTransition t : automaton
                .getStateTransitions(startState)) {
            if (t.getTo() != startState) {
                // Don't add loops so we don't get duplicates in the list
                removeTransitions.add(t);
            }
        }
        removeTransitions.addAll(automaton.getIngoingTransition(startState));

        // Remove all in-going and out-going transitions on the start state
        for (AutomatonTransition t : removeTransitions) {
            super.commands.add(new RemoveTransitionCommand(automaton, t));
        }

        // Create a new state which will be a copy of the current start state,
        // add an epsilon transition from the actual start state to the copy
        mStartStateCopy = automaton.createNewState();
        AutomatonTransition epsilonTrans = automaton.createNewTransition(
                startState, mStartStateCopy, BasicRegexp.EPSILON_EXPRESSION);
        mNewStateCommand = new AddStateCommand(automaton, mStartStateCopy);
        super.commands.add(mNewStateCommand);
        super.commands.add(new AddTransitionCommand(automaton, epsilonTrans));

        // Add all the transitions we removed from the start state, but to/from
        // the copied state.
        for (AutomatonTransition t : removeTransitions) {
            AutomatonTransition newTrans;
            if (t.getFrom() == startState && t.getTo() == startState) {
                newTrans = automaton.createNewTransition(mStartStateCopy,
                        mStartStateCopy, t.getData());
            } else if (t.getFrom() == startState) {
                newTrans = automaton.createNewTransition(mStartStateCopy,
                        t.getTo(), t.getData());
            } else {
                newTrans = automaton.createNewTransition(t.getFrom(),
                        mStartStateCopy, t.getData());
            }
            super.commands.add(new AddTransitionCommand(automaton, newTrans));
        }

    }

    /**
     * @return The command which adds the copy of the initial state.
     */
    public AddStateCommand getNewStateCommand()
    {
        return mNewStateCommand;
    }

    /**
     * @return The new state which is a copy of the initial state.
     */
    public AutomatonState getStartStateCopy()
    {
        return mStartStateCopy;
    }

}
