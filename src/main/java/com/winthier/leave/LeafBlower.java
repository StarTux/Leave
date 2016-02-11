package com.winthier.leave;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

public class LeafBlower {
    private final LeavePlugin plugin;
    private final Set<Block> leaves = new HashSet<>();
    private BukkitRunnable task = null;

    public LeafBlower(LeavePlugin plugin) {
        this.plugin = plugin;
    }

    public void start() {
        if (task != null) return;
        task = new BukkitRunnable() {
            @Override public void run() {
                tick();
            }
        };
        task.runTaskTimer(plugin, 4L, 4L);
    }

    public void stop() {
        if (task == null) return;
        try {
            task.cancel();
        } catch (IllegalStateException ise) {
            ise.printStackTrace();
        }
        task = null;
    }

    public void addBlock(Block block) {
        if (leaves.add(block)) start();
    }

    private void tick() {
        if (leaves.isEmpty()) {
            stop();
            return;
        }
        List<Block> copy = new ArrayList<>(leaves);
        leaves.clear();
        for (Block leaf : copy) {
            plugin.updateLeaf(leaf);
        }
    }
}
