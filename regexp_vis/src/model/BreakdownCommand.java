package model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Abstract class to represent a command which breaks down a complex expression
 * to a more simple one. This is used when translating a regular expression into
 * an NFA. These commands essentially fill a command buffer of more basic
 * operations such as AddStateCommand, AddTransitionCommand, RemoveStateCommand,
 * etc.
 *
 * <i>Note:</i> for the translation process, these commands should be executed
 * before any more are queued. Translating operations such as iteration can be
 * "unsafe" / incorrect if they are not "isolated". See "Regular Expressions - a
 * Graphical User Interface" by Stefan Khars for an explanation of safe and
 * unsafe translations.
 */
public abstract class BreakdownCommand extends Command {
    protected final LinkedList<Command> mCommands;
    private final AutomatonTransition mOriginalTransition;

    public BreakdownCommand(Automaton automaton, AutomatonTransition t)
    {
        super(automaton);
        mCommands = new LinkedList<Command>();
        mOriginalTransition = t;
    }

    /**
     * @return The original transition which is to be broken down
     */
    public AutomatonTransition getOriginalTransition()
    {
        return mOriginalTransition;
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
