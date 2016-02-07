package controller;

import model.BreakdownCommand;
import model.Command;
import view.GraphCanvasFX;

/**
 * 
 * @author sp611
 *
 */
public class BreakdownOptionUICommand extends BreakdownUICommand {

    public BreakdownOptionUICommand(GraphCanvasFX graph, BreakdownCommand cmd) {
        super(graph, cmd);
        for (Command c : cmd.getCommands()) {
            this.commands.add(UICommand.fromCommand(graph, c));
        }
    }

}
