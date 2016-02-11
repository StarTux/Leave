package com.winthier.leave;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class LogFinder extends BlockSearch {
    private boolean result = false;

    @Override
    public LogFinder search(Block block, int distance) {
        super.search(block, distance);
        return this;
    }

    @Override
    public boolean blockCallback(Block block) {
        switch (block.getType()) {
        case LEAVES: case LEAVES_2:
            // Continue search
            return true;
        case LOG: case LOG_2:
            // Stop search, this leaf is connected.
            result = true;
            stopSearch();
            return false;
        default:
            // Don't search neighbors
            return false;
        }
    }

    public boolean getResult() {
        return result;
    }
}
