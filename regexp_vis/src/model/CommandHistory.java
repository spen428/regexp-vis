package model;

import java.util.*;

public class CommandHistory {
    private Automaton mAutomaton;
    private ArrayList<Command> mCommandList;
    private int mHistoryIdx;

    public CommandHistory(Automaton automaton)
    {
        mAutomaton = automaton;
        mCommandList = new ArrayList<>();
        mHistoryIdx = 0;
    }

    public Automaton getAutomaton()
    {
        return mAutomaton;
    }

    public int getHistoryIdx()
    {
        return mHistoryIdx;
    }

    public int getHistorySize()
    {
        return mCommandList.size();
    }

    public void prev()
    {
        if (mHistoryIdx == 0)
            return;

        mCommandList.get(--mHistoryIdx).undo();
    }

    public void next()
    {
        if (mHistoryIdx == mCommandList.size())
            return;

        mCommandList.get(mHistoryIdx++).redo();
    }

    public void seekIdx(int idx)
    {
        if (idx < 0)
            throw new IndexOutOfBoundsException("Specified history idx cannot be negative");
        if (idx > mCommandList.size())
            throw new IndexOutOfBoundsException("Specified history idx cannot be greater than history length");

        // Either one of the following loops will execute, depending
        // on which direction we need to seek
        while (idx > mHistoryIdx) {
            mCommandList.get(mHistoryIdx++).redo();
        }

        while (idx < mHistoryIdx) {
            mCommandList.get(--mHistoryIdx).undo();
        }
    }

    public void executeNewCommand(Command cmd)
    {
        if (mHistoryIdx != mCommandList.size())
            throw new RuntimeException("TODO: sdfsdfsdf");

        mCommandList.add(cmd);
        cmd.redo();
        mHistoryIdx++;
    }
}
