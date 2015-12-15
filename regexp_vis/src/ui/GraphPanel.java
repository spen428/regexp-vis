package ui;

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
import model.Command;
import model.CommandHistory;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxEdgeLabelLayout;
import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.layout.mxGraphLayout;
import com.mxgraph.layout.mxOrganicLayout;
import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxConnectionHandler;

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
    private GraphLayout layout;

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
        this.layout = GraphLayout.CIRCLE_LAYOUT;
        doGraphLayout();

        /* Disable unwanted user actions */
        // this.graph.setCellsBendable(false); // No idea what this does
        this.graph.setCellsCloneable(false);
        this.graph.setCellsDeletable(false);
        this.graph.setCellsDisconnectable(false);
        this.graph.setCellsEditable(false);
        // this.graph.setCellsMovable(true);
        this.graph.setCellsResizable(false);
    }

    // GETTERS AND SETTERS //
    public CommandHistory getHistory() {
        return this.history;
    }

    /**
     * Switches between the automatic vertex layout types provided by JGraphX.
     * This method automatically calls {@link #doGraphLayout()} to refresh the
     * layout of the cells
     * 
     * @param layout
     *            the {@link GraphPanel.GraphLayout} to use.
     * @see {@link #doGraphLayout()}
     */
    public void setVertexLayout(GraphLayout layout) {
        this.layout = layout;
        doGraphLayout();
    }

    /**
     * Calls the {@code execute()} method for the currently selected
     * {@link GraphLayout}, automatically repositioning the nodes, edges, and
     * edge labels of the graph.
     * 
     * @see {@link mxGraphLayout#execute(Object)}
     */
    public void doGraphLayout() {
        Object parent = this.graph.getDefaultParent();

        switch (this.layout) {
        default:
        case CIRCLE_LAYOUT:
            this.vertexCircleLayout.execute(parent);
            break;
        case FAST_ORGANIC_LAYOUT:
            this.vertexFastOrganicLayout.execute(parent);
            break;
        case ORGANIC_LAYOUT:
            this.vertexOrganicLayout.execute(parent);
            break;
        }

        /* Update edges and edge labels now that the vertices have moved. */
        this.edgeLayout.execute(parent);
        this.edgeLabelLayout.execute(parent);
    }

    // OVERRIDES //
    @Override
    protected void installDoubleClickHandler() {
        super.graphControl.addMouseListener(new DoubleClickHandler(this));
    }

    @Override
    protected mxConnectionHandler createConnectionHandler() {
        return null;
    }

    // UTILITY //
    public void executeNewCommand(UICommand cmd) {
        if (cmd != null) {
            this.history.executeNewCommand(cmd);
        }
    }

    /**
     * Converts the given {@link Command} into a {@link UICommand} before
     * calling {@link #executeNewCommand(UICommand)}
     * 
     * @param cmd
     *            the {@link Command} to execute
     */
    public void executeNewCommand(Command cmd) {
        this.executeNewCommand(UICommand.fromCommand(this.graph, cmd));
    }

    public void executeNewCommands(UICommand[] cmds) {
        for (UICommand c : cmds) {
            this.history.executeNewCommand(c);
        }
    }

    public void executeNewCommands(Command[] cmds) {
        for (Command c : cmds) {
            this.executeNewCommand(c);
        }
    }

    /**
     * Clear the current Graph, and create a new one that represents a regular
     * expression that has yet to be broken down.
     * 
     * @param re
     *            a regular expression
     */
    public void resetGraph(BasicRegexp re) {
        this.graph.clear();
        this.history = new CommandHistory();

        /* Generate new automaton */
        Automaton a = this.graph.getAutomaton();
        AutomatonState finalState = a.createNewState();
        AutomatonTransition transition = a.createNewTransition(
                a.getStartState(), finalState, re);
        UICommand[] cmds = new UICommand[] {
                new AddStateUICommand(this.graph, new AddStateCommand(a,
                        finalState)),
                new AddTransitionUICommand(this.graph,
                        new AddTransitionCommand(a, transition)) };
        executeNewCommands(cmds);
        doGraphLayout();
    }

}
