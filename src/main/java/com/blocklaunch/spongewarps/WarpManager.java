package com.blocklaunch.spongewarps;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;

public class WarpManager {

	private static List<Warp> warps = new ArrayList<Warp>();

	private static final String WARP_NAME_EXISTS_MSG = "A warp with that name already exists!";
	private static final String WARP_LOCATION_EXISTS_MSG = "A warp at that location already exists!";
	private static final String ERROR_FILE_WRITE = "There was an error writing to the file!";

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
		File warpsFile = new File(SpongeWarps.configFolder, "warps.json");
		ObjectMapper mapper = new ObjectMapper();
		try {
			// Only creates the file if it doesn't already exist.
			warpsFile.createNewFile();
			mapper.writeValue(warpsFile, warps);
		} catch (IOException e) {
			SpongeWarps.logger.warn(ERROR_FILE_WRITE);
			e.printStackTrace();
		}
	}

}
