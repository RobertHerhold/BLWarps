package com.blocklaunch.blwarps.managers.storage.sql;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.skife.jdbi.v2.DBI;
import org.spongepowered.api.service.sql.SqlService;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.WarpBase;

public abstract class SqlManager<T extends WarpBase> {
    
    public DBI dbi;
    public BLWarps plugin;
    
    public SqlManager(BLWarps plugin) {
        this.plugin = plugin;
        
        SqlService sql = plugin.getGame().getServiceManager().provide(SqlService.class).get();

        DataSource dataSource = null;
        try {
            dataSource = sql.getDataSource(plugin.getConfig().getSqlConfig().getConnectionUrl());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dbi = new DBI(dataSource);
    }

}
