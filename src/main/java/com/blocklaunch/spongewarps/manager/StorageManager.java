package com.blocklaunch.spongewarps.manager;

import com.blocklaunch.spongewarps.SpongeWarps;

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
	public void failedSaveWarps() {
		SpongeWarps.fallbackManager.saveWarps();
	}

	/**
	 * Loads all saved warps
	 * 
	 * @return the success of the loading operation
	 */
	abstract boolean loadWarps();

	/**
	 * Saves the currently loaded warps
	 * 
	 * @return the success of the saving operation
	 */
	abstract boolean saveWarps();

}
