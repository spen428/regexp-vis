/*
 * Copyright (c) 2015, 2016 Matthew J. Nicholls, Samuel Pengelly,
 * Parham Ghassemi, William R. Dix
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package model;

/**
 * Class which represents a transition from one state to another in an
 * automaton
 */
public final class AutomatonTransition {
    private final int mId;
    private final AutomatonState mFrom;
    private final AutomatonState mTo;

    private final BasicRegexp mData;

    public AutomatonTransition(int id, AutomatonState from, AutomatonState to,
        BasicRegexp data)
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
    public BasicRegexp getData()
    {
        return mData;
    }

}
