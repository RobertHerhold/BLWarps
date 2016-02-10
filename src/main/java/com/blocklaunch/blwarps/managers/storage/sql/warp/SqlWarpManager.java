package com.blocklaunch.blwarps.managers.storage.sql.warp;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Warp;
import com.blocklaunch.blwarps.managers.storage.StorageManager;
import com.blocklaunch.blwarps.managers.storage.sql.SqlManager;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class SqlWarpManager extends SqlManager<Warp> implements StorageManager<Warp> {

    WarpDAO warpDAO;
    ObjectMapper mapper;

    public SqlWarpManager(BLWarps plugin) {
        super(plugin);
        this.mapper = new ObjectMapper();

        // Use on demand so we don't have to bother closing connections
        this.warpDAO = this.dbi.onDemand(WarpDAO.class);

        this.warpDAO.createWarpTable();
    }

    @Override
    public List<Warp> load() {
        return this.warpDAO.getAllWarps();
    }

    @Override
    public void saveNew(Warp warp) {
        this.warpDAO.insertWarp(warp);
    }

    @Override
    public void delete(Warp warp) {
        this.warpDAO.deleteWarp(warp);
    }

    @Override
    public void update(Warp warp) {
        this.warpDAO.updateWarp(warp);
    }

}
