package com.blocklaunch.blwarps.commands.executors.warp;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Constants;
import com.blocklaunch.blwarps.Util;
import com.blocklaunch.blwarps.Warp;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.List;

public class ListWarpsExecutor implements CommandExecutor {

    private BLWarps plugin;

    public ListWarpsExecutor(BLWarps plugin) {
        this.plugin = plugin;
    }

    /**
     * Gets the currently loaded warps, paginates them into pages of size
     * WARPS_PER_PAGE, and sends the warp names in a message to the player
     *
     * @param source
     * @param args
     * @return
     * @throws CommandException
     */
    @Override
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {
        List<Text> warpNames = new ArrayList<Text>();

        for (Warp w : this.plugin.getWarpManager().getPayload()) {
            if (Util.hasPermission(source, w)) {
                warpNames.add(Text.of(Util.warpText(w), TextColors.WHITE, " - ", Util.deleteWarpText(w)));
            }
        }

        if (warpNames.isEmpty()) {
            source.sendMessage(Constants.NO_WARPS_MSG);
            return CommandResult.success();
        }

        PaginationService paginationService = Sponge.getServiceManager().provide(PaginationService.class).get();
        paginationService.builder().contents(warpNames).title(Text.of(TextColors.BLUE, "Warps")).paddingString("-").sendTo(source);

        return CommandResult.success();
    }

}
