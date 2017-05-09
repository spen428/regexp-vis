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
 * Command to change whether a state is final or not
 */
public class SetIsFinalCommand extends Command {
    private final AutomatonState mState;
    private final boolean mDiffers;
    private final boolean mOriginalFinality;

    public SetIsFinalCommand(Automaton automaton, AutomatonState state,
        boolean isFinal)
    {
        super(automaton);
        mState = state;
        mDiffers = (state.isFinal() != isFinal);
        mOriginalFinality = state.isFinal();
    }

    @Override
    public void redo()
    {
        // No-op if isFinal doesn't differ
        if (mDiffers) {
            mState.setFinal(!mState.isFinal());
        }
    }

    @Override
    public void undo()
    {
        if (mDiffers) {
            mState.setFinal(!mState.isFinal());
        }
    }

    public boolean isDiffers()
    {
        return mDiffers;
    }

    public AutomatonState getState()
    {
        return mState;
    }

    public boolean getOriginalFinality() {
        return mOriginalFinality;
    }
}
