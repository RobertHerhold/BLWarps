package com.blocklaunch.blwarps.managers.storage.sql;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.WarpBase;
import org.skife.jdbi.v2.DBI;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.sql.SqlService;

import java.sql.SQLException;

import javax.sql.DataSource;

public abstract class SqlManager<T extends WarpBase> {

    public DBI dbi;
    public BLWarps plugin;

    public SqlManager(BLWarps plugin) {
        this.plugin = plugin;

        SqlService sql = Sponge.getServiceManager().provide(SqlService.class).get();

        DataSource dataSource = null;
        try {
            dataSource = sql.getDataSource(plugin.getConfig().getSqlConfig().getConnectionUrl());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.dbi = new DBI(dataSource);
    }

}
