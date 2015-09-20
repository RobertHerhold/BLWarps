package com.blocklaunch.blwarps.eventhandlers;

import com.blocklaunch.blwarps.BLWarps;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DamageEntityEvent;

public class DamageEntityEventHandler {

    private BLWarps plugin;

    public DamageEntityEventHandler(BLWarps plugin) {
        this.plugin = plugin;
    }

    @Listener
    public void onDamageEntity(DamageEntityEvent event) {
        if (!(event.getTargetEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getTargetEntity();
        
        if (!this.plugin.getConfig().isPvpProtect()) {
            return;
        }
        // pvp-protect setting is enabled
        
        if (!this.plugin.getWarpManager().isWarping(player)) {
            return;
        }
        // Player is warping
        
        this.plugin.getWarpManager().cancelWarp(player);

    }
}
