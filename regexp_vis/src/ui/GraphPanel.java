package ui;

import java.util.HashMap;
import java.util.Map;

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
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxPerimeter;
import com.mxgraph.view.mxStylesheet;

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

	/**
	 * Default diameter of vertices.
	 */
	public static final int VERTEX_DIAMETER_PX = 50;

	private final mxGraph graph;
	private final mxGraphLayout vertexCircleLayout, vertexOrganicLayout,
			vertexFastOrganicLayout, edgeLayout, edgeLabelLayout;

	public GraphPanel() {
		this(new mxGraph());
	}

	public GraphPanel(mxGraph graph) {
		super(graph);

		this.graph = graph;
		graph.setStylesheet(generateStylesheet());
		Object parent = graph.getDefaultParent();

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

			/* Perform automatic layout. */
			vertexCircleLayout.execute(parent);
			edgeLayout.execute(parent);
			edgeLabelLayout.execute(parent);
		} finally {
			graph.getModel().endUpdate();
		}
	}

	/**
	 * Generates a stylesheet defining the default visual appearance of the
	 * graph.
	 */
	private static mxStylesheet generateStylesheet() {
		Map<String, Object> edgeStyle = new HashMap<String, Object>();
		/* The curve shape seems to mess up the arrow heads. */
		// edgeStyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CURVE);
		edgeStyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CONNECTOR);
		edgeStyle.put(mxConstants.STYLE_STROKECOLOR, "#000000");
		edgeStyle.put(mxConstants.STYLE_FONTSTYLE, mxConstants.FONT_BOLD);
		edgeStyle.put(mxConstants.STYLE_FONTSIZE, "12");
		edgeStyle.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);
		edgeStyle.put(mxConstants.STYLE_VERTICAL_ALIGN,
				mxConstants.ALIGN_MIDDLE);
		edgeStyle.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC);

		Map<String, Object> vertexStyle = new HashMap<String, Object>();
		vertexStyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
		vertexStyle.put(mxConstants.STYLE_STROKECOLOR, "#000000");
		vertexStyle.put(mxConstants.STYLE_FILLCOLOR, "#ffffff");
		vertexStyle.put(mxConstants.STYLE_FONTSIZE, "14");
		vertexStyle.put(mxConstants.STYLE_PERIMETER,
				mxPerimeter.EllipsePerimeter);
		vertexStyle.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);
		vertexStyle.put(mxConstants.STYLE_VERTICAL_ALIGN,
				mxConstants.ALIGN_MIDDLE);

		mxStylesheet styleSheet = new mxStylesheet();
		styleSheet.setDefaultEdgeStyle(edgeStyle);
		styleSheet.setDefaultVertexStyle(vertexStyle);
		return styleSheet;
	}

	public void setVertexLayout(int layout) {
		Object parent = graph.getDefaultParent();
		graph.getModel().beginUpdate();
		try {
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
		} finally {
			graph.getModel().endUpdate();
		}
	}

}
