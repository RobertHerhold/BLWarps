package com.blocklaunch.blwarps.sql;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.blocklaunch.blwarps.Warp;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowpowered.math.vector.Vector3d;

public class WarpMapper implements ResultSetMapper<Warp> {

    @Override
    public Warp map(int index, ResultSet r, StatementContext context) throws SQLException {
        // Use Jackson to de-serialize List<String> because JDBI doesn't seem to be able
        String groups = r.getString("groups");
        List<String> groupList = new ArrayList<>();
        try {
            groupList = new ObjectMapper().readValue(groups, new TypeReference<List<String>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }

        Warp warp = new Warp(r.getString("name"), r.getString("world"), new Vector3d(r.getInt("x"), r.getInt("y"), r.getInt("z")));
        warp.setGroups(groupList);

        return warp;
    }
}
