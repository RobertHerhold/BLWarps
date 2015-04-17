package com.blocklaunch.spongewarps.runnable;

import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;

import com.blocklaunch.spongewarps.SpongeWarps;
import com.blocklaunch.spongewarps.Warp;
import com.blocklaunch.spongewarps.manager.WarpManager;
import com.flowpowered.math.vector.Vector3d;

public class WarpPlayerRunnable implements Runnable {

	private Player player;
	private Warp warp;

	private static final String WARP_SUCCESS_MSG = SpongeWarps.PREFIX + " You have been warped to ";

	public WarpPlayerRunnable(Player player, Warp warp) {
		this.player = player;
		this.warp = warp;
	}

	@Override
	public void run() {
		// Get old location of player --> insert new coordinates --> set
		// player's location
		// Does not support warping across worlds
		Location oldLoc = player.getLocation();
		Location newLoc = oldLoc.setPosition(new Vector3d(warp.getX(), warp.getY(), warp.getZ()));
		player.setLocation(newLoc);

		player.sendMessage(Texts.builder(WARP_SUCCESS_MSG).color(TextColors.GREEN)
				.append(Texts.of(TextColors.GOLD, warp.getName())).build());

		WarpManager.warpCompleted(player);

	}

}
