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
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.service.permission.option.OptionSubject;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.List;
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

        // Is a global warp accesible to everyone w/ permission or a private
        // warp
        boolean isGlobal = (boolean) args.getOne("g").orElse(false);

        String owner = "";

        if (isGlobal) {
            owner = "global";
        } else {
            int warpsAllowed = 0;

            Subject subject = source.getContainingCollection().get(source.getIdentifier());
            if (subject instanceof OptionSubject) {
                Optional<String> warpLimitOption = ((OptionSubject) subject).getOption(Constants.WARP_CREATION_LIMIT_OPTION);
                if (warpLimitOption.isPresent()) {
                    // Try to parse the option to an integer, warn the player if
                    // it
                    // fails
                    try {
                        warpsAllowed = Integer.valueOf(warpLimitOption.get());
                    } catch (NumberFormatException e) {
                        player.sendMessage(Constants.CANT_PARSE_WARP_CREATION_LIMIT_OPTION_MSG);
                        return CommandResult.empty();
                    }
                }
            }

            // Check if the player is within his/her limit
            List<Warp> playerPrivateWarps = plugin.getWarpManager().getWarpsOwnedBy(player);
            if (playerPrivateWarps.size() >= warpsAllowed) {
                player.sendMessage(Text.of(TextColors.RED, Constants.PREFIX + " You cannot create anymore warps! You currently have ",
                        TextColors.GOLD, playerPrivateWarps.size(), TextColors.RED, " and your limit is ", TextColors.GOLD, warpsAllowed,
                        TextColors.RED, "!"));
                return CommandResult.empty();
            }
            owner = player.getUniqueId().toString();
        }

        Vector3d position = (Vector3d) args.getOne("position").orElse(player.getLocation().getPosition());

        String worldName = player.getWorld().getName();

        Warp newWarp = new Warp(owner, warpName, worldName, position);

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
