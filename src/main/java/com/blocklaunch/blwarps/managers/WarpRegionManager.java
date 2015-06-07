package com.blocklaunch.blwarps.managers;

import java.util.ArrayList;
import java.util.List;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Constants;
import com.blocklaunch.blwarps.region.WarpRegion;
import com.google.common.base.Optional;

public class WarpRegionManager extends WarpBaseManager<WarpRegion> {
    
    private BLWarps plugin;
    
    public WarpRegionManager(BLWarps plugin) {
        this.plugin = plugin;
    }
    
    private List<WarpRegion> warpRegions = new ArrayList<WarpRegion>();
    
    /**
     * @param warpName The name of the warp
     * @param warpLocation The location of the warp
     * @return An error if the warp already exists, Optional.absent() otherwise
     */
    public Optional<String> addWarp(WarpRegion newWarpRegion) {

        for (WarpRegion warpRegion : warpRegions) {
            if (warpRegion.getName().equalsIgnoreCase(newWarpRegion.getName())) {
                // A warp with that name already exists
                return Optional.of(Constants.WARP_REGION_NAME_EXISTS);
            }
            if (warpRegion.collidesWith(newWarpRegion)) {
                return Optional.of(Constants.WARP_REGION_LOCATION_EXISTS);
            }
        }

        warpRegions.add(newWarpRegion);

        // Save warps after putting a new one in rather than saving when server
        // shuts down to prevent loss of data if the server crashed
        plugin.getStorageManager().saveNewWarp(newWarpRegion);

        // No errors, return an absent optional
        return Optional.absent();

    }

}
