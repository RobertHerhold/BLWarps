package com.blocklaunch.blwarps.region;

import com.blocklaunch.blwarps.Warp;
import com.blocklaunch.blwarps.WarpBase;
import com.flowpowered.math.vector.Vector3d;

public class WarpRegion extends WarpBase {

    private Warp linkedWarp;
    private Vector3d minLoc;
    private Vector3d maxLoc;

    // Empty constructor for Jackson
    public WarpRegion() {

    }

    public WarpRegion(Warp linkedWarp, String name, String world, Vector3d loc1, Vector3d loc2) {
        super(name, world);
        this.linkedWarp = linkedWarp;
        this.minLoc = new Vector3d(Math.min(loc1.getX(), loc2.getX()), Math.min(loc1.getY(), loc2.getY()), Math.min(loc1.getZ(), loc2.getZ()));
        this.maxLoc = new Vector3d(Math.max(loc1.getX(), loc2.getX()), Math.min(loc1.getY(), loc2.getY()), Math.min(loc1.getZ(), loc2.getZ()));
    }

    /**
     * @param warpRegion the warp region to test collision with
     * @return true if the regions are colliding, false if not
     */
    public boolean collidesWith(WarpRegion warpRegion) {
        // These two regions are Axis Aligned Bounding Boxes (AABBs)
        // They are colliding if the first box's max point is greater than the second one's min
        // point and that the first one's min point is less than the second one's max point.
        // It does not matter which box is "the first"

        return (this.maxLoc.getX() > warpRegion.getMinLoc().getX() && this.minLoc.getX() < warpRegion.getMaxLoc().getX()
                && this.maxLoc.getY() > warpRegion.getMinLoc().getY() && this.minLoc.getY() < warpRegion.getMaxLoc().getY()
                && this.maxLoc.getZ() > warpRegion.getMinLoc().getZ() && this.minLoc.getZ() < warpRegion.getMaxLoc().getZ());

    }

    public Warp getLinkedWarp() {
        return linkedWarp;
    }

    public void setLinkedWarp(Warp linkedWarp) {
        this.linkedWarp = linkedWarp;
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
