package com.blocklaunch.spongewarps.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.service.scheduler.Task;

import com.blocklaunch.spongewarps.Settings;
import com.blocklaunch.spongewarps.SpongeWarps;
import com.blocklaunch.spongewarps.Warp;
import com.blocklaunch.spongewarps.runnable.WarpPlayerRunnable;
import com.google.common.base.Optional;

public class WarpManager {

	public static List<Warp> warps = new ArrayList<Warp>();
	private static Map<Player, Task> warpsInProgress = new HashMap<Player, Task>();

	private static final int TICKS_PER_SECOND = 20;

	private static final String WARP_NAME_EXISTS_MSG = "A warp with that name already exists!";
	private static final String WARP_LOCATION_EXISTS_MSG = "A warp at that location already exists!";
	private static final String WARP_NOT_EXIST = "That warp does not exist!";
	private static final String ERROR_SCHEDULING_WARP_MSG = "There was an error scheduling your warp. Please try again.";

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
	 * Saves the currently loaded warps
	 */
	private static void saveWarps() {
		SpongeWarps.storageManager.saveWarps();
	}

	/**
	 * Loads all saved warps
	 */
	public static void loadWarps() {
		SpongeWarps.storageManager.loadWarps();
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

	/**
	 * Deletes the warp with the provided name
	 * 
	 * @param warpName
	 * @return
	 */
	public static Optional<String> deleteWarp(String warpName) {
		for (Warp warp : warps) {
			if (warp.getName().equalsIgnoreCase(warpName)) {
				warps.remove(warp);
				saveWarps();
				return Optional.absent();
			}
		}
		return Optional.of(WARP_NOT_EXIST);
	}

	/**
	 * Schedules a task to warp the specified player to the specified warp
	 * 
	 * @param player
	 *            the player to be warped
	 * @param warp
	 *            the location the player is to be warped to
	 * @return An error if one exists, or Optional.absent() otherwise
	 */
	public static Optional<String> scheduleWarp(Player player, Warp warp) {
		long delay = Settings.warpDelay * TICKS_PER_SECOND;

		// Schedule the task
		Optional<Task> optTask = SpongeWarps.scheduler.runTaskAfter(SpongeWarps.plugin, new WarpPlayerRunnable(player,
				warp), delay);

		if (!optTask.isPresent()) {
			// There was an error scheduling the warp
			return Optional.of(ERROR_SCHEDULING_WARP_MSG);
		}

		addWarpInProgress(player, optTask.get());

		return Optional.absent();
	}

	/**
	 * Removes the specified player from the map of players who are waiting to
	 * be warped
	 * 
	 * @param player
	 *            the player that has been teleported
	 */
	public static void warpCompleted(Player player) {
		warpsInProgress.remove(player);
	}

	/**
	 * Checks if the passed in player currently has a warp request
	 * 
	 * @param player
	 *            the player to check
	 * @return true if they are about to be warped, false if not.
	 */
	public static boolean isWarping(Player player) {
		return warpsInProgress.containsKey(player);
	}

	/**
	 * Adds the player to the map of in-progress warps
	 * 
	 * @param player
	 *            the player to be warped
	 * @param task
	 *            the reference to the task to warp the player
	 */
	public static void addWarpInProgress(Player player, Task task) {
		warpsInProgress.put(player, task);
	}

}
