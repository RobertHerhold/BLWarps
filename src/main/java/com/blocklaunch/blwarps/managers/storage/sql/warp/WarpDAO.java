package com.blocklaunch.blwarps.managers.storage.sql.warp;

import com.blocklaunch.blwarps.Warp;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(WarpMapper.class)
public interface WarpDAO {

    @SqlUpdate("CREATE TABLE IF NOT EXISTS warps (id VARCHAR(45) NOT NULL, name VARCHAR(45) NOT NULL, world VARCHAR(45) NOT NULL, x INT NOT NULL, y INT NOT NULL, z INT NOT NULL, PRIMARY KEY (id))")
            void createWarpTable();

    @SqlUpdate("INSERT INTO warps (id, name, world, x, y, z) VALUES (:warp.id, :warp.name, :warp.world, :warp.x, :warp.y, :warp.z)")
    void insertWarp(@BindBean("warp") Warp warp);

    @SqlUpdate("DELETE FROM warps WHERE id=:warp.id")
    void deleteWarp(@BindBean("warp") Warp warp);

    @SqlUpdate("UPDATE warps SET world=:warp.world, x=:warp.x, y=:warp.y, z=:warp.z, WHERE id=:warp.id")
    void updateWarp(@BindBean("warp") Warp warp);

    @SqlQuery("SELECT * FROM warps")
    List<Warp> getAllWarps();
}
