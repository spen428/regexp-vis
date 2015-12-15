package model;

/**
 * Class which represents a transition from one state to another in an
 * automaton
 */
public final class AutomatonTransition {
    private final int mId;
    private final AutomatonState mFrom;
    private final AutomatonState mTo;

    // TODO(mjn33): Possibly use BasicRegexp in the future
    private final Object mData;

    public AutomatonTransition(int id, AutomatonState from, AutomatonState to,
        Object data)
    {
        mId = id;
        mFrom = from;
        mTo = to;
        mData = data;
    }

    /**
     * @return The unique id for this transition in an automaton
     */
    public int getId()
    {
        return mId;
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
