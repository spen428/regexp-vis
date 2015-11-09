package test.ui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import ui.Graph;
import ui.GraphPanel;

/**
 * 
 * @author sp611, pg272
 * 
 */
public class GraphTest extends KeyAdapter implements KeyListener {

	private static final int JFRAME_WIDTH_PX = 450;
	private static final int JFRAME_HEIGHT_PX = 450;
	private final JFrame frame;
	private final GraphPanel graphPanel;

	public GraphTest(Graph graph) {
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

	private static Graph graph1() {
		Graph graph = new Graph();
		Object parent = graph.getDefaultParent();

		graph.getModel().beginUpdate();
		try {
			/* Build graph */
			Object s1 = graph.insertVertex(parent, null, "State 1", 0, 0, 50, 50);
			Object s2 = graph.insertVertex(parent, null, "State 2", 0, 0, 50, 50);
			Object s3 = graph.insertVertex(parent, null, "State 3", 0, 0, 50, 50);

			graph.insertEdge(parent, null, "Edge 1", s1, s2);
			graph.insertEdge(parent, null, "Edge 1", s2, s1);
		} finally {
			graph.getModel().endUpdate();
		}
		return graph;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_LEFT:
			break;
		case KeyEvent.VK_RIGHT:
			break;
		}
	}

	public static void main(String[] args) {
		Graph graph = graph1();
		new GraphTest(graph);
	}

}
