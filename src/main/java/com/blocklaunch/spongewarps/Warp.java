package com.blocklaunch.spongewarps;

/**
 * Bean for representing a warp with name and location. Meant for easy
 * (de)serialization with Jackson
 *
 */
public class Warp {

	private String name;
	private String world;
	private double x;
	private double y;
	private double z;

	public Warp() {
	}

	public Warp(String name, String world, double x, double y, double z) {
		super();
		this.name = name;
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Checks if the location of two warps are the same.
	 * 
	 * @param warp
	 *            warp to compare to
	 * @return true if the two warps have the same location (x,y,z), false
	 *         otherwise
	 */
	public boolean locationIsSame(Warp warp) {
		if (this.world.equals(warp.getWorld()) && this.x == warp.getX() && this.y == warp.getY()
				&& this.z == warp.getZ()) {
			return true;
		}
		return false;
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

	@Override
	public String toString() {
		return "Warp [name=" + name + ", world=" + world + ", x=" + x + ", y=" + y + ", z=" + z + "]";
	}

}