package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public class IsolateInitialStateCommand extends Command {
    protected final ArrayList<Command> mCommands;
    private final AutomatonState mStartStateCopy;

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
        mCommands = new ArrayList<>();

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
            mCommands.add(new RemoveTransitionCommand(automaton, t));
        }

        // Create a new state which will be a copy of the current start state,
        // add an epsilon transition from the actual start state to the copy
        mStartStateCopy = automaton.createNewState();
        AutomatonTransition epsilonTrans = automaton.createNewTransition(
                startState, mStartStateCopy, BasicRegexp.EPSILON_EXPRESSION);
        mCommands.add(new AddStateCommand(automaton, mStartStateCopy));
        mCommands.add(new AddTransitionCommand(automaton, epsilonTrans));

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
            mCommands.add(new AddTransitionCommand(automaton, newTrans));
        }

    }

    /**
     * @return The new state which is a copy of the
     */
    public AutomatonState getStartStateCopy()
    {
        return mStartStateCopy;
    }

    /**
     * @return the list of commands which this command executes, as an
     * unmodifiable list
     */
    public List<Command> getCommands()
    {
        return Collections.unmodifiableList(mCommands);
    }

    @Override
    public void undo()
    {
        ListIterator<Command> it = mCommands.listIterator(mCommands.size());
        while (it.hasPrevious()) {
            Command c = it.previous();
            c.undo();
        }
    }

    @Override
    public void redo()
    {
        for (Command c : mCommands) {
            c.redo();
        }
    }

}
