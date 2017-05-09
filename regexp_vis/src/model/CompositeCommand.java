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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public abstract class CompositeCommand extends Command {

    protected final ArrayList<Command> commands;

    public CompositeCommand(Automaton automaton) {
        super(automaton);
        this.commands = new ArrayList<>();
    }

    /**
     * @return the list of commands which this command executes, as an
     *         unmodifiable list
     */
    public List<Command> getCommands() {
        return Collections.unmodifiableList(this.commands);
    }

    @Override
    public void undo() {
        ListIterator<Command> it = this.commands.listIterator(this.commands
                .size());
        while (it.hasPrevious()) {
            Command cmd = it.previous();
            cmd.undo();
        }
    }

    @Override
    public void redo() {
        for (Command c : this.commands) {
            c.redo();
        }
    }

}
