package com.blocklaunch.blwarps.exceptions;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

@SuppressWarnings("serial")
public class MultipleWarpRegionsException extends Exception {

    public MultipleWarpRegionsException(Location<World> location) {
        super("Multiple warp regions were found at: " + location.toString());
    }
}
