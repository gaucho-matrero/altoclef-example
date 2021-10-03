package adris.example.commands;

import adris.altoclef.AltoClef;
import adris.altoclef.commandsystem.ArgParser;
import adris.altoclef.commandsystem.Command;
import adris.altoclef.commandsystem.CommandException;
import adris.example.tasks.SearchChestsForItemTask;
import net.minecraft.item.Items;

public class ExampleCommand extends Command {
    public ExampleCommand() {
        super("example", "An example command");
    }

    @Override
    protected void call(AltoClef altoClef, ArgParser argParser) throws CommandException {
        altoClef.log("Hello World!");
        altoClef.runUserTask(new SearchChestsForItemTask(Items.SADDLE), this::finish);
    }
}
