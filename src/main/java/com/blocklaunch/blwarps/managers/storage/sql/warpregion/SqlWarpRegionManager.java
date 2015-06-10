package com.blocklaunch.blwarps.managers.storage.sql.warpregion;

import java.util.List;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Validity;
import com.blocklaunch.blwarps.Warp;
import com.blocklaunch.blwarps.managers.storage.StorageManager;
import com.blocklaunch.blwarps.managers.storage.sql.SqlManager;
import com.blocklaunch.blwarps.region.WarpRegion;
import com.google.common.base.Optional;

public class SqlWarpRegionManager extends SqlManager<WarpRegion> implements StorageManager<WarpRegion> {

    WarpRegionDAO warpRegionDAO;

    public SqlWarpRegionManager(BLWarps plugin) {
        super(plugin);

        // Use on demand so we don't have to bother closing connections
        warpRegionDAO = dbi.onDemand(WarpRegionDAO.class);

        warpRegionDAO.createWarpRegionTable();
    }

    @Override
    public List<WarpRegion> load() {
        List<WarpRegion> regions = warpRegionDAO.getAllWarpRegions();
        for(WarpRegion region : regions) {
            String warpName = region.getLinkedWarp().getName();
            Optional<Warp> optWarp = plugin.getWarpManager().getOne(warpName);
            if(!optWarp.isPresent()) {
                region.setValidity(Validity.INVALID);
            }
            
            Warp warp = optWarp.get();
            region.setLinkedWarp(warp);
            region.setValidity(Validity.VALID);
        }
        return regions;
    }

    @Override
    public void saveNew(WarpRegion region) {
        warpRegionDAO.insertWarpRegion(region);
    }

    @Override
    public void delete(WarpRegion region) {
        warpRegionDAO.deleteWarpRegion(region);

    }

    @Override
    public void update(WarpRegion region) {
        warpRegionDAO.updateWarpRegion(region);
    }

}
