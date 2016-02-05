package controller;

import java.util.Collections;
import java.util.List;

import model.AddStateCommand;
import model.BreakdownCommand;
import model.BreakdownSequenceCommand;
import model.Command;
import view.GraphCanvasFX;
import view.GraphNode;

public class BreakdownSequenceUICommand extends BreakdownUICommand {
    private final BreakdownSequenceCommand ccmd;

    public BreakdownSequenceUICommand(GraphCanvasFX graph,
            BreakdownSequenceCommand cmd) {
        super(graph, cmd);
        ccmd = cmd;

        int transCount = ccmd.getNewTransitionsCount();
        GraphNode fromNode = graph
                .lookupNode(ccmd.getOriginalTransition().getFrom().getId());
        GraphNode toNode = graph
                .lookupNode(ccmd.getOriginalTransition().getTo().getId());
        double dxPerNode = (toNode.getX() - fromNode.getX()) / transCount;
        double dyPerNode = (toNode.getY() - fromNode.getY()) / transCount;

        double curX = fromNode.getX() + dxPerNode;
        double curY = fromNode.getY() + dyPerNode;

        for (Command tmpCmd : ccmd.getCommands()) {
            if (tmpCmd instanceof AddStateCommand) {
                AddStateCommand oldCommand = (AddStateCommand) tmpCmd;
                AddStateUICommand newCommand = new AddStateUICommand(graph,
                        oldCommand, curX, curY);
                this.commands.add(newCommand);
                curX += dxPerNode;
                curY += dyPerNode;
            } else {
                this.commands.add(UICommand.fromCommand(graph, tmpCmd));
            }
        }
    }
}
