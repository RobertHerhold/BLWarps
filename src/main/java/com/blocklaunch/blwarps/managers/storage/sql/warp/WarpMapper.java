package com.blocklaunch.blwarps.managers.storage.sql.warp;

import com.blocklaunch.blwarps.Warp;
import com.flowpowered.math.vector.Vector3d;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WarpMapper implements ResultSetMapper<Warp> {

    @Override
    public Warp map(int index, ResultSet r, StatementContext context) throws SQLException {
        Warp warp = new Warp(r.getString("name"), r.getString("world"), new Vector3d(r.getInt("x"), r.getInt("y"), r.getInt("z")));
        return warp;
    }
}
