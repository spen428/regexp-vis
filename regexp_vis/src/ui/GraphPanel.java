package ui;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxEdgeLabelLayout;
import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.layout.mxGraphLayout;
import com.mxgraph.layout.mxOrganicLayout;
import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

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

	private final mxGraph graph;
	private final mxGraphLayout vertexCircleLayout, vertexOrganicLayout,
			vertexFastOrganicLayout, edgeLayout, edgeLabelLayout;

	public GraphPanel() {
		this(new Graph());
	}

	public GraphPanel(Graph graph) {
		super(graph);
		this.graph = graph;

		/*
		 * The layouts used may vary depending on the structure of the graph. We
		 * will need to throw a bunch of different graph structures at it and
		 * see.
		 */
		this.vertexCircleLayout = new mxCircleLayout(graph);
		this.vertexOrganicLayout = new mxOrganicLayout(graph);
		this.vertexFastOrganicLayout = new mxFastOrganicLayout(graph);
		this.edgeLayout = new mxParallelEdgeLayout(graph);
		this.edgeLabelLayout = new mxEdgeLabelLayout(graph);

		setVertexLayout(0);
	}

	public void setVertexLayout(int layout) {
		Object parent = graph.getDefaultParent();
		switch (layout) {
		default:
		case 0:
			vertexCircleLayout.execute(parent);
			break;
		case 1:
			vertexFastOrganicLayout.execute(parent);
			break;
		case 2:
			vertexOrganicLayout.execute(parent);
			break;
		}
		edgeLayout.execute(parent);
		edgeLabelLayout.execute(parent);
	}

}
