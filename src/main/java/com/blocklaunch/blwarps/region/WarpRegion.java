package com.blocklaunch.blwarps.region;

import java.text.DecimalFormat;

import com.blocklaunch.blwarps.Validity;
import com.blocklaunch.blwarps.Warp;
import com.blocklaunch.blwarps.WarpBase;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flowpowered.math.vector.Vector3d;

public class WarpRegion extends WarpBase {

    private Warp linkedWarp;
    private double loc1x;
    private double loc1y;
    private double loc1z;
    private double loc2x;
    private double loc2y;
    private double loc2z;

    private Vector3d minLoc;
    private Vector3d maxLoc;

    // The validity of the region
    // UNKNOWN if the linkedWarp is not know to exist
    // VALID if the linkedWarp has been checked and does exist
    // INVALID if the linkedWarp does not exist
    private Validity validity;

    // Empty constructor for Jackson
    public WarpRegion() {

    }

    public WarpRegion(Warp linkedWarp, String name, String world, Vector3d loc1, Vector3d loc2) {
        super(name, world);
        this.linkedWarp = linkedWarp;
        this.minLoc = new Vector3d(Math.min(loc1.getX(), loc2.getX()), Math.min(loc1.getY(), loc2.getY()), Math.min(loc1.getZ(), loc2.getZ()));
        this.maxLoc = new Vector3d(Math.max(loc1.getX(), loc2.getX()), Math.min(loc1.getY(), loc2.getY()), Math.min(loc1.getZ(), loc2.getZ()));

        this.loc1x = formatDouble(minLoc.getX());
        this.loc1y = formatDouble(minLoc.getY());
        this.loc1z = formatDouble(minLoc.getZ());
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

    private double formatDouble(double d) {
        DecimalFormat f = new DecimalFormat("##.00");
        return Double.valueOf(f.format(d));
    }

    public Warp getLinkedWarp() {
        return linkedWarp;
    }

    public void setLinkedWarp(Warp linkedWarp) {
        this.linkedWarp = linkedWarp;
    }

    public double getLoc1x() {
        return loc1x;
    }

    public void setLoc1x(double loc1x) {
        this.loc1x = loc1x;
    }

    public double getLoc1y() {
        return loc1y;
    }

    public void setLoc1y(double loc1y) {
        this.loc1y = loc1y;
    }

    public double getLoc1z() {
        return loc1z;
    }

    public void setLoc1z(double loc1z) {
        this.loc1z = loc1z;
    }

    public double getLoc2x() {
        return loc2x;
    }

    public void setLoc2x(double loc2x) {
        this.loc2x = loc2x;
    }

    public double getLoc2y() {
        return loc2y;
    }

    public void setLoc2y(double loc2y) {
        this.loc2y = loc2y;
    }

    public double getLoc2z() {
        return loc2z;
    }

    public void setLoc2z(double loc2z) {
        this.loc2z = loc2z;
    }

    @JsonIgnore
    public Validity getValidity() {
        return validity;
    }

    @JsonIgnore
    public void setValidity(Validity validity) {
        this.validity = validity;
    }

    @JsonIgnore
    public Vector3d getMinLoc() {
        return minLoc;
    }

    @JsonIgnore
    public Vector3d getMaxLoc() {
        return maxLoc;
    }

}
