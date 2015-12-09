package ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import model.Command;
import model.TranslationTools;

import com.mxgraph.model.mxCell;

/**
 * Handling code for when the user double-clicks a node or edge of the graph.
 * 
 * @author sp611
 * 
 */
public class DoubleClickHandler extends MouseAdapter {

    private final GraphPanel graphPanel;

    public DoubleClickHandler(GraphPanel graphPanel) {
        this.graphPanel = graphPanel;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (this.graphPanel.isEnabled()) {
            if (!e.isConsumed() && this.graphPanel.isEditEvent(e)) {
                Object cell = this.graphPanel.getCellAt(e.getX(), e.getY(),
                        false);

                if (cell != null
                        && this.graphPanel.getGraph().isCellEditable(cell)) {
                    System.out.printf("Double-clicked cell: %s %s " + "%s%n",
                            cell, e, e.getPoint());
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

    private void breakDown(mxCell cell) {
        Graph graph = (Graph) this.graphPanel.getGraph();
        Command cmd = TranslationTools.createBreakdownCommand(
                graph.getAutomaton(), graph.getTransitionFromCell(cell));
        this.graphPanel.executeNewCommand(cmd);
    }

}
