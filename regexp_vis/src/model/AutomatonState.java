package model;

/**
 * Class which represents a state of an automaton
 */
public final class AutomatonState {
    private final int mId;
    private boolean mIsFinal;

    public AutomatonState(int id, boolean isFinal)
    {
        mId = id;
        mIsFinal = isFinal;
    }

    public AutomatonState(int id)
    {
        this(id, false);
    }

    /**
     * @return The unique id for this state in an automaton
     */
    public int getId()
    {
        return mId;
    }

    /**
     * @return Whether this is a final state or not
     */
    public boolean isFinal()
    {
        return mIsFinal;
    }

    /**
     * Sets whether this state is final or not
     *
     * @param f Whether this state is final or not
     */
    public void setFinal(boolean f)
    {
        mIsFinal = f;
    }
}
