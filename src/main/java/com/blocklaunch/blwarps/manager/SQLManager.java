package com.blocklaunch.blwarps.manager;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.skife.jdbi.v2.DBI;
import org.spongepowered.api.service.sql.SqlService;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Warp;
import com.blocklaunch.blwarps.sql.WarpDAO;

public class SQLManager extends StorageManager {

	WarpDAO warpDAO;

	public SQLManager() {
		StringBuilder sb = new StringBuilder();
		sb.append("jdbc:");
		// We don't need to worry about the StorageType not being a SQL database
		// because this constructor will only be called if StorageType is SQL.
		sb.append(BLWarps.config.getSQLConfig().getSQLDatabase().toLowerCase());
		sb.append("://");
		sb.append(BLWarps.config.getSQLConfig().getSQLUsername());
		if (!BLWarps.config.getSQLConfig().getSQLPassword().isEmpty()) {
			sb.append(":");
			sb.append(BLWarps.config.getSQLConfig().getSQLPassword());
		}
		sb.append("@");
		sb.append(BLWarps.config.getSQLConfig().getSQLURL());
		sb.append("/").append(BLWarps.config.getSQLConfig().getSQLDatabaseName());

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
