package com.blocklaunch.blwarps.managers.storage.sql;

import java.util.List;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.managers.storage.StorageManager;
import com.blocklaunch.blwarps.region.WarpRegion;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SqlWarpRegionManager extends SqlManager<WarpRegion> implements StorageManager<WarpRegion> {

    WarpRegionDAO warpRegionDAO;

    public SqlWarpRegionManager(BLWarps plugin) {
        super(plugin);

        // Use on demand so we don't have to bother closing connections
        warpRegionDAO = dbi.onDemand(WarpRegionDAO.class);

        // TODO warpRegionDAO.createWarpRegionTable();
    }

    @Override
    public List<WarpRegion> load() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void saveNew(WarpRegion t) {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete(WarpRegion t) {
        // TODO Auto-generated method stub

    }

    @Override
    public void update(WarpRegion t) {
        // TODO Auto-generated method stub

    }

}
