package com.blocklaunch.blwarps.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.service.scheduler.Task;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Constants;
import com.blocklaunch.blwarps.Warp;
import com.blocklaunch.blwarps.managers.storage.StorageManager;
import com.blocklaunch.blwarps.runnables.WarpPlayerRunnable;
import com.google.common.base.Optional;

public class WarpManager extends WarpBaseManager<Warp> {

    private Map<Player, Task> warpsInProgress = new HashMap<Player, Task>();

    private Validator validator;

    public WarpManager(BLWarps plugin, StorageManager<Warp> storage) {
        super(plugin, storage);
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * Adds a warp with the passed in name and location
     * 
     * @param warpName The name of the warp
     * @param warpLocation The location of the warp
     * @return An error if the warp already exists, Optional.absent() otherwise
     */
    @Override
    public Optional<String> addNew(Warp newWarp) {

        for (Warp warp : payload) {
            if (warp.getName().equalsIgnoreCase(newWarp.getName())) {
                // A warp with that name already exists
                return Optional.of(Constants.WARP_NAME_EXISTS);
            }
            if (warp.locationIsSame(newWarp)) {
                return Optional.of(Constants.WARP_LOCATION_EXISTS);
            }
        }
        Set<ConstraintViolation<Warp>> violations = validator.validate(newWarp);
        if (!violations.isEmpty()) {
            return Optional.of(violations.iterator().next().getMessage());
        }

        payload.add(newWarp);
        names.add(newWarp.getName());

        // Save warps after putting a new one in rather than saving when server
        // shuts down to prevent loss of data if the server crashed
        storage.saveNew(newWarp);

        // No errors, return an absent optional
        return Optional.absent();

    }

    /**
     * Adds the warp to the specified group (puts the group's name in the list of the warp's groups)
     * 
     * @param warp The warp to be added to the group
     * @param group The group to add the warp to
     */
    public void addWarpToGroup(Warp warp, String group) {
        if (warp.getGroups().contains(group)) {
            return;
        }
        warp.getGroups().add(group);
        storage.update(warp);
    }

    /**
     * Removes the warp from the specified group (removes the group's name from the list of the
     * warp's groups)
     * 
     * @param warp The warp to be removed from the group
     * @param group The group to remove the warp from
     */
    public void removeWarpFromGroup(Warp warp, String group) {
        if (!warp.getGroups().contains(group)) {
            return;
        }
        warp.getGroups().remove(group);
        storage.update(warp);
    }

    /**
     * Schedules a task to warp the specified player to the specified warp
     * 
     * @param player The player to be warped
     * @param warp The location the player is to be warped to
     * @return An error if one exists, or Optional.absent() otherwise
     */
    public Optional<String> scheduleWarp(Player player, Warp warp) {
        long delay = plugin.getConfig().getWarpDelay() * Constants.TICKS_PER_SECOND;

        // Schedule the task
        Optional<Task> optTask = plugin.getGame().getSyncScheduler().runTaskAfter(plugin, new WarpPlayerRunnable(plugin, player, warp), delay);

        if (!optTask.isPresent()) {
            // There was an error scheduling the warp
            return Optional.of(Constants.ERROR_SCHEDULING_WARP);
        }

        warpsInProgress.put(player, optTask.get());

        return Optional.absent();
    }
    
    /**
     * Removes the specified player from the map of players who are waiting to be warped
     * 
     * @param player The player that has been teleported
     */
    public void warpCompleted(Player player) {
        warpsInProgress.remove(player);
    }

    /**
     * Checks if the passed in player currently has a warp request
     * 
     * @param player The player to check
     * @return true if they are about to be warped, false if not.
     */
    public boolean isWarping(Player player) {
        return warpsInProgress.containsKey(player);
    }
    
}
