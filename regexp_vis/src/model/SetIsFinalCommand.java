package model;

/**
 * Command to change whether a state is final or not
 */
public class SetIsFinalCommand extends Command {
    private final AutomatonState mState;
    private final boolean mDiffers;

    public SetIsFinalCommand(Automaton automaton, AutomatonState state,
        boolean isFinal)
    {
        super(automaton);
        mState = state;
        mDiffers = (state.isFinal() != isFinal);
    }

    public void redo()
    {
        // No-op if isFinal doesn't differ
        if (mDiffers) {
            mState.setFinal(!mState.isFinal());
        }
    }

    public void undo()
    {
        if (mDiffers) {
            mState.setFinal(!mState.isFinal());
        }
    }
}