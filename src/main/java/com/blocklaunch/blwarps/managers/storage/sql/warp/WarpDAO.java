package com.blocklaunch.blwarps.managers.storage.sql.warp;

import com.blocklaunch.blwarps.Warp;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(WarpMapper.class)
public interface WarpDAO {

    @SqlUpdate("CREATE TABLE IF NOT EXISTS warps (name VARCHAR(45) NOT NULL, world VARCHAR(45) NOT NULL, x INT NOT NULL, y INT NOT NULL, z INT NOT NULL, PRIMARY KEY (name))")
            void createWarpTable();

    @SqlUpdate("INSERT INTO warps (name, world, x, y, z) VALUES (:warp.name, :warp.world, :warp.x, :warp.y, :warp.z)")
    void insertWarp(@BindBean("warp") Warp warp);

    @SqlUpdate("DELETE FROM warps WHERE name=:name")
    void deleteWarp(@BindBean Warp warp);

    @SqlUpdate("UPDATE warps SET world=:warp.world, x=:warp.x, y=:warp.y, z=:warp.z, WHERE name=:warp.name")
    void updateWarp(@BindBean("warp") Warp warp);

    @SqlQuery("SELECT * FROM warps")
    List<Warp> getAllWarps();
}
