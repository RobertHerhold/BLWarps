package com.blocklaunch.spongewarps;

public class Settings {

	/**
	 * Time, in seconds, between initiating a warp and teleporting the player
	 */
	public static int warpDelay = 5;

	/**
	 * Whether or not to cancel a player's warp if they move or get hurt
	 */
	public static boolean pvpProtect = false;
	
	/**
	 * The storage solution to store warps in
	 */
	public static StorageType storageType = StorageType.FLATFILE;
}
