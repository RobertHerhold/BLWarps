package com.blocklaunch.blwarps.exceptions;

import org.spongepowered.api.world.Location;

public class MultipleWarpRegionsException extends Exception {

    public MultipleWarpRegionsException(Location location) {
        super("Multiple warp regions were found at: " + location.toString());
    }
}
