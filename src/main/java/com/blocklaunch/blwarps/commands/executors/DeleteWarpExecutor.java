package com.blocklaunch.blwarps.commands.executors;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Warp;
import com.google.common.base.Optional;

public class DeleteWarpExecutor implements CommandExecutor {

    private static final Text SUCCESS_DELETE_WARP_MSG = Texts.of(TextColors.GREEN, BLWarps.PREFIX + " You successfully deleted the warp: ");
    private static final Text WARP_NOT_EXIST_MSG = Texts.of(TextColors.RED, BLWarps.PREFIX + " You must specify a valid warp!");

    private BLWarps plugin;

    public DeleteWarpExecutor(BLWarps plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {
        Optional<Warp> optWarp = args.getOne("warp");
        if (!optWarp.isPresent()) {
            source.sendMessage(WARP_NOT_EXIST_MSG);
            return CommandResult.empty();
        }
        
        Warp warp = optWarp.get();

        plugin.getWarpManager().deleteWarp(warp);

        source.sendMessage(SUCCESS_DELETE_WARP_MSG.builder().append(Texts.of(TextColors.GOLD, warp.getName())).build());
        return CommandResult.success();
    }

}
