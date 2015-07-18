package com.blocklaunch.blwarps.eventhandlers;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.region.WarpRegion;
import com.blocklaunch.blwarps.region.WarpRegionMBRConverter;
import org.khelekore.prtree.MBR;
import org.khelekore.prtree.PRTree;
import org.khelekore.prtree.SimpleMBR;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.entity.player.PlayerMoveEvent;
import org.spongepowered.api.world.Location;

import java.util.ArrayList;
import java.util.List;

public class PlayerMoveEventHandler {

    private BLWarps plugin;
    private PRTree<WarpRegion> tree;
    private static final int BRANCH_FACTOR = 5;

    public PlayerMoveEventHandler(BLWarps plugin) {
        this.plugin = plugin;
        this.tree = new PRTree<WarpRegion>(new WarpRegionMBRConverter(), BRANCH_FACTOR);
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
        for (WarpRegion r : this.tree.find(locationMBR)) {
            warpRegions.add(r);
        }

        return warpRegions;
    }

}
