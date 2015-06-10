package com.blocklaunch.blwarps.managers.storage.sql.warpregion;

import java.util.List;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.managers.storage.StorageManager;
import com.blocklaunch.blwarps.managers.storage.sql.SqlManager;
import com.blocklaunch.blwarps.region.WarpRegion;

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
        return warpRegionDAO.getAllWarpRegions();
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
