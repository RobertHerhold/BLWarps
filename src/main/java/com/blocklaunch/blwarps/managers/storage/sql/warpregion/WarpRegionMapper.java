package com.blocklaunch.blwarps.managers.storage.sql.warpregion;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.blocklaunch.blwarps.Validity;
import com.blocklaunch.blwarps.Warp;
import com.blocklaunch.blwarps.region.WarpRegion;
import com.flowpowered.math.vector.Vector3d;

public class WarpRegionMapper implements ResultSetMapper<WarpRegion> {

    @Override
    public WarpRegion map(int index, ResultSet r, StatementContext context) throws SQLException {
        Vector3d loc1 = new Vector3d(r.getInt("loc1x"), r.getInt("loc1y"), r.getInt("loc1z"));
        Vector3d loc2 = new Vector3d(r.getInt("loc2x"), r.getInt("loc2y"), r.getInt("loc2z"));
        
        Warp linkedWarp = new Warp();
        linkedWarp.setName(r.getString("linkedWarp"));
        
        WarpRegion region = new WarpRegion(linkedWarp, r.getString("name"), r.getString("world"), loc1, loc2);
        region.setValidity(Validity.UNKNOWN);
        
        return region;
    }

}
