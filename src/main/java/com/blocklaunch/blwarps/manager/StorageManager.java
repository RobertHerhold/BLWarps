package com.blocklaunch.blwarps.manager;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Warp;

public abstract class StorageManager {
	
	protected BLWarps plugin;
	
	public StorageManager(BLWarps plugin) {
		this.plugin = plugin;
	}

	/**
	 * To be called when loading the warps from a storage solution like REST,
	 * MySQL, etc. fails
	 */
	public void failedLoadWarps() {
		plugin.getFallBackManager().loadWarps();
	}

	/**
	 * To be called when saving the warps to a storage solution like REST,
	 * MySQL, etc. fails
	 */
	public void failedSaveNewWarp(Warp warp) {
		plugin.getFallBackManager().saveNewWarp(warp);
	}

	/**
	 * To be called when deleting a warp with a storage solution like REST,
	 * MySQL, etc. fails
	 */
	public void failedDeleteWarp(Warp warp) {
		plugin.getFallBackManager().deleteWarp(warp);
	}

	/**
	 * Loads all saved warps
	 * 
	 * @return the success of the loading operation
	 */
	abstract boolean loadWarps();

	/**
	 * Saves an individual newly added warp
	 * 
	 * @return the success of the saving operation
	 */
	abstract boolean saveNewWarp(Warp warp);

	/**
	 * Deletes an individual warp
	 * 
	 * @return the success of the deletion operation
	 */
	abstract boolean deleteWarp(Warp warp);

}
