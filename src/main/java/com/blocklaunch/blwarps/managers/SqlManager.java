package com.blocklaunch.blwarps.managers;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.skife.jdbi.v2.DBI;
import org.spongepowered.api.service.sql.SqlService;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Warp;
import com.blocklaunch.blwarps.WarpBase;
import com.blocklaunch.blwarps.region.WarpRegion;
import com.blocklaunch.blwarps.sql.WarpBaseDAO;
import com.blocklaunch.blwarps.sql.WarpDAO;
import com.blocklaunch.blwarps.sql.WarpRegionDAO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SqlManager<T extends WarpBase> extends StorageManager<T> {

    private Class<T> type;
    WarpBaseDAO dao;
    ObjectMapper mapper;

    public SqlManager(Class<T> type, BLWarps plugin) {
        super(plugin);
        
        this.type = type;

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
        if(type == Warp.class) {
            dao = dbi.onDemand(WarpDAO.class);
        } else if(type == WarpRegion.class) {
            dao = dbi.onDemand(WarpRegionDAO.class);
        }
                
        dao.createTable();
    }

    @Override
    public void load() {
        plugin.getWarpBaseManager(type).setPayload(dao.getAll());
    }

    @Override
    public void saveNew(T object) {
        warpDAO.insertWarp(object, serializeGroupList(object.getGroups()));
    }

    @Override
    public void delete(T object) {
        warpDAO.deleteWarp(warp);
    }

    @Override
    public void update(T object) {
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
