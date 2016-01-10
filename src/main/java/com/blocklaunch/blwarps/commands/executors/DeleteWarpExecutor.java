package com.blocklaunch.blwarps.commands.executors;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Constants;
import com.blocklaunch.blwarps.Util;
import com.blocklaunch.blwarps.Warp;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

public class DeleteWarpExecutor implements CommandExecutor {

    private BLWarps plugin;

    public DeleteWarpExecutor(BLWarps plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {
        Optional<Warp> optWarp = args.getOne("warp");
        if (!optWarp.isPresent()) {
            source.sendMessage(Constants.WARP_NOT_FOUND_MSG);
            return CommandResult.empty();
        }

        Warp warp = optWarp.get();

        this.plugin.getWarpManager().deleteOne(warp);

        source.sendMessage(Text.of(Constants.SUCCESS_DELETE_WARP_MSG, TextColors.GOLD, warp.getName(), TextColors.WHITE, " - ",
                Util.undoDeleteWarpText(warp)));
        return CommandResult.success();
    }

}
