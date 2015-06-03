package com.blocklaunch.blwarps.eventhandlers;

import java.util.ArrayList;
import java.util.List;

import org.khelekore.prtree.MBR;
import org.khelekore.prtree.PRTree;
import org.khelekore.prtree.SimpleMBR;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.EventHandler;
import org.spongepowered.api.event.entity.player.PlayerMoveEvent;
import org.spongepowered.api.world.Location;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.region.WarpRegion;
import com.blocklaunch.blwarps.region.WarpRegionMBRConverter;

public class PlayerMoveEventHandler implements EventHandler<PlayerMoveEvent> {

    private BLWarps plugin;
    private PRTree<WarpRegion> tree;
    private static final int BRANCH_FACTOR = 5;

    public PlayerMoveEventHandler(BLWarps plugin) {
        this.plugin = plugin;
        tree = new PRTree<WarpRegion>(new WarpRegionMBRConverter(), BRANCH_FACTOR);
    }

    @Override
    public void handle(PlayerMoveEvent event) throws Exception {
        Player player = event.getEntity();
        Location location = event.getNewLocation();

        List<WarpRegion> warpRegions = getContainingRegions(location);

    }

    private List<WarpRegion> getContainingRegions(Location location) {
        MBR locationMBR = new SimpleMBR(location.getX(), location.getX(), location.getY(), location.getY(), location.getZ(), location.getZ());

        List<WarpRegion> warpRegions = new ArrayList<WarpRegion>();
        for (WarpRegion r : tree.find(locationMBR)) {
            warpRegions.add(r);
        }

        return warpRegions;
    }

}
