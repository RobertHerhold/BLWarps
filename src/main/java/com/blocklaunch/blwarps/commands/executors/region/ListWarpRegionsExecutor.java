package com.blocklaunch.blwarps.commands.executors.region;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Constants;
import com.blocklaunch.blwarps.Util;
import com.blocklaunch.blwarps.Warp;
import com.blocklaunch.blwarps.region.WarpRegion;
import java.util.Optional;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

import java.util.ArrayList;
import java.util.List;

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
            Optional<Warp> linkedWarpOpt = this.plugin.getWarpManager().getOne(region.getLinkedWarpName());
            if (linkedWarpOpt.isPresent()) {
                if (this.plugin.getUtil().hasPermission(source, linkedWarpOpt.get()) == false) {
                    continue;
                }
            }
            warpRegionNames
                    .add(Texts.of(Util.warpRegionInfoText(region), TextColors.WHITE, " - ", Util.deleteWarpRegionText(region)));
        }

        PaginationService paginationService = this.plugin.getGame().getServiceManager().provide(PaginationService.class).get();
        paginationService.builder().contents(warpRegionNames).title(Texts.of(TextColors.BLUE, "WarpRegions")).paddingString("-").sendTo(source);

        return CommandResult.success();
    }

}
