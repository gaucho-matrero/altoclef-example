package adris.example.tasks;

import adris.altoclef.AltoClef;
import adris.altoclef.TaskCatalogue;
import adris.altoclef.tasks.resources.KillAndLootTask;
import adris.altoclef.tasksystem.Task;
import adris.altoclef.util.ItemTarget;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.item.Items;

public class PunkCowsExampleTask extends Task {

    @Override
    protected void onStart(AltoClef altoClef) {
        // Run for the first frame of this task.
    }

    @Override
    protected Task onTick(AltoClef altoClef) {
        // Run every frame this task runs.

        /* This task will:
         * - Get an iron sword if we don't have one
         * - Attack cows
         */
        if (!altoClef.getInventoryTracker().hasItem("iron_sword")) {
            return TaskCatalogue.getItemTask("iron_sword", 1);
        }
        return new KillAndLootTask(CowEntity.class, new ItemTarget(Items.BEEF), new ItemTarget(Items.LEATHER));
        //return new KillEntitiesTask(CowEntity.class);
    }

    @Override
    protected void onStop(AltoClef altoClef, Task task) {
        // Run after this task stops or is interrupted.
        // the `task` variable will hold the task that interrupted this one (can be null), but it's rarely (if ever) used
    }

    @Override
    protected boolean isEqual(Task task) {
        // This is VERY important when running sub tasks.
        // Make sure EVERY task has a reliable "isEqual" function.
        return task instanceof PunkCowsExampleTask;
    }

    @Override
    protected String toDebugString() {
        // Used for debug printing.
        return "Doing Example Stuff";
    }
}
