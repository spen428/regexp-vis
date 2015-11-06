package model;

public final class AutomatonTransition {
    private final AutomatonState mFrom;
    private final AutomatonState mTo;

    // TODO(mjn33): Possibly use BasicRegexp in the future
    private final Object mData;

    public AutomatonTransition(AutomatonState from, AutomatonState to,
        Object data)
    {
        mFrom = from;
        mTo = to;
        mData = data;
    }

    /**
     * @return The state this transition is from
     */
    public AutomatonState getFrom()
    {
        return mFrom;
    }

    /**
     * @return The state this transition points to
     */
    public AutomatonState getTo()
    {
        return mTo;
    }

    /**
     * @return The data associated with this transition
     */
    public Object getData()
    {
        return mData;
    }
}
