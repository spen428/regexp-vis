package ui;

import java.util.HashMap;
import java.util.Map;

import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxPerimeter;
import com.mxgraph.view.mxStylesheet;

/**
 * Extends {@link mxGraph}, overriding the default stylesheet.
 * 
 * @author sp611
 *
 */
public class Graph extends mxGraph {

	/**
	 * Generates a stylesheet defining the default visual appearance of the
	 * graph.
	 */
	@Override
	protected mxStylesheet createStylesheet() {
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

}
