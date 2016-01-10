package com.blocklaunch.blwarps.commands.executors;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Constants;
import com.blocklaunch.blwarps.Util;
import com.blocklaunch.blwarps.Warp;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

public class CreateWarpExecutor implements CommandExecutor {

    private BLWarps plugin;

    public CreateWarpExecutor(BLWarps plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {
        if (!(source instanceof Player)) {
            source.sendMessage(Constants.MUST_BE_PLAYER_MSG);
            return CommandResult.empty();
        }
        // Make sure the source is a player before attempting to get their
        // location
        Player player = (Player) source;

        String warpName = (String) args.getOne("name").orElse("warp");

        if (Constants.FORBIDDEN_NAMES.contains(warpName.toLowerCase())) {
            source.sendMessage(Constants.CANNOT_USE_FORBIDDEN_NAME_MSG);
            return CommandResult.empty();
        }

        Vector3d position = (Vector3d) args.getOne("position").orElse(player.getLocation().getPosition());

        String worldName = player.getWorld().getName();

        Warp newWarp = new Warp(warpName, worldName, position);

        Optional<String> error = this.plugin.getWarpManager().addNew(newWarp);
        if (error.isPresent()) {
            source.sendMessage(Text.builder(Constants.ERROR_CREATE_WARP_MSG + error.get()).color(TextColors.RED).build());
            return CommandResult.empty();
        } else {
            source.sendMessage(Text.of(Constants.SUCCESS_CREATE_WARP_MSG, Util.warpText(newWarp)));
            return CommandResult.success();
        }
    }

}
