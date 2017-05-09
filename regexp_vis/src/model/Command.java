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
 * The abstract base class encapsulate modifications performed on an Automaton
 * object.
 *
 * This uses the "Command" design pattern, hence the name. The CommandHistory
 * class can record the history of commands to provide undo and redo.
 *
 * @see CommandHistory
 */
public abstract class Command {
    private final Automaton mAutomaton;

    public Command(Automaton automaton) {
        mAutomaton = automaton;
    }

    /**
     * @return The automaton this command is associated with.
     */
    public Automaton getAutomaton() {
        return mAutomaton;
    }

    /**
     * This performs the opposite of the redo() method, should return the state
     * of the Automaton to exactly as it was before redo() was executed for the
     * first time. Calling undo() if the command hasn't been done, or has
     * already been "undone" is undefined.
     */
    public abstract void undo();
    /**
     * This performs the command. Also used to execute the command for the first
     * time (despite the name "redo"). Calling redo() if the command has already
     * been "done" is undefined.
     */
    public abstract void redo();
}
