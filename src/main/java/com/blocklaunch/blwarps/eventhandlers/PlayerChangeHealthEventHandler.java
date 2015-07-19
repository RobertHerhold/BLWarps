package com.blocklaunch.blwarps.eventhandlers;

import com.blocklaunch.blwarps.BLWarps;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.entity.player.PlayerChangeHealthEvent;

public class PlayerChangeHealthEventHandler {

    private BLWarps plugin;

    public PlayerChangeHealthEventHandler(BLWarps plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void playerChangeHealth(PlayerChangeHealthEvent event) {
        if(plugin.getConfig().isPvpProtect()) {
            // pvp-protect setting is enabled
            if(plugin.getWarpManager().isWarping(event.getEntity())) {
                // Player is warping
                if(event.getNewData().getHealth() < event.getOldData().getHealth()) {
                    // Player was damaged
                    plugin.getWarpManager().cancelWarp(event.getEntity());
                }
            }
            
        }
    }
}
