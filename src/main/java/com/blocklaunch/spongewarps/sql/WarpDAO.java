package com.blocklaunch.spongewarps.sql;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import com.blocklaunch.spongewarps.Warp;

public interface WarpDAO {

	@SqlUpdate("CREATE TABLE IF NOT EXISTS warps (name VARCHAR(45) NOT NULL, world VARCHAR(45) NOT NULL, x INT NOT NULL, y INT NOT NULL, z INT NOT NULL, PRIMARY KEY (name))")
	void createWarpTable();

	@SqlUpdate("INSERT INTO warps (name, world, x, y, z) VALUES (:name, :world, :x, :y, :z)")
	void insertWarp(@BindBean Warp warp);

	@SqlUpdate("DELETE FROM warps WHERE name=:name")
	void deleteWarp(@BindBean Warp warp);

	@SqlQuery("SELECT * FROM warps")
	@Mapper(WarpMapper.class)
	List<Warp> getWarps();
}
