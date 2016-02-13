package com.blocklaunch.blwarps.managers.storage.sql.warpregion;

import com.blocklaunch.blwarps.region.WarpRegion;
import com.flowpowered.math.vector.Vector3d;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WarpRegionMapper implements ResultSetMapper<WarpRegion> {

    @Override
    public WarpRegion map(int index, ResultSet r, StatementContext context) throws SQLException {
        Vector3d loc1 = new Vector3d(r.getInt("loc1x"), r.getInt("loc1y"), r.getInt("loc1z"));
        Vector3d loc2 = new Vector3d(r.getInt("loc2x"), r.getInt("loc2y"), r.getInt("loc2z"));

        WarpRegion region = new WarpRegion(r.getString("owner"), r.getString("name"), r.getString("linkedWarp"), r.getString("world"), loc1, loc2);

        return region;
    }

}
