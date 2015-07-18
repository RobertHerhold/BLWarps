package com.blocklaunch.blwarps.managers;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Constants;
import com.blocklaunch.blwarps.managers.storage.StorageManager;
import com.blocklaunch.blwarps.region.WarpRegion;
import com.blocklaunch.blwarps.region.WarpRegionMBRConverter;
import com.google.common.base.Optional;
import org.khelekore.prtree.PRTree;

public class WarpRegionManager extends WarpBaseManager<WarpRegion> {

    private PRTree<WarpRegion> tree;
    private static final int BRANCH_FACTOR = 5;

    public WarpRegionManager(BLWarps plugin, StorageManager<WarpRegion> storage) {
        super(plugin, storage);
        this.tree = new PRTree<WarpRegion>(new WarpRegionMBRConverter(), BRANCH_FACTOR);
    }

    /**
     * @param warpName The name of the warp
     * @param warpLocation The location of the warp
     * @return An error if the warp already exists, Optional.absent() otherwise
     */
    @Override
    public Optional<String> addNew(WarpRegion newWarpRegion) {

        for (WarpRegion warpRegion : this.payload) {
            if (warpRegion.getName().equalsIgnoreCase(newWarpRegion.getName())) {
                // A warp with that name already exists
                return Optional.of(Constants.WARP_REGION_NAME_EXISTS);
            }
            if (warpRegion.collidesWith(newWarpRegion)) {
                return Optional.of(Constants.WARP_REGION_LOCATION_EXISTS);
            }
        }

        this.payload.add(newWarpRegion);
        this.names.add(newWarpRegion.getName());

        // Save warps after putting a new one in rather than saving when server
        // shuts down to prevent loss of data if the server crashed
        this.storage.saveNew(newWarpRegion);

        // No errors, return an absent optional
        return Optional.absent();

    }

    @Override
    public void load() {
        super.load();
        // Load warp regions into PR tree
        this.tree.load(super.getPayload());
    }

    public PRTree<WarpRegion> getPRTree() {
        return this.tree;
    }

}
