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
	 */
	abstract void loadWarps();

	/**
	 * Saves an individual newly added warp
	 * 
	 * @param warp
	 *            the warp to save
	 * 
	 */
	abstract void saveNewWarp(Warp warp);

	/**
	 * Deletes an individual warp
	 * 
	 * @param warp
	 *            the warp to delete
	 * 
	 */
	abstract void deleteWarp(Warp warp);

	/**
	 * Replaces a warp with the same name
	 * 
	 * @param warp
	 *            the new warp
	 */
	abstract void updateWarp(Warp warp);

}
