package com.blocklaunch.blwarps.runnable;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Warp;

import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;

public class WarpPlayerRunnable implements Runnable {

    private BLWarps plugin;
    private Player player;
    private Warp warp;

    private static final String WARP_SUCCESS_MSG = BLWarps.PREFIX + " You have been warped to ";
    private static final Text WORLD_NOT_FOUND_MSG = Texts.of(TextColors.RED, BLWarps.PREFIX
            + " The world you requested to be warped to could not be found!");

    public WarpPlayerRunnable(BLWarps plugin, Player player, Warp warp) {
        this.plugin = plugin;
        this.player = player;
        this.warp = warp;
    }

    @Override
    public void run() {
        if (!player.transferToWorld(warp.getWorld(), warp.getPosition())) {
            player.sendMessage(WORLD_NOT_FOUND_MSG);
        } else {
            player.sendMessage(Texts.builder(WARP_SUCCESS_MSG).color(TextColors.GREEN).append(Texts.of(TextColors.GOLD, warp.getName())).build());

        }

        plugin.getWarpManager().warpCompleted(player);

    }

}
