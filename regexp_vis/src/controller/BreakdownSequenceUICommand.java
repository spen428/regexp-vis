package controller;

import javafx.geometry.Point2D;
import model.AddStateCommand;
import model.BreakdownSequenceCommand;
import model.Command;
import view.GraphCanvasFX;

public class BreakdownSequenceUICommand extends BreakdownUICommand {
    private final BreakdownSequenceCommand ccmd;

    public BreakdownSequenceUICommand(GraphCanvasFX graph,
            BreakdownSequenceCommand cmd) {
        super(graph, cmd);
        this.ccmd = cmd;

        Point2D[] points = BreakdownUITools.placeNodes(graph, this.ccmd);
        int added = 0;

        for (Command c : this.ccmd.getCommands()) {
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
