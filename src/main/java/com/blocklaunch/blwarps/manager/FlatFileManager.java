package com.blocklaunch.blwarps.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Warp;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Optional;

public class FlatFileManager extends StorageManager {

	private ObjectMapper mapper = new ObjectMapper();

	private static final String ERROR_FILE_WRITE = "There was an error writing to the file!";
	private static final String ERROR_FILE_READ = "There was an error reading the warps file!";
	
	public FlatFileManager(BLWarps plugin) {
		super(plugin);
	}

	/**
	 * Reads in warps file, and de-serializes it to a List<Warp>
	 */
	@Override
	public boolean loadWarps() {
		Optional<List<Warp>> optWarps = readInWarps();
		if (!optWarps.isPresent()) {
			return false;
		}

		WarpManager.warps = optWarps.get();
		return true;
	}

	/**
	 * Read in warp file and deserialize to List<Warp> Insert new warp to List
	 * Serialize to JSON and write to file
	 * 
	 * @param warp
	 *            the new warp to save
	 * @return the success of the saving operation
	 */
	@Override
	boolean saveNewWarp(Warp warp) {
		Optional<List<Warp>> warpsOpt = readInWarps();

		List<Warp> currentlySavedWarps = new ArrayList<Warp>();
		if (warpsOpt.isPresent()) {
			currentlySavedWarps = warpsOpt.get();
		}
		currentlySavedWarps.add(warp);

		return writeOutWarps(currentlySavedWarps);
	}

	/**
	 * Read in warp file and deserialize to List<Warp>. Remove the warp from the
	 * list Serialize to JSON and write to file
	 * 
	 * @param warp
	 *            the warp to remove
	 * @return the success of the deletion operation
	 */
	@Override
	boolean deleteWarp(Warp warp) {
		Optional<List<Warp>> warpsOpt = readInWarps();
		List<Warp> warps;
		if (warpsOpt.isPresent()) {
			warps = warpsOpt.get();
		} else {
			return false;
		}

		// Temporary warp for avoiding ConcurrentModificationException
		Warp warpToRemove = null;		
		for (Warp w : warps) {
			if (w.getName().equalsIgnoreCase(warp.getName())) {
				warpToRemove = w;
			}
		}
		if (warpToRemove != null)
			warps.remove(warpToRemove);

		return writeOutWarps(warps);
	}

	/**
	 * Read in warp file (if it exists) and serialize to a List<Warp> (if
	 * possible)
	 * 
	 * @return an Optional containing the List<Warp>, or Optional.absent()
	 *         otherwise
	 */
	private Optional<List<Warp>> readInWarps() {
		if (!BLWarps.warpsFile.exists()) {
			return Optional.absent();
		}

		try {
			List<Warp> warps = mapper.readValue(BLWarps.warpsFile, new TypeReference<List<Warp>>() {
			});
			return Optional.of(warps);
		} catch (IOException e) {
			plugin.getLogger().warn(ERROR_FILE_READ);
			e.printStackTrace();
			return Optional.absent();
		}

	}

	/**
	 * Serializes a List<Warp> and saves it to the file.
	 * 
	 * @param warps
	 *            the warps to save to the file
	 * @return the success of the saving operation
	 */
	private boolean writeOutWarps(List<Warp> warps) {
		try {
			// Only creates the file if it doesn't already exist.
			BLWarps.warpsFile.createNewFile();
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			mapper.writeValue(BLWarps.warpsFile, warps);
			return true;
		} catch (IOException e) {
			plugin.getLogger().warn(ERROR_FILE_WRITE);
			e.printStackTrace();
			return false;
		}
	}

}
