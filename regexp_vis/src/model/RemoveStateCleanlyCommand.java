package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Similar to RemoveStateCommand, however this command also handles in-going
 * transitions. Note because of this, similarly to BreakdownIterationCommand for
 * example, this command should be executed before any other commands are
 * executed. For example, adding another in-going transition afterwards would
 * result in that transition not being removed/added correctly.
 */
public class RemoveStateCleanlyCommand extends Command {
    private final AutomatonState mState;
    private final ArrayList<Command> mCommands;

    public RemoveStateCleanlyCommand(Automaton automaton, AutomatonState state)
    {
        super(automaton);
        mState = state;

        // Find in-going transitions and create commands to remove them
        mCommands = new ArrayList<>();
        Iterator<Automaton.StateTransitionsPair> it = automaton.graphIterator();
        while (it.hasNext()) {
            Automaton.StateTransitionsPair pair = it.next();
            List<AutomatonTransition> trans = pair.getTransitions();
            for (AutomatonTransition t : trans) {
                if (t.getTo() == state) {
                    mCommands.add(new RemoveTransitionCommand(
                            automaton, t));
                }
            }
        }

        mCommands.add(new RemoveStateCommand(automaton, state));
    }

    /**
     * @return The state which is to be removed
     */
    public AutomatonState getState()
    {
        return mState;
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
        ListIterator<Command> it = mCommands
                .listIterator(mCommands.size());
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
