package com.blocklaunch.blwarps.region;

import com.flowpowered.math.vector.Vector3d;

public class WarpRegion {

    private String name;
    private String world;
    private Vector3d minLoc;
    private Vector3d maxLoc;

    public WarpRegion(String name, String world, Vector3d loc1, Vector3d loc2) {
        this.name = name;
        this.world = world;
        this.minLoc = new Vector3d(Math.min(loc1.getX(), loc2.getX()), Math.min(loc1.getY(), loc2.getY()), Math.min(loc1.getZ(), loc2.getZ()));
        this.maxLoc = new Vector3d(Math.max(loc1.getX(), loc2.getX()), Math.min(loc1.getY(), loc2.getY()), Math.min(loc1.getZ(), loc2.getZ()));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public Vector3d getMinLoc() {
        return minLoc;
    }

    public void setMinLoc(Vector3d minloc) {
        this.minLoc = minloc;
    }

    public Vector3d getMaxLoc() {
        return maxLoc;
    }

    public void setMaxLoc(Vector3d maxLoc) {
        this.maxLoc = maxLoc;
    }

}
