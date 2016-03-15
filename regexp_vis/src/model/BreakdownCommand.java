package model;

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
public abstract class BreakdownCommand extends CompositeCommand {

    private final AutomatonTransition mOriginalTransition;

    public BreakdownCommand(Automaton automaton, AutomatonTransition t) {
        super(automaton);
        this.mOriginalTransition = t;
    }

    /**
     * @return The original transition which is to be broken down
     */
    public AutomatonTransition getOriginalTransition() {
        return this.mOriginalTransition;
    }

}
