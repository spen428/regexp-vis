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
 * Abstract class to represent a command which breaks down a complex expression
 * to a more simple one. This is used when translating a regular expression into
 * an NFA. These commands essentially fill a command buffer of more basic
 * operations such as AddStateCommand, AddTransitionCommand, RemoveStateCommand,
 * etc.
 *
 * <i>Note:</i> for the translation process, these commands should be executed
 * before any more are queued. Translating operations such as iteration can be
 * "unsafe" / incorrect if they are not "isolated". See "Regular Expressions - a
 * Graphical User Interface" by Stefan Khars for an explanation of safe and
 * unsafe translations.
 */
public abstract class BreakdownCommand extends CompositeCommand {

    private final AutomatonTransition mOriginalTransition;

    public BreakdownCommand(Automaton automaton, AutomatonTransition t) {
        super(automaton);
        this.mOriginalTransition = t;
    }

    /**
     * @return The original transition which is to be broken down
     */
    public AutomatonTransition getOriginalTransition() {
        return this.mOriginalTransition;
    }

}
