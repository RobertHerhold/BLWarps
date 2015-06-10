package com.blocklaunch.blwarps.managers.storage.sql.warpregion;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import com.blocklaunch.blwarps.Warp;
import com.blocklaunch.blwarps.managers.storage.sql.warp.WarpMapper;
import com.blocklaunch.blwarps.region.WarpRegion;

public interface WarpRegionDAO {
    
    static final String TABLE_NAME = "warp_regions";

    @SqlUpdate("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + "name VARCHAR(45) NOT NULL, "
            + "world VARCHAR(45) NOT NULL, "
            + "pos1 VARCHAR(45) NOT NULL, "
            + "pos2 VARCHAR(45) NOT NULL, "
            + "linked_warp VARCHAR(45) NOT NULL, "
            + "PRIMARY KEY (name))")
    void createTable();

    @SqlUpdate("INSERT INTO " + TABLE_NAME + " VALUES (:region.name, :region.world, :pos1, :pos2, :linkedWarp.name)")
    void insert(@BindBean("region") WarpRegion warpRegion, @Bind("pos1") String pos1, @Bind("pos2") String pos2, @Bind("warp") Warp linkedWarp);

    @SqlUpdate("DELETE FROM " + TABLE_NAME + " WHERE name=:name")
    void delete(@BindBean WarpRegion warp);

    @SqlUpdate("UPDATE "+ TABLE_NAME + " SET world=:region.world, pos1=:pos1, pos2=:pos2, linked_warp=:linkedWarp.name")
    @Mapper(WarpMapper.class)
    void update(@BindBean("region") WarpRegion region, @Bind("pos1") String pos1, @Bind("pos2") String pos2, @Bind("warp") Warp linkedWarp);

    @SqlQuery("SELECT * FROM " + TABLE_NAME)
    @Mapper(WarpMapper.class)
    List<WarpRegion> getAll();
}
