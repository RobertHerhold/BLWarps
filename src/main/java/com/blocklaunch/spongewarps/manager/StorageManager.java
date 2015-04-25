package com.blocklaunch.spongewarps.manager;

import com.blocklaunch.spongewarps.SpongeWarps;
import com.blocklaunch.spongewarps.Warp;

public abstract class StorageManager {

	/**
	 * To be called when loading the warps from a storage solution like REST,
	 * MySQL, etc. fails
	 */
	public void failedLoadWarps() {
		SpongeWarps.fallbackManager.loadWarps();
	}

	/**
	 * To be called when saving the warps to a storage solution like REST,
	 * MySQL, etc. fails
	 */
	public void failedSaveNewWarp(Warp warp) {
		SpongeWarps.fallbackManager.saveNewWarp(warp);
	}

	/**
	 * To be called when deleting a warp with a storage solution like REST,
	 * MySQL, etc. fails
	 */
	public void failedDeleteWarp(Warp warp) {
		SpongeWarps.fallbackManager.deleteWarp(warp);
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
