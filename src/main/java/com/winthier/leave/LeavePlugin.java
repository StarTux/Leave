package com.winthier.leave;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority; 
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class LeavePlugin extends JavaPlugin implements Listener {
    // Constants
    private final BlockFace[] directions = { BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };
    private final int PLAYER_PLACED_BIT = 4;
    private final int LOG_DISTANCE = 4;
    private final LeafBlower leafBlower = new LeafBlower(this);

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        leafBlower.stop();
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
        if (new LogFinder().search(leaf, LOG_DISTANCE).getResult()) return false;
        // Call event
        LeavesDecayEvent event = new LeavesDecayEvent(leaf);
        getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) return false;
        // Break leaf
        leaf.breakNaturally();
        return true;
    }

    @SuppressWarnings("deprecation")
    public boolean isLeaf(Block block) {
        // Check if block is leaf
        switch (block.getType()) {
        case LEAVES: case LEAVES_2: break;
        default: return false;
        }
        // Check if leaf is permanent
        if (((int)block.getData() & PLAYER_PLACED_BIT) != 0) return false;
        return true;
    }

    public boolean isLog(Block block) {
        switch (block.getType()) {
        case LOG: case LOG_2: break;
        default: return false;
        }
        return true;
    }
}
