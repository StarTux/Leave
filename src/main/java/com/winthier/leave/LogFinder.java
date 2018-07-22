package com.winthier.leave;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

@Getter
final class LogFinder {
    private final LinkedHashMap<Block, Integer> todo = new LinkedHashMap<>();
    private final Set<Block> done = new HashSet<>();
    private final BlockFace[] directions = {BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
    private boolean stopSearch = false;
    private boolean connected = false;

    LogFinder search(Block first, int distance) {
        todo.put(first, distance);
        while (!stopSearch && !todo.isEmpty()) {
            final Block block = todo.keySet().iterator().next();
            final int depth = todo.get(block);
            todo.remove(block);
            iter(block, depth);
        }
        return this;
    }

    private void iter(Block block, int depth) {
        switch (block.getType()) {
        case ACACIA_LEAVES:
        case BIRCH_LEAVES:
        case DARK_OAK_LEAVES:
        case JUNGLE_LEAVES:
        case OAK_LEAVES:
        case SPRUCE_LEAVES:
            // Continue search
            break;
        case ACACIA_LOG:
        case BIRCH_LOG:
        case DARK_OAK_LOG:
        case JUNGLE_LOG:
        case OAK_LOG:
        case SPRUCE_LOG:
        case STRIPPED_ACACIA_LOG:
        case STRIPPED_BIRCH_LOG:
        case STRIPPED_DARK_OAK_LOG:
        case STRIPPED_JUNGLE_LOG:
        case STRIPPED_OAK_LOG:
        case STRIPPED_SPRUCE_LOG:
            // Stop search, this leaf is connected.
            connected = true;
            stopSearch = true;
            return;
        default:
            // Don't search neighbors
            return;
        }
        if (depth > 0) {
            int d2 = depth - 1;
            for (BlockFace direction : directions) {
                final Block b2 = block.getRelative(direction);
                if (!done.contains(b2)) {
                    todo.put(b2, d2);
                    done.add(b2);
                }
            }
        }
    }
}
