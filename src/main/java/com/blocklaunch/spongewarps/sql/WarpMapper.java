package com.blocklaunch.spongewarps.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.blocklaunch.spongewarps.Warp;

public class WarpMapper implements ResultSetMapper<Warp> {

	@Override
	public Warp map(int index, ResultSet r, StatementContext context) throws SQLException {
		return new Warp(r.getString("name"), r.getString("world"), r.getInt("x"), r.getInt("y"), r.getInt("z"));
	}
}
