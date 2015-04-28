package com.blocklaunch.spongewarps.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.blocklaunch.spongewarps.SpongeWarps;
import com.blocklaunch.spongewarps.Warp;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Optional;

public class FlatFileManager extends StorageManager {

	private ObjectMapper mapper = new ObjectMapper();

	private static final String ERROR_FILE_WRITE = "There was an error writing to the file!";
	private static final String ERROR_FILE_READ = "There was an error reading the warps file!";

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

		for (Warp w : warps) {
			if (w.getName().equalsIgnoreCase(warp.getName())) {
				warps.remove(w);
			}
		}

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
		if (!SpongeWarps.warpsFile.exists()) {
			return Optional.absent();
		}

		try {
			List<Warp> warps = mapper.readValue(SpongeWarps.warpsFile, new TypeReference<List<Warp>>() {
			});
			return Optional.of(warps);
		} catch (IOException e) {
			SpongeWarps.logger.warn(ERROR_FILE_READ);
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
			SpongeWarps.warpsFile.createNewFile();
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			mapper.writeValue(SpongeWarps.warpsFile, warps);
			return true;
		} catch (IOException e) {
			SpongeWarps.logger.warn(ERROR_FILE_WRITE);
			e.printStackTrace();
			return false;
		}
	}

}
