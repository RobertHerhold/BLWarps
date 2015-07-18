package com.blocklaunch.blwarps.eventhandlers;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.region.WarpRegion;
import org.khelekore.prtree.MBR;
import org.khelekore.prtree.SimpleMBR;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.entity.player.PlayerMoveEvent;
import org.spongepowered.api.world.Location;

import java.util.ArrayList;
import java.util.List;

public class PlayerMoveEventHandler {

    private BLWarps plugin;

    public PlayerMoveEventHandler(BLWarps plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void playerMove(PlayerMoveEvent event) throws Exception {
        Player player = event.getEntity();
        Location location = event.getNewLocation();

        List<WarpRegion> warpRegions = getContainingRegions(location);

    }

    private List<WarpRegion> getContainingRegions(Location location) {
        MBR locationMBR = new SimpleMBR(location.getX(), location.getX(), location.getY(), location.getY(), location.getZ(), location.getZ());
        List<WarpRegion> warpRegions = new ArrayList<WarpRegion>();

        // Find intersecting regions & store in list
        this.plugin.getWarpRegionManager().getPRTree().find(locationMBR, warpRegions);

        return warpRegions;
    }

}
