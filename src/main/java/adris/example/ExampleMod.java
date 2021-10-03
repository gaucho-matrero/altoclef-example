package adris.example;

import adris.altoclef.AltoClef;
import adris.example.commands.ExampleCommand;
import net.fabricmc.api.ModInitializer;

public class ExampleMod implements ModInitializer {

    @Override
    public void onInitialize() {
        System.out.println("Hello from ExampleMod!");
        AltoClef.subscribeToPostInit(this::initAltoClef);
    }

    private void initAltoClef(AltoClef altoClef) {
        System.out.println("ExampleMod ALTOCLEF INIT!");
        AltoClef.getCommandExecutor().registerNewCommand(
                new ExampleCommand()
        );
    }
}
