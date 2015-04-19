package com.blocklaunch.spongewarps.manager;

import com.blocklaunch.spongewarps.SpongeWarps;

public abstract class StorageManager {
	
	public void failedLoadWarps(){
		SpongeWarps.fallbackManager.loadWarps();
	}
	
	public void failedSaveWarps(){
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
