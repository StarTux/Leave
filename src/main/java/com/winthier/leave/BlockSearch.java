package com.winthier.leave;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public abstract class BlockSearch {
    private final LinkedHashMap<Block, Integer> todo = new LinkedHashMap<>();
    private final Set<Block> done = new HashSet<>();
    private final BlockFace[] directions = { BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };
    private boolean stopSearch = false;

    public BlockSearch search(Block first, int distance) {
        todo.put(first, distance);
        while (!stopSearch && !todo.isEmpty()) {
            final Block block = todo.keySet().iterator().next();
            final int depth = todo.get(block);
            todo.remove(block);
            iter(block, depth);
        }
        return this;
    }

    private final void iter(Block block, int depth) {
        if (!blockCallback(block)) return;
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

    public final void stopSearch() {
        this.stopSearch = true;
    }

    protected abstract boolean blockCallback(Block block);
}
