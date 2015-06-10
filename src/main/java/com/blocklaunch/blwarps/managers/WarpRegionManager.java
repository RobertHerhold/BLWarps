package com.blocklaunch.blwarps.managers;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Constants;
import com.blocklaunch.blwarps.managers.storage.StorageManager;
import com.blocklaunch.blwarps.region.WarpRegion;
import com.google.common.base.Optional;

public class WarpRegionManager extends WarpBaseManager<WarpRegion> {
    
    public WarpRegionManager(BLWarps plugin, StorageManager<WarpRegion> storage) {
        super(plugin, storage);
    }
    
    /**
     * @param warpName The name of the warp
     * @param warpLocation The location of the warp
     * @return An error if the warp already exists, Optional.absent() otherwise
     */
    public Optional<String> addNew(WarpRegion newWarpRegion) {

        for (WarpRegion warpRegion : payload) {
            if (warpRegion.getName().equalsIgnoreCase(newWarpRegion.getName())) {
                // A warp with that name already exists
                return Optional.of(Constants.WARP_REGION_NAME_EXISTS);
            }
            if (warpRegion.collidesWith(newWarpRegion)) {
                return Optional.of(Constants.WARP_REGION_LOCATION_EXISTS);
            }
        }

        payload.add(newWarpRegion);
        names.add(newWarpRegion.getName());

        // Save warps after putting a new one in rather than saving when server
        // shuts down to prevent loss of data if the server crashed
        storage.saveNew(newWarpRegion);

        // No errors, return an absent optional
        return Optional.absent();

    }

}
