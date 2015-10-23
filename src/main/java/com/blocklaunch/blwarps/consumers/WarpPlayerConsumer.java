package com.blocklaunch.blwarps.consumers;

import org.spongepowered.api.service.scheduler.Task;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Constants;
import com.blocklaunch.blwarps.Warp;
import org.spongepowered.api.entity.living.player.Player;

import java.util.function.Consumer;

public class WarpPlayerConsumer implements Consumer<Task> {

    private BLWarps plugin;
    private Player player;
    private Warp warp;

    public WarpPlayerConsumer(BLWarps plugin, Player player, Warp warp) {
        this.plugin = plugin;
        this.player = player;
        this.warp = warp;
    }

    @Override
    public void accept(Task task) {
        if (!this.player.transferToWorld(this.warp.getWorld(), this.warp.getPosition())) {
            this.player.sendMessage(Constants.WORLD_NOT_FOUND_MSG);
        }

        this.plugin.getWarpManager().warpCompleted(this.player);

    }

    public Warp getWarp() {
        return this.warp;
    }

}
