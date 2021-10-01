package adris.example.commands;

import adris.altoclef.AltoClef;
import adris.altoclef.commandsystem.ArgParser;
import adris.altoclef.commandsystem.Command;
import adris.altoclef.commandsystem.CommandException;

public class ExampleCommand extends Command {
    public ExampleCommand() {
        super("example", "An example command");
    }

    @Override
    protected void Call(AltoClef altoClef, ArgParser argParser) throws CommandException {
        altoClef.log("Hello World!");
    }
}
