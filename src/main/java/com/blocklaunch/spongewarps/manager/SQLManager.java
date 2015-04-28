package com.blocklaunch.spongewarps.manager;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.skife.jdbi.v2.DBI;
import org.spongepowered.api.service.sql.SqlService;

import com.blocklaunch.spongewarps.Settings;
import com.blocklaunch.spongewarps.SpongeWarps;
import com.blocklaunch.spongewarps.Warp;
import com.blocklaunch.spongewarps.sql.WarpDAO;

public class SQLManager extends StorageManager {

	WarpDAO warpDAO;

	public SQLManager() {
		StringBuilder sb = new StringBuilder();
		sb.append("jdbc:");
		// We don't need to worry about the StorageType not being a SQL database
		// because this constructor will only be called if StorageType is SQL.
		sb.append(Settings.SQLDatabase.toLowerCase());
		sb.append("://");
		sb.append(Settings.SQLUsername);
		if (!Settings.SQLPassword.isEmpty()) {
			sb.append(":");
			sb.append(Settings.SQLPassword);
		}
		sb.append("@");
		sb.append(Settings.SQLURL);
		sb.append("/").append(Settings.SQLDatabaseName);

		SqlService sql = SpongeWarps.game.getServiceManager().provide(SqlService.class).get();

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
