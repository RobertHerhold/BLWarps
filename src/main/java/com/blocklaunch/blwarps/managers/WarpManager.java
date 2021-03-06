package com.blocklaunch.blwarps.managers;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Constants;
import com.blocklaunch.blwarps.Util;
import com.blocklaunch.blwarps.Warp;
import com.blocklaunch.blwarps.consumers.WarpPlayerConsumer;
import com.blocklaunch.blwarps.managers.storage.StorageManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

public class WarpManager extends WarpBaseManager<Warp> {

    private Map<Player, Task> warpsInProgress = new HashMap<Player, Task>();

    private Validator validator;

    public WarpManager(BLWarps plugin, StorageManager<Warp> storage) {
        super(plugin, storage);
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * Adds a warp with the passed in name and location
     *
     * @param warpName The name of the warp
     * @param warpLocation The location of the warp
     * @return An error if the warp already exists, Optional.empty() otherwise
     */
    @Override
    public Optional<String> addNew(Warp newWarp) {

        for (Warp warp : this.payload) {
            if (warp.getId().equalsIgnoreCase(newWarp.getId())) {
                // A warp with that name already exists
                return Optional.of(Constants.WARP_NAME_EXISTS);
            }
        }
        Set<ConstraintViolation<Warp>> violations = this.validator.validate(newWarp);
        if (!violations.isEmpty()) {
            return Optional.of(violations.iterator().next().getMessage());
        }

        this.payload.add(newWarp);
        this.names.add(newWarp.getName());

        // Save warps after putting a new one in rather than saving when server
        // shuts down to prevent loss of data if the server crashed
        this.storage.saveNew(newWarp);

        // No errors, return an absent optional
        return Optional.empty();

    }

    /**
     * Schedules a task to warp the specified player to the specified warp
     *
     * @param player The player to be warped
     * @param warp The location the player is to be warped to
     * @return An error if one exists, or Optional.empty() otherwise
     */
    public void scheduleWarp(Player player, Warp warp) {
        // Schedule the task
        Task scheduledWarpTask =
                Sponge.getScheduler().createTaskBuilder().delay(this.plugin.getConfig().getWarpDelay(), TimeUnit.SECONDS)
                        .execute(new WarpPlayerConsumer(this.plugin, player, warp)).submit(this.plugin);

        this.warpsInProgress.put(player, scheduledWarpTask);

        Text timingText = Text.of();

        if (this.plugin.getConfig().getWarpDelay() != 0) {
            timingText = Text.of(" in ",
                    TextColors.GOLD, this.plugin.getConfig().getWarpDelay(), TextColors.GREEN, " seconds.");
        }

        // Notify the player that they will be warped
        player.sendMessage(Text.of(TextColors.GREEN, Constants.PREFIX + " You will be warped to ", Util.warpText(warp), timingText));

        // If the pvp-protect config setting is set to true, warn the player not
        // to move
        if (this.plugin.getConfig().isPvpProtect() == true) {
            player.sendMessage(Constants.DONT_MOVE_MSG);
        }

    }

    public void cancelWarp(Player player) {
        Task task = this.warpsInProgress.get(player);
        task.cancel();

        Warp previousDestination = ((WarpPlayerConsumer) task.getConsumer()).getWarp();

        player.sendMessage(Text.of(TextColors.RED, Constants.PREFIX + " Your warp to ", Util.warpText(previousDestination),
                " has been canceled!"));
        this.warpsInProgress.remove(player);
    }

    public Optional<Warp> getPlayerDestination(Player player) {
        if (!this.warpsInProgress.containsKey(player)) {
            return Optional.empty();
        }
        Task task = this.warpsInProgress.get(player);
        Warp destination = ((WarpPlayerConsumer) task.getConsumer()).getWarp();
        return Optional.of(destination);
    }

    public List<Warp> getWarpsOwnedBy(Player player) {
        List<Warp> warps = new ArrayList<Warp>();
        for (Warp w : this.payload) {
            if (w.getOwner().equalsIgnoreCase(player.getUniqueId().toString())) {
                warps.add(w);
            }
        }
        return warps;
    }

    /**
     * Removes the specified player from the map of players who are waiting to
     * be warped
     *
     * @param player The player that has been teleported
     */
    public void warpCompleted(Player player) {
        this.warpsInProgress.remove(player);
    }

    /**
     * Checks if the passed in player currently has a warp request
     *
     * @param player The player to check
     * @return true if they are about to be warped, false if not.
     */
    public boolean isWarping(Player player) {
        return this.warpsInProgress.containsKey(player);
    }

}
