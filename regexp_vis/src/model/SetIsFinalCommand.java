package model;

public class SetIsFinalCommand extends Command {
    private AutomatonState mState;
    private boolean mDiffers;

    public SetIsFinalCommand(Automaton automaton, AutomatonState state, boolean isFinal)
    {
        super(automaton);
        mState = state;
        mDiffers = (state.isFinal() != isFinal);
    }

    public void redo()
    {
        // No-op if isFinal doesn't differ
        if (mDiffers)
            mState.setFinal(!mState.isFinal());
    }

    public void undo()
    {
        if (mDiffers)
            mState.setFinal(!mState.isFinal());
    }
}
