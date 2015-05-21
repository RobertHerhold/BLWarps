package com.blocklaunch.blwarps.commands;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Warp;
import com.blocklaunch.blwarps.manager.WarpManager;
import com.flowpowered.math.vector.Vector3d;
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

public class SetWarpCommand implements CommandExecutor {

    private static final Text MUST_BE_PLAYER_MSG = Texts.of(TextColors.RED, BLWarps.PREFIX
            + " You must be a player to send that command (not console)");
    private static final Text SUCCESSFULLY_CREATED_WARP_MSG = Texts.of(TextColors.GREEN, BLWarps.PREFIX + " You have successfully created a warp: ");
    private static final String ERROR_CREATING_WARP_MSG = BLWarps.PREFIX + " There was an error creating the warp: ";

    @Override
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {
        if (!(source instanceof Player)) {
            source.sendMessage(MUST_BE_PLAYER_MSG);
            return CommandResult.empty();
        }
        // Make sure the source is a player before attempting to get their
        // location
        Player player = (Player) source;

        String warpName = (String) args.getOne("name").or("warp");
        Vector3d position = (Vector3d) args.getOne("position").or(player.getLocation().getPosition());

        String worldName = player.getWorld().getName();

        Warp newWarp = new Warp(warpName, worldName, position);

        Optional<String> error = WarpManager.addWarp(newWarp);
        if (error.isPresent()) {
            source.sendMessage(Texts.builder(ERROR_CREATING_WARP_MSG + error.get()).color(TextColors.RED).build());
            return CommandResult.empty();
        } else {
            source.sendMessage(SUCCESSFULLY_CREATED_WARP_MSG.builder().append(Texts.of(TextColors.GOLD, newWarp.toString())).build());
            return CommandResult.success();
        }
    }

}
