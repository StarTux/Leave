package com.winthier.leave;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class LeavePlugin extends JavaPlugin implements Listener {
    // Constants
    private final BlockFace[] directions = {BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
    private static final int LOG_DISTANCE = 6;
    private final LeafBlower leafBlower = new LeafBlower(this);

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event) {
        // Check block
        final Block block = event.getBlock();
        if (!isLog(block) && !isLeaf(block)) return;
        // Check for neighbor leaves
        for (BlockFace face : directions) {
            Block other = block.getRelative(face);
            if (isLeaf(other)) leafBlower.addBlock(other);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onLeavesDecay(LeavesDecayEvent event) {
        // Check neighbor leaves
        for (BlockFace face : directions) {
            Block other = event.getBlock().getRelative(face);
            if (isLeaf(other)) leafBlower.addBlock(other);
        }
    }

    public boolean updateLeaf(Block leaf) {
        if (!isLeaf(leaf)) return false;
        // Check connectedness
        if (new LogFinder().search(leaf, LOG_DISTANCE).isConnected()) return false;
        // Call event
        LeavesDecayEvent event = new LeavesDecayEvent(leaf);
        getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) return false;
        // Break leaf
        leaf.breakNaturally();
        return true;
    }

    public static boolean isLeaf(Block block) {
        // Check if block is leaf
        switch (block.getType()) {
        case ACACIA_LEAVES:
        case BIRCH_LEAVES:
        case DARK_OAK_LEAVES:
        case JUNGLE_LEAVES:
        case OAK_LEAVES:
        case SPRUCE_LEAVES:
            Leaves leaves = (Leaves)block.getBlockData();
            return !leaves.isPersistent();
        default:
            return false;
        }
    }

    public static boolean isLog(Block block) {
        switch (block.getType()) {
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
            return true;
        default:
            return false;
        }
    }
}
