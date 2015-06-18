package com.blocklaunch.blwarps.runnables;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Constants;
import com.blocklaunch.blwarps.Warp;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;

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
        if (!this.player.transferToWorld(this.warp.getWorld(), this.warp.getPosition())) {
            this.player.sendMessage(Constants.WORLD_NOT_FOUND_MSG);
        } else {
            this.player.sendMessage(Texts.builder(Constants.WARP_SUCCESS_MSG).color(TextColors.GREEN)
                    .append(this.plugin.getUtil().generateWarpText(this.warp))
                    .build());

        }

        this.plugin.getWarpManager().warpCompleted(this.player);

    }

}
