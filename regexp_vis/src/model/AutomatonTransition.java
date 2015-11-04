package model;

public final class AutomatonTransition {
    // TODO(mjn33): Make final?
    private final AutomatonState mFrom;
    private final AutomatonState mTo;

    // TODO(mjn33): More specific?
    private final Object mData;

    public AutomatonTransition(AutomatonState from, AutomatonState to, Object data)
    {
        mFrom = from;
        mTo = to;
        mData = data;
    }

    public AutomatonState getFrom()
    {
        return mFrom;
    }

    public AutomatonState getTo()
    {
        return mTo;
    }

    public Object getData()
    {
        return mData;
    }
}
