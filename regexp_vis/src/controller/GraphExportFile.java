package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import view.GraphCanvasFX;
import view.GraphNode;
import model.Automaton;
import model.Automaton.StateTransitionsPair;
import model.AutomatonState;
import model.AutomatonTransition;
import model.BasicRegexp;
import model.InvalidRegexpException;

public class GraphExportFile {
    /**
     * Class which contains all the data for a AutomatonState entry
     */
    static final class AutomatonStateEntry {
        public double x, y;
        public boolean isFinal;
    }

    /**
     * Class which contains all the data for a AutomatonTransition entry
     */
    static final class AutomatonTransitionEntry {
        public int fromId, toId;
        public BasicRegexp data;
    }

    /**
     * The AutomatonState entries in this file, the first one is always the
     * start state, thus we expect at least one entry here.
     */
    ArrayList<AutomatonStateEntry> mStateEntries;
    /**
     * The AutomatonTransition entries in this file, the IDs used refer to
     * indexes into mStateEntries, not whatever the IDs where previously
     * (when the file was saved)
     */
    ArrayList<AutomatonTransitionEntry> mTransitionEntries;

    /**
     * Creates a new GraphExportFile by reading the given file.
     *
     * @param file The file to read
     * @throws BadGraphExportFileException If the file isn't a valid Automaton
     * Graph File
     */
    public GraphExportFile(File file)
        throws BadGraphExportFileException, IOException
    {
        mStateEntries = new ArrayList<>();
        mTransitionEntries = new ArrayList<>();
        readFile(file);
    }

    /**
     * Creates a new GraphExportFile by taking the necessary information from an
     * Automaton and its corresponding canvas. Nothing is written to disk yet.
     *
     * @param automaton The automaton to use
     * @param canvas The canvas to use
     */
    public GraphExportFile(Automaton automaton, GraphCanvasFX canvas)
    {
        mStateEntries = new ArrayList<>();
        mTransitionEntries = new ArrayList<>();

        // Mapping from AutomatonState IDs into indexes into mStateEntries, for
        // use in the AutomatonTransition entries which reference indexes and
        // not the original IDs
        HashMap<Integer, Integer> idMap = new HashMap<>();

        // Add the start state first
        AutomatonState startState = automaton.getStartState();
        GraphNode startStateNode = canvas.lookupNode(startState.getId());
        AutomatonStateEntry startStateEntry = new AutomatonStateEntry();
        startStateEntry.x = startStateNode.getX();
        startStateEntry.y = startStateNode.getY();
        startStateEntry.isFinal = startState.isFinal();
        idMap.put(startState.getId(), mStateEntries.size());
        mStateEntries.add(startStateEntry);

        Iterator<StateTransitionsPair> it = automaton.graphIterator();
        while (it.hasNext()) {
            StateTransitionsPair pair = it.next();
            AutomatonState state = pair.getState();
            GraphNode node = canvas.lookupNode(state.getId());
            // Start state already added, don't add it again
            if (state == startState) {
                continue;
            }

            AutomatonStateEntry entry = new AutomatonStateEntry();
            entry.x = node.getX();
            entry.y = node.getY();
            entry.isFinal = state.isFinal();
            idMap.put(state.getId(), mStateEntries.size());
            mStateEntries.add(entry);
        }

        it = automaton.graphIterator();
        while (it.hasNext()) {
            StateTransitionsPair pair = it.next();
            List<AutomatonTransition> trans = pair.getTransitions();

            for (AutomatonTransition t : trans) {
                AutomatonTransitionEntry entry = new AutomatonTransitionEntry();
                entry.fromId = idMap.get(t.getFrom().getId());
                entry.toId = idMap.get(t.getTo().getId());
                entry.data = t.getData();
                mTransitionEntries.add(entry);
            }
        }
    }

    private void readFile(File file)
        throws BadGraphExportFileException, IOException
    {
        // We need to make sure we are using UTF-8 to encode characters such as
        // epsilon
        FileInputStream is = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(is, "UTF-8");
        try (BufferedReader br = new BufferedReader(isr)) {
            String strLine;

            while ((strLine = br.readLine()) != null) {
                Scanner s = new Scanner(strLine);
                 readLine(s);
                 s.close();
            }
        }
    }

    /**
     * Set up the given automaton and canvas based on the contents of this
     * GraphExportFile. It is expected that the automaton and canvas are empty.
     *
     * @param automaton The automaton to add to
     * @param canvas The canvas to add to
     */
    public void loadFile(Automaton automaton, GraphCanvasFX canvas)
    {
        // Mapping from indexes into mStateEntries to AutomatonState IDs
        AutomatonState[] idxStateMap = new AutomatonState[mStateEntries.size()];
        AutomatonTransition[] idxTransMap = new AutomatonTransition[mTransitionEntries.size()];

        int i = 0;
        for (AutomatonStateEntry entry : mStateEntries) {
            // Special case: first item is the initial state
            if (i == 0) {
                idxStateMap[i] = automaton.getStartState();
            } else {
                idxStateMap[i] = automaton.createNewState();
            }
            idxStateMap[i].setFinal(entry.isFinal);
            i++;
        }

        i = 0;
        for (AutomatonTransitionEntry entry : mTransitionEntries) {
            AutomatonState stateFrom = idxStateMap[entry.fromId];
            AutomatonState stateTo = idxStateMap[entry.toId];
            idxTransMap[i] = automaton.createNewTransition(stateFrom, stateTo, entry.data);
            i++;
        }

        i = 0;
        for (AutomatonStateEntry entry : mStateEntries) {
            AutomatonState state = idxStateMap[i];
            GraphNode n = canvas.addNode(state.getId(), entry.x, entry.y);
            // Need to make sure the initial state has the correct style, and
            // that we don't try to insert it into the automaton
            if (state == automaton.getStartState()) {
                canvas.setNodeUseStartStyle(n, true);
            } else {
                automaton.addStateWithTransitions(state,
                        new LinkedList<AutomatonTransition>());
            }
            // Same for the final states
            canvas.setNodeUseFinalStyle(n, state.isFinal());
            i++;
        }

        for (i = 0; i < idxTransMap.length; i++) {
            int fromId = idxTransMap[i].getFrom().getId();
            int toId = idxTransMap[i].getTo().getId();
            GraphNode fromNode = canvas.lookupNode(fromId);
            GraphNode toNode = canvas.lookupNode(toId);
            String text = idxTransMap[i].getData().toString();
            canvas.addEdge(idxTransMap[i].getId(), fromNode, toNode, text);
            automaton.addTransition(idxTransMap[i]);
        }
    }

    /**
     * Parses a line.
     *
     * @param s A scanner which can be used to read this line (i.e. we don't
     * read past multiple lines).
     */
    private void readLine(Scanner s)
        throws BadGraphExportFileException
    {
        if (!s.hasNext()) {
            throw new BadGraphExportFileException("Expected entry type");
        }
        String entryType = s.next();

        if (entryType.equals("AutomatonState")) {
            // The first AutomatonState we find specifies the start state
            mStateEntries.add(readState(s));
        } else if (entryType.equals("AutomatonTransition")) {
            mTransitionEntries.add(readTransition(s));
        } else {
            throw new BadGraphExportFileException("Unknown entry type found: "
                    + entryType);
        }
    }

    private AutomatonStateEntry readState(Scanner s)
        throws BadGraphExportFileException
    {
        AutomatonStateEntry entry = new AutomatonStateEntry();
        if (!s.hasNextDouble()) {
            throw new BadGraphExportFileException(
                    "Couldn't parse state x coordinate value");
        }
        entry.x = s.nextDouble();

        if (!s.hasNextDouble()) {
            throw new BadGraphExportFileException(
                    "Couldn't parse state y coordinate value");
        }
        entry.y = s.nextDouble();

        if (!s.hasNextBoolean()) {
            throw new BadGraphExportFileException(
                    "Couldn't parse isFinal flag for state");
        }
        entry.isFinal = s.nextBoolean();
        return entry;
    }

    private AutomatonTransitionEntry readTransition(Scanner s)
        throws BadGraphExportFileException
    {
        AutomatonTransitionEntry entry = new AutomatonTransitionEntry();
        if (!s.hasNextInt()) {
            throw new BadGraphExportFileException(
                    "Couldn't parse transition fromId value");
        }
        entry.fromId = s.nextInt();

        if (!s.hasNextInt()) {
            throw new BadGraphExportFileException(
                    "Couldn't parse transition toId value");
        }
        entry.toId = s.nextInt();

        if (!s.hasNextLine()) {
            throw new BadGraphExportFileException(
                    "Couldn't parse transition data string");
        }
        // The regexp is the data until the end of the line
        String text = s.nextLine();
        try {
            entry.data = BasicRegexp.parseRegexp(text);
        } catch (InvalidRegexpException e) {
            throw new BadGraphExportFileException(
                    "Couldn't parse regexp in file");
        }
        return entry;
    }

    /**
     * Write this GraphExportFile to the specified file.
     *
     * @param file The file to write to
     */
    public void writeFile(File file)
        throws IOException
    {
        FileOutputStream os = new FileOutputStream(file);
        OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
        try (BufferedWriter bw = new BufferedWriter(osw)) {
            for (AutomatonStateEntry entry : mStateEntries) {
                writeStateEntry(bw, entry);
            }
            for (AutomatonTransitionEntry entry : mTransitionEntries) {
                writeTransitionEntry(bw, entry);
            }
        }
    }

    private void writeStateEntry(BufferedWriter w, AutomatonStateEntry entry)
        throws IOException
    {
        StringBuilder strLine = new StringBuilder();

        strLine.append("AutomatonState ");
        strLine.append(entry.x);
        strLine.append(" ");
        strLine.append(entry.y);
        strLine.append(" ");
        strLine.append(entry.isFinal);
        w.write(strLine.toString());
        w.newLine();

    }

    private void writeTransitionEntry(BufferedWriter w,
            AutomatonTransitionEntry entry)
        throws IOException
    {
        StringBuilder strLine = new StringBuilder();

        strLine.append("AutomatonTransition ");
        strLine.append(entry.fromId);
        strLine.append(" ");
        strLine.append(entry.toId);
        strLine.append(" ");
        strLine.append(entry.data.toString());
        w.write(strLine.toString());
        w.newLine();
    }
}
