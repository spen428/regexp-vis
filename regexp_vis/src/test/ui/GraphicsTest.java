package test.ui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import ui.Graph;
import ui.GraphPanel;
import ui.GraphPanel.GraphLayout;

/**
 * Demonstrates the graph rendering capabilities of JGraphX.
 * 
 * @author sp611
 * 
 */
public class GraphicsTest extends KeyAdapter implements KeyListener {

    private static final int VERTEX_DIAMETER_PX = 50;
    private static final int NUM_VERTICES = 10;
    private static final int JFRAME_WIDTH_PX = VERTEX_DIAMETER_PX
            * NUM_VERTICES;
    private static final int JFRAME_HEIGHT_PX = JFRAME_WIDTH_PX;

    private final JFrame frame;
    private final GraphPanel graphPanel;

    public GraphicsTest() {
        /* Construct the JFrame */
        frame = new JFrame("Regular Language Visualiser - "
                + "Graph Layout Demo");
        frame.addKeyListener(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(JFRAME_WIDTH_PX, JFRAME_HEIGHT_PX);

        /* Instantiate GraphPanel, providing the test graph generated below. */
        graphPanel = new GraphPanel(generateTestGraph());
        graphPanel.addKeyListener(this);
        frame.getContentPane().add(graphPanel);
        frame.setVisible(true);
    }

    /**
     * Generates a simple cyclic graph with two edges per vertex.
     * 
     * @return The generated {@link Graph}
     */
    public static Graph generateTestGraph() {
        Graph graph = new Graph();
        Object parent = graph.getDefaultParent();

        graph.getModel().beginUpdate();
        /*
         * A try-finally block is recommended to ensure that endUpdate() is
         * always called even if an exception is thrown.
         */
        try {
            /* Add some vertices and edges, all at position (0,0). */
            Object vFirst, vPrev, vNew;
            vFirst = graph.insertVertex(parent, null, 0, 0, 0,
                    VERTEX_DIAMETER_PX, VERTEX_DIAMETER_PX);
            vPrev = vFirst;

            /* Loop to generate vertices and connect them all together. */
            char edgeLabel;
            for (int i = 0; i < NUM_VERTICES; i++) {
                vNew = graph.insertVertex(parent, null, (i + 1), 0, 0,
                        VERTEX_DIAMETER_PX, VERTEX_DIAMETER_PX);

                edgeLabel = (char) ('A' + i);
                graph.insertEdge(parent, null, edgeLabel, vPrev, vNew);

                edgeLabel = (char) ('A' + (2 * (i + 1)));
                graph.insertEdge(parent, null, edgeLabel, vNew, vPrev);

                /*
                 * Keep track of previously generated vertices, so that they can
                 * be connected together in the loop above.
                 */
                vPrev = vNew;
            }

            /* Finally, connect the first and last vertices together. */
            graph.insertEdge(parent, null, (char) ('Y'), vPrev, vFirst);
            graph.insertEdge(parent, null, (char) ('Z'), vFirst, vPrev);
        } finally {
            graph.getModel().endUpdate();
        }
        return graph;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        /* Switch between vertex layouts with the keyboard. */
        char keyChar = e.getKeyChar();
        switch (keyChar) {
        default:
        case 'a':
            graphPanel.setVertexLayout(GraphLayout.CIRCLE_LAYOUT);
            break;
        case 's':
            graphPanel.setVertexLayout(GraphLayout.ORGANIC_LAYOUT);
            break;
        case 'd':
            graphPanel.setVertexLayout(GraphLayout.FAST_ORGANIC_LAYOUT);
            break;
        }
    }

    public static void main(String[] args) {
        new GraphicsTest();
    }

}
