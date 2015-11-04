package model;

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

    public int getId()
    {
        return mId;
    }

    public boolean isFinal()
    {
        return mIsFinal;
    }

    public void setFinal(boolean f)
    {
        mIsFinal = f;
    }
}
