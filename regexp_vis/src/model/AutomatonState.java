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

    @Override
    public String toString()
    {
        return Integer.toString(mId) + (mIsFinal ? " [final]" : "");
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
