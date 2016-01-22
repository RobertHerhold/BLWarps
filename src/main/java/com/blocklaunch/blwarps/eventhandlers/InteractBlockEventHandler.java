package com.blocklaunch.blwarps.eventhandlers;

import com.blocklaunch.blwarps.Warp;
import com.blocklaunch.blwarps.data.WarpKeys;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.tileentity.Sign;
import org.spongepowered.api.block.tileentity.TileEntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;

/**
 * Listens for Players interacting with blocks If
 */
public class InteractBlockEventHandler {

    @Listener
    // InteractBlockEvent.Secondary = right click
            public
            void handle(InteractBlockEvent.Secondary event) throws Exception {
        if (!event.getTargetBlock().getLocation().isPresent()) {
            return;
        }

        Location<World> block = event.getTargetBlock().getLocation().get();

        // Ensure the block is a tile entity
        if (!block.getTileEntity().isPresent()) {
            return;
        }

        // Ensure the tile entity is a sign
        if (!(block.getTileEntity().get().getType() == TileEntityTypes.SIGN)) {
            return;
        }

        Sign sign = (Sign) block.getTileEntity().get();

        Optional<Warp> optWarp = sign.get(WarpKeys.WARP);
        if (!optWarp.isPresent()) {
            // Sign is not a warp sign
            return;
        }

        String command = "warp " + optWarp.get().getName();

        Optional<Player> player = event.getCause().first(Player.class);

        if (!player.isPresent()) {
            return;
        }

        Sponge.getCommandManager().process(player.get(), command);
    }

}
