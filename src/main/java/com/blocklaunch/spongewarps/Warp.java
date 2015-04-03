package com.blocklaunch.spongewarps;

import java.text.NumberFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Bean for representing a warp with name and location. Meant for easy
 * (de)serialization with Jackson
 *
 */
public class Warp {
	
	@JsonIgnore
	private static final int TRUNCATE_DIGITS = 2;

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

		// Truncate all coordinates to 2 decimal places for better readability
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(TRUNCATE_DIGITS);
		
		this.x = Double.parseDouble(nf.format(x));
		this.y = Double.parseDouble(nf.format(y));
		this.z = Double.parseDouble(nf.format(z));
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
