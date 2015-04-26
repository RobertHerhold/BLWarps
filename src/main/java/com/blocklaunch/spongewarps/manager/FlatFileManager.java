package com.blocklaunch.spongewarps.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.blocklaunch.spongewarps.SpongeWarps;
import com.blocklaunch.spongewarps.Warp;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class FlatFileManager extends StorageManager {

	private static final String ERROR_FILE_WRITE = "There was an error writing to the file!";
	private static final String ERROR_FILE_READ = "There was an error reading the warps file!";

	/**
	 * Reads in warps file, and de-serializes it to a List<Warp>
	 */
	@Override
	public boolean loadWarps() {
		if (!SpongeWarps.warpsFile.exists()) {
			return false;
		}

		ObjectMapper mapper = new ObjectMapper();
		try {
			WarpManager.warps = mapper.readValue(SpongeWarps.warpsFile, new TypeReference<List<Warp>>() {
			});
			return true;
		} catch (IOException e) {
			SpongeWarps.logger.warn(ERROR_FILE_READ);
			e.printStackTrace();
			return false;
		}
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
		List<Warp> currentlySavedWarps = new ArrayList<Warp>();
		ObjectMapper mapper = new ObjectMapper();

		if (SpongeWarps.warpsFile.exists()) {
			try {
				currentlySavedWarps = mapper.readValue(SpongeWarps.warpsFile, new TypeReference<List<Warp>>() {
				});
			} catch (IOException e) {
				SpongeWarps.logger.warn(ERROR_FILE_READ);
				e.printStackTrace();
				return false;
			}
		}

		currentlySavedWarps.add(warp);

		try {
			// Only creates the file if it doesn't already exist.
			SpongeWarps.warpsFile.createNewFile();
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			mapper.writeValue(SpongeWarps.warpsFile, currentlySavedWarps);
			return true;
		} catch (IOException e) {
			SpongeWarps.logger.warn(ERROR_FILE_WRITE);
			e.printStackTrace();
			return false;
		}

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
		if (!SpongeWarps.warpsFile.exists()) {
			return false;
		}

		ObjectMapper mapper = new ObjectMapper();
		List<Warp> currentlySavedWarps;
		try {
			currentlySavedWarps = mapper.readValue(SpongeWarps.warpsFile, new TypeReference<List<Warp>>() {
			});
		} catch (IOException e) {
			SpongeWarps.logger.warn(ERROR_FILE_READ);
			e.printStackTrace();
			return false;
		}

		currentlySavedWarps.remove(warp);

		try {
			// Only creates the file if it doesn't already exist.
			SpongeWarps.warpsFile.createNewFile();
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			mapper.writeValue(SpongeWarps.warpsFile, currentlySavedWarps);
			return true;
		} catch (IOException e) {
			SpongeWarps.logger.warn(ERROR_FILE_WRITE);
			e.printStackTrace();
			return false;
		}
	}

}
