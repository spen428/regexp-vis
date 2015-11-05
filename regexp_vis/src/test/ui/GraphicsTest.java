package test.ui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import com.mxgraph.view.mxGraph;

import ui.GraphPanel;

/**
 * Demonstrates the capabilities of JGraphX.
 * 
 * @author sp611
 * 
 */
public class GraphicsTest extends KeyAdapter implements KeyListener {

	/**
	 * Default diameter of vertices.
	 */
	private static final int VERTEX_DIAMETER_PX = 50;
	private static final int JFRAME_WIDTH_PX = 450;
	private static final int JFRAME_HEIGHT_PX = 450;
	private final JFrame frame;
	private final GraphPanel graphPanel;

	public GraphicsTest(mxGraph graph) {
		frame = new JFrame("Regular Language Visualiser - "
				+ "Graph Layout Demo");
		frame.addKeyListener(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(JFRAME_WIDTH_PX, JFRAME_HEIGHT_PX);
		graphPanel = new GraphPanel(graph);
		graphPanel.addKeyListener(this);
		frame.getContentPane().add(graphPanel);
		frame.setVisible(true);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		char keyChar = e.getKeyChar();
		switch (keyChar) {
		default:
		case 'a':
			graphPanel.setVertexLayout(0);
			break;
		case 's':
			graphPanel.setVertexLayout(1);
			break;
		case 'd':
			graphPanel.setVertexLayout(2);
			break;
		}
	}

	public static void main(String[] args) {
		mxGraph graph = makeGraph();
		new GraphicsTest(graph);
	}

	private static mxGraph makeGraph() {
		mxGraph graph = new mxGraph();
		Object parent = graph.getDefaultParent();

		graph.getModel().beginUpdate();
		try {
			/* Add some vertices and edges, all at position (0,0). */
			Object vFirst, vPrev, vNew;
			vFirst = graph.insertVertex(parent, null, 0, 0, 0,
					VERTEX_DIAMETER_PX, VERTEX_DIAMETER_PX);
			vPrev = vFirst;

			for (int i = 0; i <= 9; i++) {
				vNew = graph.insertVertex(parent, null, (i + 1), 0, 0,
						VERTEX_DIAMETER_PX, VERTEX_DIAMETER_PX);
				graph.insertEdge(parent, null, (char) ('A' + i), vPrev, vNew);
				graph.insertEdge(parent, null, (char) ('A' + (2 * (i + 1))),
						vPrev, vNew);
				vPrev = vNew;
			}

			graph.insertEdge(parent, null, (char) ('Y'), vPrev, vFirst);
			graph.insertEdge(parent, null, (char) ('Z'), vPrev, vFirst);
		} finally {
			graph.getModel().endUpdate();
		}
		return graph;
	}

}
