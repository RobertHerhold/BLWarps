package com.blocklaunch.blwarps.runnable;

import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Warp;
import com.blocklaunch.blwarps.manager.WarpManager;

public class WarpPlayerRunnable implements Runnable {

	private Player player;
	private Warp warp;

	private static final String WARP_SUCCESS_MSG = BLWarps.PREFIX + " You have been warped to ";
	private static final Text WORLD_NOT_FOUND_MSG = Texts.of(TextColors.RED, BLWarps.PREFIX
			+ " The world you requested to be warped to could not be found!");

	public WarpPlayerRunnable(Player player, Warp warp) {
		this.player = player;
		this.warp = warp;
	}

	@Override
	public void run() {
		if (!player.transferToWorld(warp.getWorld(), warp.getPosition())) {
			player.sendMessage(WORLD_NOT_FOUND_MSG);
		} else {
			player.sendMessage(Texts.builder(WARP_SUCCESS_MSG).color(TextColors.GREEN)
					.append(Texts.of(TextColors.GOLD, warp.getName())).build());

		}

		WarpManager.warpCompleted(player);

	}

}