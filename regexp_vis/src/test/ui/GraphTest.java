package test.ui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import com.mxgraph.view.mxGraph;

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

	public GraphTest(mxGraph graph) {
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

	private static mxGraph graph1() {
		mxGraph graph = new mxGraph();
		Object parent = graph.getDefaultParent();

		graph.getModel().beginUpdate();
		try {
			/* Build graph */
			Object s1 = graph.insertVertex(parent, null, "State 1", 0, 0, 50, 50);
			Object s2 = graph.insertVertex(parent, null, "State 2", 0, 0, 50, 50);
			Object s3 = graph.insertVertex(parent, null, "State 3", 0, 0, 50, 50);

			graph.insertEdge(parent, null, "Edge 1", s1, s2);
			graph.insertEdge(parent, null, "Edge 2", s2, s1);
		} finally {
			graph.getModel().endUpdate();
		}
		return graph;
	}
	
	private static mxGraph graph2() {
		mxGraph graph = new mxGraph();
		Object parent = graph.getDefaultParent();

		graph.getModel().beginUpdate();
		try {
			/* Build graph */
			Object s1 = graph.insertVertex(parent, null, "State 1", 0, 0, 50, 50);
			Object s2 = graph.insertVertex(parent, null, "State 2", 0, 0, 50, 50);
			Object s3 = graph.insertVertex(parent, null, "State 3", 0, 0, 50, 50);

			graph.insertEdge(parent, null, "Edge 1", s1, s2);
			graph.insertEdge(parent, null, "Edge 2", s2, s1);
			graph.insertEdge(parent, null, "Edge 3", s2, s3);
			graph.insertEdge(parent, null, "Edge 4", s3, s2);
		} finally {
			graph.getModel().endUpdate();
		}
		return graph;
	}
	
	private static mxGraph graph3() {
		mxGraph graph = new mxGraph();
		Object parent = graph.getDefaultParent();

		graph.getModel().beginUpdate();
		try {
			/* Build graph */
			Object s1 = graph.insertVertex(parent, null, "State 1", 0, 0, 50, 50);
			Object s2 = graph.insertVertex(parent, null, "State 2", 0, 0, 50, 50);
			Object s3 = graph.insertVertex(parent, null, "State 3", 0, 0, 50, 50);
			Object s4 = graph.insertVertex(parent, null, "State 4", 0, 0, 50, 50);
			Object s5 = graph.insertVertex(parent, null, "State 5", 0, 0, 50, 50);

			graph.insertEdge(parent, null, "Edge 1", s1, s5);
			graph.insertEdge(parent, null, "Edge 2", s5, s2);
			graph.insertEdge(parent, null, "Edge 3", s2, s4);
			graph.insertEdge(parent, null, "Edge 4", s4, s2);
			graph.insertEdge(parent, null, "Edge 5", s4, s5);
			graph.insertEdge(parent, null, "Edge 6", s3, s5);
			graph.insertEdge(parent, null, "Edge 7", s1, s3);
			graph.insertEdge(parent, null, "Edge 8", s4, s1);
		} finally {
			graph.getModel().endUpdate();
		}
		return graph;
	}
	
	private static mxGraph graph4() {
		mxGraph graph = new mxGraph();
		Object parent = graph.getDefaultParent();

		graph.getModel().beginUpdate();
		try {
			/* Build graph */
			Object s1 = graph.insertVertex(parent, null, "State 1", 0, 0, 50, 50);
			Object s2 = graph.insertVertex(parent, null, "State 2", 0, 0, 50, 50);
			Object s3 = graph.insertVertex(parent, null, "State 3", 0, 0, 50, 50);
			Object s4 = graph.insertVertex(parent, null, "State 4", 0, 0, 50, 50);
			Object s5 = graph.insertVertex(parent, null, "State 5", 0, 0, 50, 50);
			Object s6 = graph.insertVertex(parent, null, "State 6", 0, 0, 50, 50);

			graph.insertEdge(parent, null, "Edge 1", s6, s1);
			graph.insertEdge(parent, null, "Edge 2", s6, s2);
			graph.insertEdge(parent, null, "Edge 3", s6, s3);
			graph.insertEdge(parent, null, "Edge 4", s6, s4);
			graph.insertEdge(parent, null, "Edge 5", s6, s5);
			graph.insertEdge(parent, null, "Edge 6", s6, s6);
			graph.insertEdge(parent, null, "Edge 7", s1, s6);
			graph.insertEdge(parent, null, "Edge 8", s5, s6);
			graph.insertEdge(parent, null, "Edge 9", s3, s2);
			graph.insertEdge(parent, null, "Edge 10", s2, s3);
			graph.insertEdge(parent, null, "Edge 11", s4, s5);
			graph.insertEdge(parent, null, "Edge 12", s3, s1);
			graph.insertEdge(parent, null, "Edge 8", s4, s1);


		} finally {
			graph.getModel().endUpdate();
		}
		return graph;
	}
	
	private static mxGraph graph5() {
		mxGraph graph = new mxGraph();
		Object parent = graph.getDefaultParent();

		graph.getModel().beginUpdate();
		try {
			/* Build graph */
			Object s1 = graph.insertVertex(parent, null, "State 1", 0, 0, 50, 50);
			Object s2 = graph.insertVertex(parent, null, "State 2", 0, 0, 50, 50);
			Object s3 = graph.insertVertex(parent, null, "State 3", 0, 0, 50, 50);
			Object s4 = graph.insertVertex(parent, null, "State 4", 0, 0, 50, 50);
			Object s5 = graph.insertVertex(parent, null, "State 5", 0, 0, 50, 50);
			Object s6 = graph.insertVertex(parent, null, "State 6", 0, 0, 50, 50);

			graph.insertEdge(parent, null, "Edge 1", s1, s2);
			graph.insertEdge(parent, null, "Edge 2", s2, s3);
			graph.insertEdge(parent, null, "Edge 3", s3, s4);
			graph.insertEdge(parent, null, "Edge 4", s4, s5);
			graph.insertEdge(parent, null, "Edge 5", s5, s6);
			graph.insertEdge(parent, null, "Edge 6", s6, s1);
			graph.insertEdge(parent, null, "Edge 7", s1, s6);
			graph.insertEdge(parent, null, "Edge 8", s4, s2);
			graph.insertEdge(parent, null, "Edge 9", s2, s4);
			graph.insertEdge(parent, null, "Edge 10", s5, s2);
			graph.insertEdge(parent, null, "Edge 11", s2, s5);
			graph.insertEdge(parent, null, "Edge 12", s4, s3);
			graph.insertEdge(parent, null, "Edge 13", s4, s4);


		} finally {
			graph.getModel().endUpdate();
		}
		return graph;
	}
	
	private static mxGraph graph6() {
		mxGraph graph = new mxGraph();
		Object parent = graph.getDefaultParent();

		graph.getModel().beginUpdate();
		try {
			/* Build graph */
			Object s1 = graph.insertVertex(parent, null, "State 1", 0, 0, 50, 50);
			Object s2 = graph.insertVertex(parent, null, "State 2", 0, 0, 50, 50);
			Object s3 = graph.insertVertex(parent, null, "State 3", 0, 0, 50, 50);
			Object s4 = graph.insertVertex(parent, null, "State 4", 0, 0, 50, 50);
			Object s5 = graph.insertVertex(parent, null, "State 5", 0, 0, 50, 50);
			Object s6 = graph.insertVertex(parent, null, "State 6", 0, 0, 50, 50);
			Object s7 = graph.insertVertex(parent, null, "State 7", 0, 0, 50, 50);
			Object s8 = graph.insertVertex(parent, null, "State 8", 0, 0, 50, 50);

			graph.insertEdge(parent, null, "Edge 1", s1, s2);
			graph.insertEdge(parent, null, "Edge 2", s2, s3);
			graph.insertEdge(parent, null, "Edge 3", s3, s4);
			graph.insertEdge(parent, null, "Edge 4", s4, s5);
			graph.insertEdge(parent, null, "Edge 5", s5, s6);
			graph.insertEdge(parent, null, "Edge 6", s6, s1);
			graph.insertEdge(parent, null, "Edge 7", s1, s6);
			graph.insertEdge(parent, null, "Edge 8", s4, s2);
			graph.insertEdge(parent, null, "Edge 9", s2, s4);
			graph.insertEdge(parent, null, "Edge 10", s5, s2);
			graph.insertEdge(parent, null, "Edge 11", s2, s5);
			graph.insertEdge(parent, null, "Edge 12", s4, s3);
			graph.insertEdge(parent, null, "Edge 13", s4, s8);
			graph.insertEdge(parent, null, "Edge 14", s7, s8);
			graph.insertEdge(parent, null, "Edge 15", s8, s7);
			graph.insertEdge(parent, null, "Edge 16", s7, s1);
			graph.insertEdge(parent, null, "Edge 17", s1, s8);
			


		} finally {
			graph.getModel().endUpdate();
		}
		return graph;
	}
	
	
	private static mxGraph graph7() {
		mxGraph graph = new mxGraph();
		Object parent = graph.getDefaultParent();

		graph.getModel().beginUpdate();
		try {
			/* Build graph */
			Object s1 = graph.insertVertex(parent, null, "State 1", 0, 0, 50, 50);
			Object s2 = graph.insertVertex(parent, null, "State 2", 0, 0, 50, 50);
			Object s3 = graph.insertVertex(parent, null, "State 3", 0, 0, 50, 50);
			Object s4 = graph.insertVertex(parent, null, "State 4", 0, 0, 50, 50);
			Object s5 = graph.insertVertex(parent, null, "State 5", 0, 0, 50, 50);
			Object s6 = graph.insertVertex(parent, null, "State 6", 0, 0, 50, 50);
			Object s7 = graph.insertVertex(parent, null, "State 7", 0, 0, 50, 50);
			Object s8 = graph.insertVertex(parent, null, "State 8", 0, 0, 50, 50); 

			graph.insertEdge(parent, null, "Edge 1", s1, s2);
			graph.insertEdge(parent, null, "Edge 2", s2, s3);
			graph.insertEdge(parent, null, "Edge 3", s3, s4);
			graph.insertEdge(parent, null, "Edge 4", s4, s5);
			graph.insertEdge(parent, null, "Edge 5", s5, s6);
			graph.insertEdge(parent, null, "Edge 6", s6, s7);
			graph.insertEdge(parent, null, "Edge 7", s7, s8);
			graph.insertEdge(parent, null, "Edge 8", s8, s1);
			graph.insertEdge(parent, null, "Edge 9", s1, s8);
			graph.insertEdge(parent, null, "Edge 10", s8, s7);
			graph.insertEdge(parent, null, "Edge 11", s7, s6);
			graph.insertEdge(parent, null, "Edge 12", s6, s5);
			graph.insertEdge(parent, null, "Edge 13", s5, s4);
			graph.insertEdge(parent, null, "Edge 14", s4, s3);
			graph.insertEdge(parent, null, "Edge 15", s3, s2);
			graph.insertEdge(parent, null, "Edge 16", s2, s1);
			graph.insertEdge(parent, null, "Edge 17", s8, s2);
			graph.insertEdge(parent, null, "Edge 18", s7, s2);
			graph.insertEdge(parent, null, "Edge 19", s6, s2);
			graph.insertEdge(parent, null, "Edge 20", s5, s2);
			graph.insertEdge(parent, null, "Edge 21", s4, s2);
			graph.insertEdge(parent, null, "Edge 22", s3, s2);
			graph.insertEdge(parent, null, "Edge 23", s7, s1);
			graph.insertEdge(parent, null, "Edge 24", s7, s8);
			graph.insertEdge(parent, null, "Edge 25", s6, s5);
			graph.insertEdge(parent, null, "Edge 26", s6, s4);
			

		} finally {
			graph.getModel().endUpdate();
		}
		return graph;
	}
	
	private static mxGraph graph8() {
		mxGraph graph = new mxGraph();
		Object parent = graph.getDefaultParent();

		graph.getModel().beginUpdate();
		try {
			/* Build graph */
			Object s1 = graph.insertVertex(parent, null, "State 1", 0, 0, 50, 50);
			Object s2 = graph.insertVertex(parent, null, "State 2", 0, 0, 50, 50);
			Object s3 = graph.insertVertex(parent, null, "State 3", 0, 0, 50, 50);
			Object s4 = graph.insertVertex(parent, null, "State 4", 0, 0, 50, 50);
			Object s5 = graph.insertVertex(parent, null, "State 5", 0, 0, 50, 50);
			Object s6 = graph.insertVertex(parent, null, "State 6", 0, 0, 50, 50);
			Object s7 = graph.insertVertex(parent, null, "State 7", 0, 0, 50, 50);
			Object s8 = graph.insertVertex(parent, null, "State 8", 0, 0, 50, 50); 

			graph.insertEdge(parent, null, "Edge 1", s1, s2);
			graph.insertEdge(parent, null, "Edge 2", s2, s3);
			graph.insertEdge(parent, null, "Edge 3", s3, s4);
			graph.insertEdge(parent, null, "Edge 4", s4, s1);
			graph.insertEdge(parent, null, "Edge 5", s1, s4);
			graph.insertEdge(parent, null, "Edge 6", s4, s3);
			graph.insertEdge(parent, null, "Edge 7", s3, s2);
			graph.insertEdge(parent, null, "Edge 8", s2, s1);
			graph.insertEdge(parent, null, "Edge 9", s5, s6);
			graph.insertEdge(parent, null, "Edge 10", s6, s7);
			graph.insertEdge(parent, null, "Edge 11", s7, s8);
			graph.insertEdge(parent, null, "Edge 12", s8, s5);
			graph.insertEdge(parent, null, "Edge 13", s5, s8);
			graph.insertEdge(parent, null, "Edge 14", s8, s7);
			graph.insertEdge(parent, null, "Edge 15", s7, s6);
			graph.insertEdge(parent, null, "Edge 16", s6, s5);
			

		} finally {
			graph.getModel().endUpdate();
		}
		return graph;
	}
	
	private static mxGraph graph9() {
		mxGraph graph = new mxGraph();
		Object parent = graph.getDefaultParent();

		graph.getModel().beginUpdate();
		try {
			/* Build graph */
			Object s1 = graph.insertVertex(parent, null, "State 1", 0, 0, 50, 50);
			Object s2 = graph.insertVertex(parent, null, "State 2", 0, 0, 50, 50);
			Object s3 = graph.insertVertex(parent, null, "State 3", 0, 0, 50, 50);
			Object s4 = graph.insertVertex(parent, null, "State 4", 0, 0, 50, 50);
			Object s5 = graph.insertVertex(parent, null, "State 5", 0, 0, 50, 50);
			Object s6 = graph.insertVertex(parent, null, "State 6", 0, 0, 50, 50);
			Object s7 = graph.insertVertex(parent, null, "State 7", 0, 0, 50, 50);
			Object s8 = graph.insertVertex(parent, null, "State 8", 0, 0, 50, 50); 

			graph.insertEdge(parent, null, "Edge 1", s1, s2);
			graph.insertEdge(parent, null, "Edge 2", s2, s3);
			graph.insertEdge(parent, null, "Edge 3", s1, s4);
			graph.insertEdge(parent, null, "Edge 4", s4, s5);
			graph.insertEdge(parent, null, "Edge 5", s5, s6);
			graph.insertEdge(parent, null, "Edge 6", s4, s7);
			graph.insertEdge(parent, null, "Edge 7", s7, s8);
			graph.insertEdge(parent, null, "Edge 8", s5, s8);
			graph.insertEdge(parent, null, "Edge 9", s2, s5);
			graph.insertEdge(parent, null, "Edge 10", s3, s6);
			

		} finally {
			graph.getModel().endUpdate();
		}
		return graph;
	}
	
	private static mxGraph graph11() {
		mxGraph graph = new mxGraph();
		Object parent = graph.getDefaultParent();

		graph.getModel().beginUpdate();
		try {
			/* Build graph */
			Object s1 = graph.insertVertex(parent, null, "State 1", 0, 0, 50, 50);
			Object s2 = graph.insertVertex(parent, null, "State 2", 0, 0, 50, 50);
			Object s3 = graph.insertVertex(parent, null, "State 3", 0, 0, 50, 50);
			Object s4 = graph.insertVertex(parent, null, "State 4", 0, 0, 50, 50);
			Object s5 = graph.insertVertex(parent, null, "State 5", 0, 0, 50, 50);
			Object s6 = graph.insertVertex(parent, null, "State 6", 0, 0, 50, 50);
			Object s7 = graph.insertVertex(parent, null, "State 7", 0, 0, 50, 50);
			Object s8 = graph.insertVertex(parent, null, "State 8", 0, 0, 50, 50); 
			

			graph.insertEdge(parent, null, "Edge 1", s1, s2);
			graph.insertEdge(parent, null, "Edge 2", s2, s3);
			graph.insertEdge(parent, null, "Edge 3", s1, s4);
			graph.insertEdge(parent, null, "Edge 4", s4, s5);
			graph.insertEdge(parent, null, "Edge 5", s5, s6);
			graph.insertEdge(parent, null, "Edge 6", s4, s7);
			graph.insertEdge(parent, null, "Edge 7", s7, s8);
			graph.insertEdge(parent, null, "Edge 8", s5, s8);
			graph.insertEdge(parent, null, "Edge 9", s2, s5);
			graph.insertEdge(parent, null, "Edge 10", s3, s6);
			

		} finally {
			graph.getModel().endUpdate();
		}
		return graph;
	}
	
	private static mxGraph graph12() {
		mxGraph graph = new mxGraph();
		Object parent = graph.getDefaultParent();

		graph.getModel().beginUpdate();
		try {
			/* Build graph */
			Object s1 = graph.insertVertex(parent, null, "State 1", 0, 0, 50, 50);
			Object s2 = graph.insertVertex(parent, null, "State 2", 0, 0, 50, 50);
			Object s3 = graph.insertVertex(parent, null, "State 3", 0, 0, 50, 50);
			Object s4 = graph.insertVertex(parent, null, "State 4", 0, 0, 50, 50);
			Object s5 = graph.insertVertex(parent, null, "State 5", 0, 0, 50, 50);
			Object s6 = graph.insertVertex(parent, null, "State 6", 0, 0, 50, 50);
			Object s7 = graph.insertVertex(parent, null, "State 7", 0, 0, 50, 50);
			Object s8 = graph.insertVertex(parent, null, "State 8", 0, 0, 50, 50); 
			Object s9 = graph.insertVertex(parent, null, "State 9", 0, 0, 50, 50); 
			

			graph.insertEdge(parent, null, "Edge 1", s1, s2);
			graph.insertEdge(parent, null, "Edge 2", s2, s3);
			graph.insertEdge(parent, null, "Edge 3", s3, s4);
			graph.insertEdge(parent, null, "Edge 4", s4, s5);
			graph.insertEdge(parent, null, "Edge 5", s5, s6);
			graph.insertEdge(parent, null, "Edge 6", s6, s7);
			graph.insertEdge(parent, null, "Edge 7", s7, s8);
			graph.insertEdge(parent, null, "Edge 8", s8, s9);
			graph.insertEdge(parent, null, "Edge 9", s9, s1);
			graph.insertEdge(parent, null, "Edge 10", s9, s8);
			graph.insertEdge(parent, null, "Edge 11", s8, s7);
			graph.insertEdge(parent, null, "Edge 12", s7, s6);
			graph.insertEdge(parent, null, "Edge 13", s6, s5);
			graph.insertEdge(parent, null, "Edge 14", s5, s4);
			graph.insertEdge(parent, null, "Edge 15", s4, s3);
			graph.insertEdge(parent, null, "Edge 16", s3, s2);
			graph.insertEdge(parent, null, "Edge 17", s2, s1);
			graph.insertEdge(parent, null, "Edge 18", s9, s5);
			graph.insertEdge(parent, null, "Edge 19", s8, s4);
			graph.insertEdge(parent, null, "Edge 20", s7, s3);
			graph.insertEdge(parent, null, "Edge 21", s6, s2);
			graph.insertEdge(parent, null, "Edge 22", s5, s1);



		} finally {
			graph.getModel().endUpdate();
		}
		return graph;
	}
	
	private static mxGraph graph13() {
		mxGraph graph = new mxGraph();
		Object parent = graph.getDefaultParent();

		graph.getModel().beginUpdate();
		try {
			/* Build graph */
			Object s1 = graph.insertVertex(parent, null, "State 1", 0, 0, 50, 50);
			Object s2 = graph.insertVertex(parent, null, "State 2", 0, 0, 50, 50);
			Object s3 = graph.insertVertex(parent, null, "State 3", 0, 0, 50, 50);
			Object s4 = graph.insertVertex(parent, null, "State 4", 0, 0, 50, 50);
			Object s5 = graph.insertVertex(parent, null, "State 5", 0, 0, 50, 50);
			Object s6 = graph.insertVertex(parent, null, "State 6", 0, 0, 50, 50);
			Object s7 = graph.insertVertex(parent, null, "State 7", 0, 0, 50, 50);
			Object s8 = graph.insertVertex(parent, null, "State 8", 0, 0, 50, 50); 
			Object s9 = graph.insertVertex(parent, null, "State 9", 0, 0, 50, 50); 
			

			graph.insertEdge(parent, null, "Edge 1", s1, s2);
			graph.insertEdge(parent, null, "Edge 2", s2, s1);
			graph.insertEdge(parent, null, "Edge 3", s3, s2);
			graph.insertEdge(parent, null, "Edge 4", s2, s3);
			graph.insertEdge(parent, null, "Edge 5", s1, s4);
			graph.insertEdge(parent, null, "Edge 6", s4, s1);
			graph.insertEdge(parent, null, "Edge 7", s2, s5);
			graph.insertEdge(parent, null, "Edge 8", s5, s2);
			graph.insertEdge(parent, null, "Edge 9", s3, s6);
			graph.insertEdge(parent, null, "Edge 10", s6, s3);
			graph.insertEdge(parent, null, "Edge 11", s4, s5);
			graph.insertEdge(parent, null, "Edge 12", s5, s4);
			graph.insertEdge(parent, null, "Edge 13", s6, s5);
			graph.insertEdge(parent, null, "Edge 14", s5, s6);
			graph.insertEdge(parent, null, "Edge 15", s4, s7);
			graph.insertEdge(parent, null, "Edge 16", s7, s4);
			graph.insertEdge(parent, null, "Edge 17", s5, s8);
			graph.insertEdge(parent, null, "Edge 18", s8, s5);
			graph.insertEdge(parent, null, "Edge 19", s6, s9);
			graph.insertEdge(parent, null, "Edge 20", s9, s6);
			graph.insertEdge(parent, null, "Edge 21", s7, s8);
			graph.insertEdge(parent, null, "Edge 22", s8, s7);
			graph.insertEdge(parent, null, "Edge 23", s8, s9);
			graph.insertEdge(parent, null, "Edge 24", s9, s8);
			

		} finally {
			graph.getModel().endUpdate();
		}
		return graph;
	}
	
	private static mxGraph graph14() {
		mxGraph graph = new mxGraph();
		Object parent = graph.getDefaultParent();

		graph.getModel().beginUpdate();
		try {
			/* Build graph */
			Object s1 = graph.insertVertex(parent, null, "State 1", 0, 0, 50, 50);
			Object s2 = graph.insertVertex(parent, null, "State 2", 0, 0, 50, 50);
			Object s3 = graph.insertVertex(parent, null, "State 3", 0, 0, 50, 50);
			Object s4 = graph.insertVertex(parent, null, "State 4", 0, 0, 50, 50);
			Object s5 = graph.insertVertex(parent, null, "State 5", 0, 0, 50, 50);
			Object s6 = graph.insertVertex(parent, null, "State 6", 0, 0, 50, 50);
			Object s7 = graph.insertVertex(parent, null, "State 7", 0, 0, 50, 50);
			Object s8 = graph.insertVertex(parent, null, "State 8", 0, 0, 50, 50); 
			Object s9 = graph.insertVertex(parent, null, "State 9", 0, 0, 50, 50); 
			

			graph.insertEdge(parent, null, "Edge 1", s1, s4);
			graph.insertEdge(parent, null, "Edge 2", s4, s1);
			graph.insertEdge(parent, null, "Edge 3", s4, s7);
			graph.insertEdge(parent, null, "Edge 4", s7, s4);
			graph.insertEdge(parent, null, "Edge 5", s1, s7);
			graph.insertEdge(parent, null, "Edge 6", s7, s1);
			graph.insertEdge(parent, null, "Edge 7", s2, s5);
			graph.insertEdge(parent, null, "Edge 8", s5, s2);
			graph.insertEdge(parent, null, "Edge 9", s5, s8);
			graph.insertEdge(parent, null, "Edge 10", s8, s5);
			graph.insertEdge(parent, null, "Edge 11", s8, s2);
			graph.insertEdge(parent, null, "Edge 12", s2, s8);
			graph.insertEdge(parent, null, "Edge 13", s3, s6);
			graph.insertEdge(parent, null, "Edge 14", s6, s3);
			graph.insertEdge(parent, null, "Edge 15", s6, s9);
			graph.insertEdge(parent, null, "Edge 16", s9, s6);
			graph.insertEdge(parent, null, "Edge 17", s9, s3);
			graph.insertEdge(parent, null, "Edge 18", s3, s9);

			

		} finally {
			graph.getModel().endUpdate();
		}
		return graph;
	}
	
	private static mxGraph graph15() {
		mxGraph graph = new mxGraph();
		Object parent = graph.getDefaultParent();

		graph.getModel().beginUpdate();
		try {
			/* Build graph */
			Object s1 = graph.insertVertex(parent, null, "State 1", 0, 0, 50, 50);
			Object s2 = graph.insertVertex(parent, null, "State 2", 0, 0, 50, 50);
			Object s3 = graph.insertVertex(parent, null, "State 3", 0, 0, 50, 50);
			Object s4 = graph.insertVertex(parent, null, "State 4", 0, 0, 50, 50);
			Object s5 = graph.insertVertex(parent, null, "State 5", 0, 0, 50, 50);
			Object s6 = graph.insertVertex(parent, null, "State 6", 0, 0, 50, 50);
			Object s7 = graph.insertVertex(parent, null, "State 7", 0, 0, 50, 50);
			Object s8 = graph.insertVertex(parent, null, "State 8", 0, 0, 50, 50); 
			Object s9 = graph.insertVertex(parent, null, "State 9", 0, 0, 50, 50); 
			

			graph.insertEdge(parent, null, "Edge 1", s1, s2);
			graph.insertEdge(parent, null, "Edge 2", s2, s1);
			graph.insertEdge(parent, null, "Edge 3", s2, s3);
			graph.insertEdge(parent, null, "Edge 4", s3, s2);
			graph.insertEdge(parent, null, "Edge 5", s3, s6);
			graph.insertEdge(parent, null, "Edge 6", s6, s3);
			graph.insertEdge(parent, null, "Edge 7", s6, s9);
			graph.insertEdge(parent, null, "Edge 8", s9, s6);
			graph.insertEdge(parent, null, "Edge 9", s9, s8);
			graph.insertEdge(parent, null, "Edge 10", s8, s9);
			graph.insertEdge(parent, null, "Edge 11", s8, s7);
			graph.insertEdge(parent, null, "Edge 12", s7, s8);
			graph.insertEdge(parent, null, "Edge 13", s4, s7);
			graph.insertEdge(parent, null, "Edge 14", s7, s4);
			graph.insertEdge(parent, null, "Edge 15", s4, s1);
			graph.insertEdge(parent, null, "Edge 16", s1, s4);
			graph.insertEdge(parent, null, "Edge 17", s5, s1);
			graph.insertEdge(parent, null, "Edge 18", s5, s2);
			graph.insertEdge(parent, null, "Edge 19", s5, s3);
			graph.insertEdge(parent, null, "Edge 20", s5, s4);
			graph.insertEdge(parent, null, "Edge 21", s5, s5);
			graph.insertEdge(parent, null, "Edge 22", s5, s6);
			graph.insertEdge(parent, null, "Edge 23", s5, s7);
			graph.insertEdge(parent, null, "Edge 24", s5, s8);
			graph.insertEdge(parent, null, "Edge 17", s5, s9);

			

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
		mxGraph graph = graph1();
		new GraphTest(graph);
	}

}
