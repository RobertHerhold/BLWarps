package com.blocklaunch.blwarps.managers;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Warp;
import com.blocklaunch.blwarps.sql.WarpDAO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.skife.jdbi.v2.DBI;
import org.spongepowered.api.service.sql.SqlService;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

public class SqlManager extends StorageManager {

    WarpDAO warpDAO;
    ObjectMapper mapper;

    public SqlManager(BLWarps plugin) {
        super(plugin);

        mapper = new ObjectMapper();

        SqlService sql = plugin.getGame().getServiceManager().provide(SqlService.class).get();

        DataSource dataSource = null;
        try {
            dataSource = sql.getDataSource(plugin.getConfig().getSqlConfig().getConnectionUrl());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DBI dbi = new DBI(dataSource);
        // Use on demand so we don't have to bother closing connections
        warpDAO = dbi.onDemand(WarpDAO.class);

        warpDAO.createWarpTable();
    }

    @Override
    public void loadWarps() {
        WarpManager.warps = warpDAO.getWarps();
    }

    @Override
    public void saveNewWarp(Warp warp) {
        warpDAO.insertWarp(warp, serializeGroupList(warp.getGroups()));
    }

    @Override
    public void deleteWarp(Warp warp) {
        warpDAO.deleteWarp(warp);
    }

    @Override
    public void updateWarp(Warp warp) {
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
