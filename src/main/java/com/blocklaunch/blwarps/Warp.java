package com.blocklaunch.blwarps;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.flowpowered.math.vector.Vector3d;

/**
 * Bean for representing a warp with name and location. Meant for easy (de)serialization with
 * Jackson
 *
 */
@JsonInclude(Include.NON_EMPTY)
public class Warp extends WarpBase {

    private Vector3d position; // no getter/setter for this, don't want it to be serialized

    private double x;
    private double y;
    private double z;
    private List<String> groups;

    // Empty constructor for Jackson
    public Warp() {

    }

    public Warp(String name, String world, Vector3d position) {
        super(name, world);
        this.position = position;

        DecimalFormat f = new DecimalFormat("##.00");

        this.x = Double.valueOf(f.format(position.getX()));
        this.y = Double.valueOf(f.format(position.getY()));
        this.z = Double.valueOf(f.format(position.getZ()));

    }

    /**
     * Checks if the location of two warps are the same.
     *
     * @param warp Warp to compare to
     * @return true if the two warps have the same location (x,y,z), false otherwise
     */
    public boolean locationIsSame(Warp warp) {
        if (this.world.equals(warp.getWorld()) && this.x == warp.getX() && this.y == warp.getY() && this.z == warp.getZ()) {
            return true;
        }
        return false;
    }

    /**
     * @return The Vector3d containing the warp's x, y, and z coordinates
     */
    @JsonIgnore
    public Vector3d getPosition() {
        if (position == null) {
            position = new Vector3d(this.x, this.y, this.z);
        }
        return position;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public List<String> getGroups() {
        if (groups == null) {
            this.groups = new ArrayList<>();
        }
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return "Warp [position=" + position + ", name=" + name + ", world=" + world + ", x=" + x + ", y=" + y + ", z=" + z + ", groups=" + groups
                + "]";
    }

}
