package com.blocklaunch.spongewarps.commands;

import java.util.List;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextBuilder;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandSource;

import com.blocklaunch.spongewarps.SpongeWarps;
import com.blocklaunch.spongewarps.Warp;
import com.blocklaunch.spongewarps.WarpManager;
import com.google.common.base.Optional;

public class ListWarpsCommand implements CommandCallable {

	private static final String USAGE = "/listwarps [page number]";
	private static final String HELP = "Lists all warps.";

	private static final Text ERROR_PARSING_NUMBER_MSG = Texts.of(TextColors.RED, SpongeWarps.PREFIX
			+ " There was an error parsing the page number.");

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
	public boolean call(CommandSource source, String arguments, List<String> parents) throws CommandException {
		int pageNum = 1;

		// If there are arguments, parse them and get a page number
		if (!arguments.isEmpty()) {
			String[] args = arguments.split(" ");
			try {
				pageNum = Integer.parseInt(args[0]);
			} catch (Exception e) {
				source.sendMessage(ERROR_PARSING_NUMBER_MSG);
				return false;
			}
		}

		// Minus 1 to convert from page number to page index
		int pageIndex = pageNum - 1;

		// This is only a double so that the Math.ceil behaves properly
		double numWarps = WarpManager.warps.size();

		int numPages = (int) Math.ceil(numWarps / WARPS_PER_PAGE);

		// 0-based index of where in the array to start listing the warps
		int warpStartIndex = pageIndex * WARPS_PER_PAGE;

		Warp[] warpArray = WarpManager.warps.toArray(new Warp[0]);

		// For lack of a better name, "listNumber" is the number preceding
		// the warp name in the ordered list
		int listNumber = warpStartIndex + 1;

		source.sendMessage(Texts.of(TextColors.BLUE, "Warps Page " + pageNum + "/" + numPages));
		TextBuilder textBuilder = Texts.builder();
		for (int index = warpStartIndex; index < warpStartIndex + WARPS_PER_PAGE; index++) {
			// Check if the array at the index exists to eliminate
			// ArrayIndexOutOfBoundExceptions
			if (warpArray.length <= index) {
				// element does not exist at that index
				break;
			}

			textBuilder.append(Texts.builder(listNumber + ". ").color(TextColors.RED)
					.append(Texts.builder(warpArray[index].getName()).color(TextColors.GOLD).build()).build());
			textBuilder.append(Texts.of("\n"));

			listNumber++;
		}
		source.sendMessage(textBuilder.build());

		return true;
	}

	@Override
	public boolean testPermission(CommandSource source) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Optional<String> getShortDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<String> getHelp() {
		return Optional.of(HELP);
	}

	@Override
	public String getUsage() {
		return USAGE;
	}

}
