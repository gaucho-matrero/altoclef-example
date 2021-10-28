package adris.example.tasks;

import adris.altoclef.AltoClef;
import adris.altoclef.TaskCatalogue;
import adris.altoclef.tasks.DoToClosestBlockTask;
import adris.altoclef.tasks.InteractWithBlockTask;
import adris.altoclef.tasksystem.Task;
import adris.altoclef.util.ItemTarget;
import adris.altoclef.util.helpers.WorldHelper;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class BonemealNearbyGrassTask extends Task {

    @Override
    protected void onStart(AltoClef altoClef) {
        altoClef.getBlockTracker().trackBlock(Blocks.GRASS_BLOCK);
    }

    @Override
    protected Task onTick(AltoClef altoClef) {

        // First get bonemeal
        if (!altoClef.getInventoryTracker().hasItem(Items.BONE_MEAL)) {
            return TaskCatalogue.getItemTask("bone_meal", 1);
        }

        return new DoToClosestBlockTask(
                target -> new InteractWithBlockTask(new ItemTarget("bone_meal", 1), Direction.UP, target, true),
                (blockPos) -> !hasShrubberyNearby(altoClef, blockPos),
                Blocks.GRASS_BLOCK
        );
    }

    @Override
    protected void onStop(AltoClef altoClef, Task task) {
        altoClef.getBlockTracker().stopTracking(Blocks.GRASS_BLOCK);
    }

    @Override
    protected boolean isEqual(Task task) {
        return task instanceof BonemealNearbyGrassTask;
    }

    @Override
    protected String toDebugString() {
        return "Bonemealing nearby grass";
    }

    private static boolean hasShrubberyNearby(AltoClef altoClef, BlockPos pos) {
        int range = 4;
        for (BlockPos check : WorldHelper.scanRegion(altoClef, pos.add(-range, 0, -range), pos.add(range, 0, range))) {
            if (altoClef.getBlockTracker().blockIsValid(check, Blocks.GRASS, Blocks.TALL_GRASS, Blocks.CORNFLOWER, Blocks.DANDELION)) {
                return true;
            }
        }
        return false;
    }

}
