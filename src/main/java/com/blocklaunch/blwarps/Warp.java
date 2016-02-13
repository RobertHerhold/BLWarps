package com.blocklaunch.blwarps;

import com.blocklaunch.blwarps.data.WarpDataQueries;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.data.MemoryDataContainer;

/**
 * Bean for representing a warp with name and location. Meant for easy
 * (de)serialization with Jackson
 *
 */
@JsonInclude(Include.NON_EMPTY)
public class Warp extends WarpBase implements DataSerializable {

    private Vector3d position; // serialize the double x,y,z rather than the
                               // Vector3d

    private double x;
    private double y;
    private double z;

    // Empty constructor for Jackson
    public Warp() {

    }

    public Warp(String owner, String name, String world, Vector3d position) {
        super(owner, name, world);
        this.position = position;

        this.x = formatDouble(position.getX());
        this.y = formatDouble(position.getY());
        this.z = formatDouble(position.getZ());

    }

    /**
     * Checks if the location of two warps are the same.
     *
     * @param warp Warp to compare to
     * @return true if the two warps have the same location (x,y,z), false
     *         otherwise
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
        if (this.position == null) {
            this.position = new Vector3d(this.x, this.y, this.z);
        }
        return this.position;
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return "Warp [position=" + this.position + ", name=" + this.name + ", world=" + this.world + ", x=" + this.x + ", y=" + this.y + ", z="
                + this.z + "]";
    }

    @JsonIgnore
    @Override
    public int getContentVersion() {
        return 1;
    }

    @JsonIgnore
    @Override
    public DataContainer toContainer() {
        return new MemoryDataContainer().set(WarpDataQueries.NAME, getName())
                .set(WarpDataQueries.WORLD, getWorld())
                .set(WarpDataQueries.X, getX())
                .set(WarpDataQueries.Y, getY())
                .set(WarpDataQueries.Z, getZ());
    }
}
