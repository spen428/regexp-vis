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

import java.util.Iterator;

/**
 * Command which breaks down a sequence of expressions (e.g. "abcd") into a set
 * of simpler transitions. For the regular expression to NFA conversion process.
 */
public class BreakdownSequenceCommand extends BreakdownCommand {
    private int mNewTransitionsCount;
    
    /**
     * @param automaton The automaton for the transition
     * @param t The transition to break down
     * @throws IllegalArgumentException if the transition expression isn't
     * SEQUENCE
     */
    public BreakdownSequenceCommand(Automaton automaton, AutomatonTransition t)
    {
        super(automaton, t);

        BasicRegexp re = t.getData();
        AutomatonState from = t.getFrom();
        AutomatonState to = t.getTo();

        if (re.getOperator() != BasicRegexp.RegexpOperator.SEQUENCE) {
            throw new IllegalArgumentException(
                "BreakdownSequenceCommand must be passed a SEQUENCE " +
                "transition (e.g. \"abcd\"");
        }

        super.commands.add(new RemoveTransitionCommand(automaton, t));
        mNewTransitionsCount = re.getOperands().size();

        AutomatonState prevState = from;
        Iterator<BasicRegexp> it = re.getOperands().iterator();
        while (it.hasNext()) {
            BasicRegexp operand = it.next();
            AutomatonState nextState;
            if (it.hasNext()) {
                nextState = automaton.createNewState();
                super.commands.add(new AddStateCommand(automaton, nextState));
            } else {
                nextState = to;
            }

            AutomatonTransition newTrans = automaton.createNewTransition(
                prevState, nextState, operand);
            super.commands.add(new AddTransitionCommand(automaton, newTrans));
            prevState = nextState;
        }
    }
    
    /**
     * @return The number of new transitions this breakdown will create, e.g. 
     * "abc" will breakdown into 3 transitions "a", "b" and "c"
     */
    public int getNewTransitionsCount()
    {
        return this.mNewTransitionsCount;
    }
}
