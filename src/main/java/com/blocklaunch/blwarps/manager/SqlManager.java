package com.blocklaunch.blwarps.manager;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.skife.jdbi.v2.DBI;
import org.spongepowered.api.service.sql.SqlService;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Warp;
import com.blocklaunch.blwarps.sql.WarpDAO;

public class SqlManager extends StorageManager {

	WarpDAO warpDAO;

	public SqlManager(BLWarps plugin) {
		super(plugin);
		StringBuilder sb = new StringBuilder();
		sb.append("jdbc:");
		// We don't need to worry about the StorageType not being a SQL database
		// because this constructor will only be called if StorageType is SQL.
		sb.append(BLWarps.config.getSqlConfig().getSqlDatabase().toLowerCase());
		sb.append("://");
		sb.append(BLWarps.config.getSqlConfig().getSqlUsername());
		if (!BLWarps.config.getSqlConfig().getSqlPassword().isEmpty()) {
			sb.append(":");
			sb.append(BLWarps.config.getSqlConfig().getSqlPassword());
		}
		sb.append("@");
		sb.append(BLWarps.config.getSqlConfig().getSqlUrl());
		sb.append("/").append(BLWarps.config.getSqlConfig().getSqlDatabaseName());

		SqlService sql = BLWarps.game.getServiceManager().provide(SqlService.class).get();

		DataSource dataSource = null;
		try {
			dataSource = sql.getDataSource(sb.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		DBI dbi = new DBI(dataSource);
		// Use on demand so we don't have to bother closing connections
		warpDAO = dbi.onDemand(WarpDAO.class);

		warpDAO.createWarpTable();
	}

	@Override
	boolean loadWarps() {
		WarpManager.warps = warpDAO.getWarps();
		return true;
	}

	@Override
	boolean saveNewWarp(Warp warp) {
		warpDAO.insertWarp(warp);
		return true;
	}

	@Override
	boolean deleteWarp(Warp warp) {
		warpDAO.deleteWarp(warp);
		return true;
	}

}
