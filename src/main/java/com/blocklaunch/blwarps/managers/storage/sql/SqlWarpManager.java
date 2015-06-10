package com.blocklaunch.blwarps.managers.storage.sql;

import java.util.List;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Warp;
import com.blocklaunch.blwarps.managers.storage.StorageManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SqlWarpManager extends SqlManager<Warp> implements StorageManager<Warp> {

    WarpDAO warpDAO;
    ObjectMapper mapper;

    public SqlWarpManager(BLWarps plugin) {
        super(plugin);
        mapper = new ObjectMapper();
        
        // Use on demand so we don't have to bother closing connections
        warpDAO = dbi.onDemand(WarpDAO.class);

        warpDAO.createWarpTable();
    }

    @Override
    public List<Warp> load() {
        return warpDAO.getAllWarps();
    }

    @Override
    public void saveNew(Warp warp) {
        warpDAO.insertWarp(warp, serializeGroupList(warp.getGroups()));
    }

    @Override
    public void delete(Warp warp) {
        warpDAO.deleteWarp(warp);
    }

    @Override
    public void update(Warp warp) {
        warpDAO.updateWarp(warp, serializeGroupList(warp.getGroups()));
    }

    private String serializeGroupList(List<String> groupList) {
        // Use Jackson to serialize List<String> because JDBI doesn't seem to be
        // able
        String groups = "[]";
        try {
            groups = mapper.writeValueAsString(groupList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return groups;
    }

}
