package com.blocklaunch.blwarps.commands;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Warp;
import com.blocklaunch.blwarps.manager.WarpManager;
import com.google.common.base.Optional;

import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

public class WarpCommand implements CommandExecutor {

    private static final Text MUST_BE_PLAYER_MSG = Texts.of(TextColors.RED, BLWarps.PREFIX
            + " You must be a player to send that command (not console)");
    private static final Text WARP_NOT_EXIST_MSG = Texts.of(TextColors.RED, BLWarps.PREFIX + " That warp does not exist!");
    private static final String ERROR_WARPING_MSG = BLWarps.PREFIX + " There was an error scheduling your warp: ";
    private static final Text NO_PERMISSION = Texts.of(TextColors.RED, BLWarps.PREFIX + " You do not have permission to use that warp!");

    @Override
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {
        if (!(source instanceof Player)) {
            source.sendMessage(MUST_BE_PLAYER_MSG);
            return CommandResult.empty();
        }
        Player player = (Player) source;

        Optional<Warp> optWarp = args.getOne("warp");
        if (!optWarp.isPresent()) {
            source.sendMessage(WARP_NOT_EXIST_MSG);
            return CommandResult.empty();
        }

        Warp warp = optWarp.get();

        if (checkPermission(player, warp) == false) {
            player.sendMessage(NO_PERMISSION);
            return CommandResult.empty();
        }

        Optional<String> optError = WarpManager.scheduleWarp(player, warp);

        if (optError.isPresent()) {
            player.sendMessage(Texts.of(TextColors.RED, ERROR_WARPING_MSG + optError.get()));
            return CommandResult.empty();
        }

        return CommandResult.success();
    }

    /**
     * @param player The player to check permissions on
     * @param warp The warp the player wants to warp to
     * @return true if the player is allowed to warp, false if not
     */
    public boolean checkPermission(Player player, Warp warp) {
        String warpPermission = "blwarps.warp." + warp.getName();
        String groupPermissionBase = "blwarps.warp.group.";
        String wildCardPermission = "blwarps.warp.*";

        boolean playerIsValid = false;

        // Check permission for individual warp or wildcard
        if (player.hasPermission(warpPermission) || player.hasPermission(wildCardPermission)) {
            playerIsValid = true;
        }

        // Check permission for warp groups
        for (String groupName : warp.getGroups()) {
            String permission = groupPermissionBase + groupName;
            if (player.hasPermission(permission)) {
                playerIsValid = true;
            }
        }

        return playerIsValid;
    }

}
