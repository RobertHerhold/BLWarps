package com.blocklaunch.spongewarps.manager;

import java.io.IOException;
import java.util.List;

import com.blocklaunch.spongewarps.SpongeWarps;
import com.blocklaunch.spongewarps.Warp;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class FlatFileManager implements StorageManager {

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
	 * Serializes all currently loaded warps, and saves to the disk
	 */
	@Override
	public boolean saveWarps() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			// Only creates the file if it doesn't already exist.
			SpongeWarps.warpsFile.createNewFile();
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			mapper.writeValue(SpongeWarps.warpsFile, WarpManager.warps);
			return true;
		} catch (IOException e) {
			SpongeWarps.logger.warn(ERROR_FILE_WRITE);
			e.printStackTrace();
			return false;
		}
	}

}
