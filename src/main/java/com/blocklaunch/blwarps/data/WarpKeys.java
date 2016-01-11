package com.blocklaunch.blwarps.data;

import com.blocklaunch.blwarps.Warp;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.KeyFactory;
import org.spongepowered.api.data.value.mutable.Value;

public class WarpKeys {

    public static final Key<Value<Warp>> WARP = KeyFactory.makeSingleKey(Warp.class, Value.class, DataQuery.of("WARP"));

}
