package com.blocklaunch.blwarps.managers;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Constants;
import com.blocklaunch.blwarps.managers.storage.StorageManager;
import com.blocklaunch.blwarps.region.WarpRegion;
import com.blocklaunch.blwarps.region.WarpRegionMBRConverter;
import java.util.Optional;
import org.khelekore.prtree.PRTree;

public class WarpRegionManager extends WarpBaseManager<WarpRegion> {

    private PRTree<WarpRegion> tree;
    private static final int BRANCH_FACTOR = 5;

    public WarpRegionManager(BLWarps plugin, StorageManager<WarpRegion> storage) {
        super(plugin, storage);
        this.tree = constructPRTree();
    }

    /**
     * @param warpName The name of the warp
     * @param warpLocation The location of the warp
     * @return An error if the warp already exists, Optional.empty() otherwise
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

        // Reload the PR tree w/ the new region
        reloadPRTree();

        // No errors, return an absent optional
        return Optional.empty();

    }

    @Override
    public void load() {
        super.load();
        reloadPRTree();
    }

    @Override
    public void deleteOne(WarpRegion region) {
        super.deleteOne(region);
        reloadPRTree();
    }

    /**
     * Load warp regions into PR tree
     */
    private void reloadPRTree() {
        try {
            this.tree.load(super.getPayload());
        } catch (IllegalStateException e) {
            // Tree is already loaded
            // Have to re-instantiate the tree b/c you cannot load the regions
            // twice
            this.tree = constructPRTree();
            this.tree.load(super.getPayload());
        }

    }

    private PRTree<WarpRegion> constructPRTree() {
        return new PRTree<WarpRegion>(new WarpRegionMBRConverter(), BRANCH_FACTOR);
    }

    public PRTree<WarpRegion> getPRTree() {
        return this.tree;
    }

}
