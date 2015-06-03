package com.blocklaunch.blwarps.listeners;

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

public class PlayerMoveListener {
    
    private PRTree<WarpRegion> tree;
    private static final int BRANCH_FACTOR = 5;
    
    public PlayerMoveListener() {
        tree = new PRTree<WarpRegion>(new WarpRegionMBRConverter(), BRANCH_FACTOR);
    }
    
    @Subscribe
    public void playerMove(PlayerMoveEvent event) {
        Player player = event.getEntity();
        Location location = event.getNewLocation();
        
        List<WarpRegion> warpRegions = getContainingRegions(location);
    }
    
    private List<WarpRegion> getContainingRegions(Location location) {
        MBR locationMBR = new SimpleMBR(location.getX(), location.getX(), location.getY(), location.getY(), location.getZ(), location.getZ());
        
        List<WarpRegion> warpRegions = new ArrayList<WarpRegion>();
        for(WarpRegion r : tree.find(locationMBR)) {
            warpRegions.add(r);
        }
        
        return warpRegions;
    }

}
