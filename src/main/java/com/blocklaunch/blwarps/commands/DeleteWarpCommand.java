package com.blocklaunch.blwarps.commands;

import com.blocklaunch.blwarps.BLWarps;
import com.google.common.base.Optional;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

public class DeleteWarpCommand implements CommandExecutor {

    private static final String ERROR_DELETE_WARP_MSG = BLWarps.PREFIX + " There was an error deleting the warp: ";
    private static final Text SUCCESS_DELETE_WARP_MSG = Texts.of(TextColors.GREEN, BLWarps.PREFIX + " You successfully deleted the warp: ");

    private BLWarps plugin;

    public DeleteWarpCommand(BLWarps plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {
        String warpName = (String) args.getOne("name").orNull();

        Optional<String> optError = plugin.getWarpManager().deleteWarp(warpName);

        if (optError.isPresent()) {
            source.sendMessage(Texts.builder(ERROR_DELETE_WARP_MSG + optError.get()).color(TextColors.RED).build());
            return CommandResult.empty();
        }

        source.sendMessage(SUCCESS_DELETE_WARP_MSG.builder().append(Texts.of(TextColors.GOLD, warpName)).build());
        return CommandResult.success();
    }

}
