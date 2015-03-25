package com.blocklaunch.spongewarps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;

public class WarpManager {

	private static List<Warp> warps = new ArrayList<Warp>();

	private static final String WARP_NAME_EXISTS_MSG = "A warp with that name already exists!";
	private static final String WARP_LOCATION_EXISTS_MSG = "A warp at that location already exists!";
	private static final String ERROR_FILE_WRITE = "There was an error writing to the file!";
	private static final String ERROR_FILE_READ = "There was an error reading the warps file!";

	/**
	 * Adds a warp with the passed in name and location
	 * 
	 * @param warpName
	 *            the name of the warp
	 * @param warpLocation
	 *            the location of the warp
	 * @return An error if the warp already exists, Optional.absent() otherwise
	 */
	public static Optional<String> addWarp(Warp newWarp) {

		for (Warp warp : warps) {
			if (warp.getName().equalsIgnoreCase(newWarp.getName())) {
				// A warp with that name already exists
				return Optional.of(WARP_NAME_EXISTS_MSG);
			}
			if (warp.locationIsSame(newWarp)) {
				return Optional.of(WARP_LOCATION_EXISTS_MSG);
			}
		}

		warps.add(newWarp);

		// Save warps after putting a new one in rather than saving when server
		// shuts down to prevent loss of data if the server crashed
		saveWarps();

		// No errors, return an absent optional
		return Optional.absent();

	}

	/**
	 * Saves all currently loaded warps to the disk.
	 */
	private static void saveWarps() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			// Only creates the file if it doesn't already exist.
			SpongeWarps.warpsFile.createNewFile();
			mapper.writeValue(SpongeWarps.warpsFile, warps);
		} catch (IOException e) {
			SpongeWarps.logger.warn(ERROR_FILE_WRITE);
			e.printStackTrace();
		}
	}

	/**
	 * Reads in warps file, and serializes it to a List<Warp>
	 */
	public static void loadWarps() {
		if(!SpongeWarps.warpsFile.exists()){
			return;
		}
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			warps = mapper.readValue(SpongeWarps.warpsFile, new TypeReference<List<Warp>>() {
			});
		} catch (IOException e) {
			SpongeWarps.logger.warn(ERROR_FILE_READ);
			e.printStackTrace();
		}

	}

	/**
	 * Gets the warp with the given name
	 * 
	 * @param warpName
	 * @return the corresponding warp if it exists, Optional.absent() otherwise
	 */
	public static Optional<Warp> getWarp(String warpName) {
		for (Warp warp : warps) {
			if (warp.getName().equalsIgnoreCase(warpName)) {
				return Optional.of(warp);
			}
		}
		return Optional.absent();
	}

}
