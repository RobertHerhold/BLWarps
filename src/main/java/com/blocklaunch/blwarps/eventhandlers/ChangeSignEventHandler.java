package com.blocklaunch.blwarps.eventhandlers;

import org.spongepowered.api.text.format.TextColors;

import org.spongepowered.api.entity.living.player.Player;
import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Constants;
import com.blocklaunch.blwarps.Util;
import com.blocklaunch.blwarps.Warp;
import com.blocklaunch.blwarps.data.WarpData;
import com.blocklaunch.blwarps.data.WarpDataManipulatorBuilder;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.data.value.mutable.ListValue;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.tileentity.ChangeSignEvent;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class ChangeSignEventHandler {

    private BLWarps plugin;

    public ChangeSignEventHandler(BLWarps plugin) {
        this.plugin = plugin;
    }

    @Listener
    public void signChange(ChangeSignEvent event) throws Exception {
        ListValue<Text> lines = event.getText().lines();

        // Check that the first line is [warp]
        if (lines.get(0).toPlain().equalsIgnoreCase(Constants.WARP_SIGN_PREFIX)) {
            // Second line has to be the name of the warp
            String warpName = lines.get(1).toPlain();
            Optional<Warp> optWarp = this.plugin.getWarpManager().getOne(warpName);
            if (!optWarp.isPresent()) {
                return;
            }
            event.getText().set(Util.generateWarpSignData(optWarp.get()).getValues());

            WarpDataManipulatorBuilder builder = (WarpDataManipulatorBuilder) Sponge.getDataManager().getManipulatorBuilder(WarpData.class).get();
            WarpData data = builder.createFrom(optWarp.get());
            DataTransactionResult result = event.getTargetTile().offer(data);

            if (!result.isSuccessful()) {
                // Couldn't offer WarpData to the sign - log in the console and
                // warn the possible player that placed the sign
                String error =
                        "Failed to offer WarpData " + data.toContainer().toString() + " to the Sign at "
                                + event.getTargetTile().getLocation().toString();
                plugin.getLogger().warn(error);
                Optional<Player> optPlayer = event.getCause().first(Player.class);
                if (optPlayer.isPresent()) {
                    optPlayer.get().sendMessage(Text.of(TextColors.RED, Constants.PREFIX, " ", error));
                }

            }
        }

    }
}
