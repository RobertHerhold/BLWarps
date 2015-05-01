package com.blocklaunch.blwarps.commands;

import java.util.List;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextBuilder;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.manager.WarpManager;
import com.google.common.base.Optional;

public class ListWarpsCommand implements CommandCallable {

	private static final Text USAGE = Texts.of("/listwarps [page number]");
	private static final Text HELP = Texts
			.of("Lists all warps, split up into pages. Optionally, specify a page number as an argument");
	private static final Text SHORT_DESC = Texts.of("List warps");

	private static final Text ERROR_PARSING_NUMBER_MSG = Texts.of(TextColors.RED, BLWarps.PREFIX
			+ " There was an error parsing the page number.");
	private static final Text NO_WARPS_MSG = Texts.of(TextColors.GREEN, BLWarps.PREFIX
			+ " There were no warps to display.");

	private static final int WARPS_PER_PAGE = 10;

	@Override
	public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Gets the currently loaded warps, paginates them into pages of size
	 * WARPS_PER_PAGE, and sends the warp names in a message to the player
	 * 
	 * @param source
	 * @param arguments
	 * @param parents
	 * @return
	 * @throws CommandException
	 */
	@Override
	public Optional<CommandResult> process(CommandSource source, String arguments) throws CommandException {
		if (WarpManager.warps.isEmpty()) {
			source.sendMessage(NO_WARPS_MSG);
			return Optional.of(CommandResult.success());
		}

		int pageNum = 1;

		// If there are arguments, parse them and get a page number
		if (!arguments.isEmpty()) {
			String[] args = arguments.split(" ");
			try {
				pageNum = Integer.parseInt(args[0]);
			} catch (Exception e) {
				source.sendMessage(ERROR_PARSING_NUMBER_MSG);
				return Optional.of(CommandResult.empty());
			}
		}

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
					.append(Texts.builder(WarpManager.warps.get(index).getName()).color(TextColors.GOLD).build())
					.build());
			textBuilder.append(Texts.of("\n"));

			listNumber++;
		}
		source.sendMessage(textBuilder.build());

		return Optional.of(CommandResult.success());
	}

	@Override
	public Optional<Text> getHelp(CommandSource arg0) {
		return Optional.of(HELP);
	}

	@Override
	public Optional<Text> getShortDescription(CommandSource arg0) {
		return Optional.of(SHORT_DESC);
	}

	@Override
	public Text getUsage(CommandSource arg0) {
		return USAGE;
	}

	@Override
	public boolean testPermission(CommandSource source) {
		return false;
	}

}
