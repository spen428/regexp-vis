package model;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Stores a history of commands for rewind and playback
 * 
 * Extends {@link Observable}, sending the current value of
 * {@link CommandHistory#mHistoryIdx} to all observers.
 * @see Command
 */
public class CommandHistory extends Observable {

    public static final boolean CLOBBER_BY_DEFAULT = false;

    /**
     * Value propogated to observers each time the last element of the history
     * list is removed.
     */
    public static final int HISTORY_CLOBBERED = -2;

    private final ArrayList<Command> mCommandList;
    private int mHistoryIdx;
    private boolean clobber;

    public CommandHistory()
    {
        mCommandList = new ArrayList<>();
        mHistoryIdx = 0;
        clobber = CLOBBER_BY_DEFAULT;
    }

    /**
     * Returns the current index in the history. This index essentially
     * represents the index of the next command to be executed, e.g. a value of
     * 0 indicates no commands have been executed, and a value the same as
     * returned by {@link #getHistorySize()} indicates all commands have been
     * executed.
     *
     * @return the current index in the history
     */
    public int getHistoryIdx()
    {
        return mHistoryIdx;
    }

    /**
     * @return the number of commands in the history
     */
    public int getHistorySize()
    {
        return mCommandList.size();
    }

    /**
     * Go backward a single command, does nothing if at the beginning of the
     * history
     */
    public void prev()
    {
        if (mHistoryIdx == 0) {
            return;
        }

        mCommandList.get(--mHistoryIdx).undo();
        this.setChanged();
        this.notifyObservers(mHistoryIdx);
    }

    /**
     * Go forward a single command, does nothing if at the end of the history
     */
    public void next()
    {
        if (mHistoryIdx == mCommandList.size()) {
            return;
        }

        mCommandList.get(mHistoryIdx++).redo();
        this.setChanged();
        this.notifyObservers(mHistoryIdx);
    }

    /**
     * Go forward or backwards through the history to reach a specified index
     *
     * @param idx the index to seek to
     * @throws IndexOutOfBoundsException if "idx" is not in the the range
     * [0, {@link #getHistorySize()}]
     */
    public void seekIdx(int idx)
    {
        if (idx < 0) {
            throw new IndexOutOfBoundsException(
                "Specified history idx cannot be negative");
        }
        if (idx > mCommandList.size()) {
            throw new IndexOutOfBoundsException(
                "Specified history idx cannot be greater than history length");
        }

        // Either one of the following loops will execute, depending on which
        // direction we need to seek
        while (idx > mHistoryIdx) {
            mCommandList.get(mHistoryIdx++).redo();
        }

        while (idx < mHistoryIdx) {
            mCommandList.get(--mHistoryIdx).undo();
        }
        this.setChanged();
        this.notifyObservers(mHistoryIdx);
    }

    /**
     * Execute a command, adding it to the command buffer
     * @param cmd the command to execute
     * @throws RuntimeException if not currently at the end of the history
     */
    public void executeNewCommand(Command cmd)
    {
        if (mHistoryIdx < mCommandList.size()) {
            if (clobber) {
                /* Overwrite (well, remove) history past this point */
                while (mHistoryIdx < mCommandList.size()) {
                    mCommandList.remove(mCommandList.size() - 1);
                    this.setChanged();
                    this.notifyObservers(HISTORY_CLOBBERED);
                }
            } else {
                throw new RuntimeException(
                    "Cannot execute new command while not and the end of the " +
                    "command list");
            }
        }

        mCommandList.add(cmd);
        cmd.redo();
        mHistoryIdx++;
        this.setChanged();
        this.notifyObservers(mHistoryIdx);
    }

    public void clear() {
        mCommandList.clear();
        mHistoryIdx = 0;
    }

    public boolean isClobbered() {
        return this.clobber;
    }

    public void setClobbered(boolean clobber) {
        this.clobber = clobber;
    }

}
