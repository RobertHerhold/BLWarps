package com.blocklaunch.blwarps.commands.executors.region;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Constants;
import com.blocklaunch.blwarps.Util;
import com.blocklaunch.blwarps.Warp;
import com.blocklaunch.blwarps.region.WarpRegion;
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
import java.util.Optional;

public class ListWarpRegionsExecutor implements CommandExecutor {

    private BLWarps plugin;

    public ListWarpRegionsExecutor(BLWarps plugin) {
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

        if (this.plugin.getWarpRegionManager().getPayload().isEmpty()) {
            source.sendMessage(Constants.NO_WARP_REGIONS_MSG);
            return CommandResult.success();
        }

        List<Text> warpRegionNames = new ArrayList<Text>();

        for (WarpRegion region : this.plugin.getWarpRegionManager().getPayload()) {
            Optional<Warp> linkedWarpOpt = this.plugin.getWarpManager().getOne(region.getLinkedWarpId());
            if (linkedWarpOpt.isPresent()) {
                if (Util.hasPermission(source, linkedWarpOpt.get()) == false) {
                    continue;
                }
            }
            warpRegionNames
                    .add(Text.of(Util.warpRegionInfoText(region), TextColors.WHITE, " - ", Util.deleteWarpRegionText(region)));
        }

        PaginationService paginationService = Sponge.getServiceManager().provide(PaginationService.class).get();
        paginationService.builder().contents(warpRegionNames).title(Text.of(TextColors.BLUE, "WarpRegions")).paddingString("-").sendTo(source);

        return CommandResult.success();
    }

}
