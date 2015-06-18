package com.blocklaunch.blwarps.managers.storage.sql.warpregion;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.managers.storage.StorageManager;
import com.blocklaunch.blwarps.managers.storage.sql.SqlManager;
import com.blocklaunch.blwarps.region.WarpRegion;

import java.util.List;

public class SqlWarpRegionManager extends SqlManager<WarpRegion> implements StorageManager<WarpRegion> {

    WarpRegionDAO warpRegionDAO;

    public SqlWarpRegionManager(BLWarps plugin) {
        super(plugin);

        // Use on demand so we don't have to bother closing connections
        this.warpRegionDAO = this.dbi.onDemand(WarpRegionDAO.class);

        this.warpRegionDAO.createWarpRegionTable();
    }

    @Override
    public List<WarpRegion> load() {
        return this.warpRegionDAO.getAllWarpRegions();
    }

    @Override
    public void saveNew(WarpRegion region) {
        this.warpRegionDAO.insertWarpRegion(region);
    }

    @Override
    public void delete(WarpRegion region) {
        this.warpRegionDAO.deleteWarpRegion(region);

    }

    @Override
    public void update(WarpRegion region) {
        this.warpRegionDAO.updateWarpRegion(region);
    }

}
