package com.blocklaunch.spongewarps.manager;

public interface StorageManager {

	/**
	 * Loads all saved warps
	 * 
	 * @return the success of the loading operation
	 */
	boolean loadWarps();
	
	/**
	 * Saves the currently loaded warps
	 * 
	 * @return the success of the saving operation
	 */
	boolean saveWarps();
	
}
