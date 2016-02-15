package controller;

import javafx.geometry.Point2D;
import model.AddStateCommand;
import model.BreakdownIterationCommand;
import model.Command;
import view.GraphCanvasFX;

/**
 * 
 * @author sp611
 *
 */
public class BreakdownIterationUICommand extends BreakdownUICommand {
    private final BreakdownIterationCommand ccmd;

    public BreakdownIterationUICommand(GraphCanvasFX graph,
            BreakdownIterationCommand cmd) {
        super(graph, cmd);
        this.ccmd = cmd;

        Point2D[] points = BreakdownUITools.placeNodes(graph, this.ccmd);
        int added = 0;

        for (Command c : cmd.getCommands()) {
            if (c instanceof AddStateCommand) {
                Point2D loc = points[added++];
                AddStateUICommand newCommand = new AddStateUICommand(graph,
                        (AddStateCommand) c, loc.getX(), loc.getY());
                this.commands.add(newCommand);
            } else {
                this.commands.add(UICommand.fromCommand(graph, c));
            }
        }
    }
}
