package ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import model.AddStateCommand;
import model.AddTransitionCommand;
import model.Automaton;
import model.AutomatonState;
import model.AutomatonTransition;
import model.BasicRegexp;
import model.CommandHistory;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxEdgeLabelLayout;
import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.layout.mxGraphLayout;
import com.mxgraph.layout.mxOrganicLayout;
import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;

/**
 * An extension of {@link mxGraphComponent} that JGraphX draws graphs on to.
 * {@link mxGraphComponent} itself is an extension of {@link JScrollPane}, so
 * this class can be instantiated and then added as a {@link JComponent} to a
 * {@link JFrame} or {@link JPanel}.
 * 
 * @author sp611
 * 
 */
public class GraphPanel extends mxGraphComponent {

    public enum GraphLayout {
        CIRCLE_LAYOUT, ORGANIC_LAYOUT, FAST_ORGANIC_LAYOUT;
    }

    private final Graph graph;
    private CommandHistory history;
    private final mxGraphLayout vertexCircleLayout, vertexOrganicLayout,
            vertexFastOrganicLayout, edgeLayout, edgeLabelLayout;

    // CONSTRUCTORS //
    /**
     * Creates a new instance of {@link GraphPanel} and attaches a new instance
     * of {@link Graph} to it.
     */
    public GraphPanel() {
        this(new Graph());
    }

    /**
     * Creates a new instance of {@link GraphPanel} and attaches the given
     * {@link Graph} to it.
     * 
     * @param graph
     *            the {@link Graph} to attach to this {@link GraphPanel}.
     */
    public GraphPanel(Graph graph) {
        super(graph);
        this.graph = graph;
        this.history = new CommandHistory();

        /*
         * A number of different vertex layout managers, that have their own
         * benefits. It may be necessary to switch between them for certain
         * graph structures, so I have provided that functionality with the
         * setVertexLayout() method.
         */
        this.vertexCircleLayout = new mxCircleLayout(graph);
        this.vertexOrganicLayout = new mxOrganicLayout(graph);
        this.vertexFastOrganicLayout = new mxFastOrganicLayout(graph);
        this.edgeLayout = new mxParallelEdgeLayout(graph);
        this.edgeLabelLayout = new mxEdgeLabelLayout(graph);

        setVertexLayout(GraphLayout.CIRCLE_LAYOUT);
    }

    // GETTERS AND SETTERS //
    public CommandHistory getHistory() {
        return history;
    }

    /**
     * Switches between the automatic vertex layout types provided by JGraphX.
     * This method automatically calls the {@code execute()} method on the
     * chosen {@link mxGraphLayout} type, and also updates the positions of
     * edges and edge labels after the vertices have been laid-out.
     * 
     * @param layout
     *            the {@link GraphPanel.GraphLayout} to use.
     */
    public void setVertexLayout(GraphLayout layout) {
        Object parent = graph.getDefaultParent();

        switch (layout) {
        default:
        case CIRCLE_LAYOUT:
            vertexCircleLayout.execute(parent);
            break;
        case FAST_ORGANIC_LAYOUT:
            vertexFastOrganicLayout.execute(parent);
            break;
        case ORGANIC_LAYOUT:
            vertexOrganicLayout.execute(parent);
            break;
        }

        /* Update edges and edge labels now that the vertices have moved. */
        edgeLayout.execute(parent);
        edgeLabelLayout.execute(parent);
    }

    // OVERRIDES //
    @Override
    protected void installDoubleClickHandler() {
        super.graphControl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (isEnabled()) {
                    if (!e.isConsumed() && isEditEvent(e)) {
                        Object cell = getCellAt(e.getX(), e.getY(), false);

                        if (cell != null && getGraph().isCellEditable(cell)) {
                            System.out.printf("Double-clicked cell: %s %s "
                                    + "%s%n", cell, e, e.getPoint());
                            // TODO: eventSource.fireEvent();
                            mxCell c = ((mxCell) cell);
                            if (c.isEdge()) {
                                System.out.println("Double-clicked an edge, "
                                        + "let's break it down!");
                                breakDown(c);
                            }
                        }
                    }
                }
            }

        });
    }

    // UTILITY //
    public void executeNewCommand(UICommand cmd) {
        history.executeNewCommand(cmd);
    }

    public void executeNewCommands(UICommand[] cmds) {
        for (UICommand c : cmds) {
            history.executeNewCommand(c);
        }
    }

    final void breakDown(mxCell c) {
        // TODO
    }

    /**
     * Clear the current Graph, and create a new one that represents a regular
     * expression that has yet to be broken down.
     * 
     * @param regexp
     *            a regular expression
     */
    public void resetGraph(String regexp) {
        this.graph.clear();
        this.history = new CommandHistory();

        /* Generate new automaton */
        Automaton a = this.graph.getAutomaton();
        AutomatonState finalState = a.createNewState();
        AutomatonTransition transition = a.createNewTransition(
                a.getStartState(), finalState, regexp);
        UICommand[] cmds = new UICommand[] {
                new AddStateUICommand(this.graph, new AddStateCommand(a,
                        finalState)),
                new AddTransitionUICommand(this.graph,
                        new AddTransitionCommand(a, transition)) };
        executeNewCommands(cmds);
        setVertexLayout(GraphLayout.CIRCLE_LAYOUT);
    }

}
