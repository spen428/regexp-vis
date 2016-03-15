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
public class RemoveStateCleanlyCommand extends CompositeCommand {
    private final AutomatonState mState;

    public RemoveStateCleanlyCommand(Automaton automaton, AutomatonState state)
    {
        super(automaton);
        mState = state;

        // Find in-going transitions and create commands to remove them
        Iterator<Automaton.StateTransitionsPair> it = automaton.graphIterator();
        while (it.hasNext()) {
            Automaton.StateTransitionsPair pair = it.next();
            List<AutomatonTransition> trans = pair.getTransitions();
            for (AutomatonTransition t : trans) {
                if (t.getTo() == state) {
                    super.commands.add(new RemoveTransitionCommand(
                            automaton, t));
                }
            }
        }

        super.commands.add(new RemoveStateCommand(automaton, state));
    }

    /**
     * @return The state which is to be removed
     */
    public AutomatonState getState()
    {
        return mState;
    }

}
