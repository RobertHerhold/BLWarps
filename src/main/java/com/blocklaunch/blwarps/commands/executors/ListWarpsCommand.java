package com.blocklaunch.blwarps.commands.executors;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.managers.WarpManager;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextBuilder;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

public class ListWarpsCommand implements CommandExecutor {

    private static final Text NO_WARPS_MSG = Texts.of(TextColors.GREEN, BLWarps.PREFIX + " There were no warps to display.");

    private static final int WARPS_PER_PAGE = 10;

    /**
     * Gets the currently loaded warps, paginates them into pages of size WARPS_PER_PAGE, and sends
     * the warp names in a message to the player
     * 
     * @param source
     * @param args
     * @return
     * @throws CommandException
     */
    @Override
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {

        if (WarpManager.warps.isEmpty()) {
            source.sendMessage(NO_WARPS_MSG);
            return CommandResult.success();
        }

        int pageNum = (int) args.getOne("page").or(1);

        // Minus 1 to convert from page number to page index
        int pageIndex = pageNum - 1;

        // This is only a double so that the Math.ceil behaves properly
        double numWarps = WarpManager.warps.size();

        int numPages = (int) Math.ceil(numWarps / WARPS_PER_PAGE);

        // 0-based index of where in the array to start listing the warps
        int warpStartIndex = pageIndex * WARPS_PER_PAGE;

        // For lack of a better name, "listNumber" is the number preceding
        // the warp name in the ordered list
        int listNumber = warpStartIndex + 1;

        source.sendMessage(Texts.of(TextColors.BLUE, "Warps Page " + pageNum + "/" + numPages));
        TextBuilder textBuilder = Texts.builder();
        for (int index = warpStartIndex; index < warpStartIndex + WARPS_PER_PAGE; index++) {
            // Check if the array at the index exists to eliminate
            // IndexOutOfBoundExceptions
            if (WarpManager.warps.size() <= index) {
                // element does not exist at that index
                break;
            }

            textBuilder.append(Texts.builder(listNumber + ". ").color(TextColors.RED)
                    .append(Texts.builder(WarpManager.warps.get(index).getName()).color(TextColors.GOLD).build()).build());
            textBuilder.append(Texts.of("\n"));

            listNumber++;
        }
        source.sendMessage(textBuilder.build());

        return CommandResult.success();
    }

}
