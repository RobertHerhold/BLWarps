package com.blocklaunch.blwarps.runnables;

import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Constants;
import com.blocklaunch.blwarps.Warp;

public class WarpPlayerRunnable implements Runnable {

    private BLWarps plugin;
    private Player player;
    private Warp warp;

    public WarpPlayerRunnable(BLWarps plugin, Player player, Warp warp) {
        this.plugin = plugin;
        this.player = player;
        this.warp = warp;
    }

    @Override
    public void run() {
        if (!player.transferToWorld(warp.getWorld(), warp.getPosition())) {
            player.sendMessage(Constants.WORLD_NOT_FOUND_MSG);
        } else {
            player.sendMessage(Texts.builder(Constants.WARP_SUCCESS_MSG).color(TextColors.GREEN).append(plugin.getUtil().generateWarpText(warp))
                    .build());

        }

        plugin.getWarpManager().warpCompleted(player);

    }

}
