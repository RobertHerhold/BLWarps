package com.blocklaunch.blwarps.managers.storage.sql.warpregion;

import com.blocklaunch.blwarps.region.WarpRegion;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(WarpRegionMapper.class)
public interface WarpRegionDAO {

    static final String TABLE_NAME = "warp_regions";

    @SqlUpdate("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + "id VARCHAR(45) NOT NULL, "
            + "name VARCHAR(45) NOT NULL, "
            + "world VARCHAR(45) NOT NULL, "
            + "linked_warp VARCHAR(45) NOT NULL, "
            + "loc1x INT NOT NULL, "
            + "loc1y INT NOT NULL, "
            + "loc1z INT NOT NULL, "
            + "loc2x INT NOT NULL, "
            + "loc2y INT NOT NULL, "
            + "loc2z INT NOT NULL, "
            + "PRIMARY KEY (id))")
    void createWarpRegionTable();

    @SqlUpdate("INSERT INTO " + TABLE_NAME + " VALUES (:region.id, :region.name, :region.linkedWarp, :region.world, "
            + ":region.loc1x, :region.loc1y, :region.loc1z, "
            + ":region.loc2x, :region.loc2y, :region.loc2z)")
    void insertWarpRegion(@BindBean("region") WarpRegion warpRegion);

    @SqlUpdate("DELETE FROM " + TABLE_NAME + " WHERE id=:id")
    void deleteWarpRegion(@BindBean WarpRegion warp);

    @SqlUpdate("UPDATE " + TABLE_NAME + " SET world=:region.world, linkedWarp=:region.linkedWarp, "
            + "loc1x=:region.loc1x, loc1y=:region.loc1y, loc1z=:region.loc1z, "
            + "loc2x=:region.loc2x, loc2y=:region.loc2y, loc2z=:region.loc2z "
            + "WHERE id=:region.id")
    void updateWarpRegion(@BindBean("region") WarpRegion region);

    @SqlQuery("SELECT * FROM " + TABLE_NAME)
    List<WarpRegion> getAllWarpRegions();
}
