package adris.example.tasks;

import adris.altoclef.AltoClef;
import adris.altoclef.tasks.InteractWithBlockTask;
import adris.altoclef.tasks.chest.PickupFromChestTask;
import adris.altoclef.tasks.construction.DestroyBlockTask;
import adris.altoclef.tasks.movement.TimeoutWanderTask;
import adris.altoclef.tasksystem.Task;
import adris.altoclef.trackers.ContainerTracker;
import adris.altoclef.util.ItemTarget;
import adris.altoclef.util.helpers.WorldHelper;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class SearchChestsForItemTask extends Task {

    private final Item _itemToFind;

    private BlockPos _targetChest;

    public SearchChestsForItemTask(Item itemToFind) {
        _itemToFind = itemToFind;
    }

    @Override
    protected void onStart(AltoClef altoClef) {
        altoClef.getBlockTracker().trackBlock(Blocks.CHEST);
    }

    @Override
    protected Task onTick(AltoClef altoClef) {

        /*
         * If item exists in a chest...
         *      Grab from that chest.
         * Otherwise search unsearched chests.
         * If no chests, explore.
         *
         * This task picks the closest chest and commits to it, meaning it will lock on to that chest until
         * it either opens it or fails to reach it.
         *
         * As an alternative, you can use `DoToClosestBlockTask` just like in the Bonemeal example.
         */

        List<BlockPos> chests = altoClef.getContainerTracker().getChestMap().getBlocksWithItem(_itemToFind);

        if (chests.isEmpty() || chests.stream().noneMatch(chestPos -> canOpenChest(altoClef, chestPos))) {
            // Search untracked chests!

            // Exit out of any open chests
            if (MinecraftClient.getInstance().currentScreen instanceof GenericContainerScreen) {
                altoClef.getControllerExtras().closeScreen();
            }

            if (_targetChest != null && (isChestTrackedAlready(altoClef, _targetChest) || !canOpenChest(altoClef, _targetChest))) {
                _targetChest = null;
            }

            if (_targetChest == null) {
                _targetChest = altoClef.getBlockTracker().getNearestTracking(
                        altoClef.getPlayer().getPos(),
                        blockPos -> !isChestTrackedAlready(altoClef, blockPos) && canOpenChest(altoClef, blockPos),
                        Blocks.CHEST
                );
            }

            if (_targetChest != null) {
                // Clear the space above
                if (WorldHelper.isSolid(altoClef, _targetChest.up())) {
                    return new DestroyBlockTask(_targetChest.up());
                }
                return new InteractWithBlockTask(_targetChest);
            }

            return new TimeoutWanderTask();
        } else {
            // Grab from the nearest chest!
            double minDistance = Double.POSITIVE_INFINITY;
            BlockPos closest = null;
            for (BlockPos pos : chests) {
                if (!canOpenChest(altoClef, pos)) continue;

                double distance = pos.getSquaredDistance(altoClef.getPlayer().getPos(), false);
                if (distance < minDistance) {
                    minDistance = distance;
                    closest = pos;
                }
            }
            if (closest != null) {
                return new PickupFromChestTask(closest, new ItemTarget(_itemToFind, 1));
            } else {
                // Something messed up...
                return new TimeoutWanderTask();
            }
        }
    }

    @Override
    protected void onStop(AltoClef altoClef, Task task) {
        altoClef.getBlockTracker().stopTracking(Blocks.CHEST);
    }

    @Override
    protected boolean isEqual(Task other) {
        if (other instanceof SearchChestsForItemTask task) {
            return task._itemToFind == _itemToFind;
        }
        return false;
    }

    @Override
    protected String toDebugString() {
        return "Searching for " + _itemToFind.getTranslationKey() + " in chests.";
    }

    private static boolean isChestTrackedAlready(AltoClef altoClef, BlockPos chestPos) {
        ContainerTracker.ChestData data = altoClef.getContainerTracker().getChestMap().getCachedChestData(chestPos);
        return data != null;
    }

    private static boolean canOpenChest(AltoClef altoClef, BlockPos chestPos) {
        BlockPos above = chestPos.up();
        boolean canReach = !altoClef.getBlockTracker().unreachable(chestPos);
        return canReach && (!WorldHelper.isSolid(altoClef, above) || WorldHelper.canBreak(altoClef, above));
    }
}
