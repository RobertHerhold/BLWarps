package com.blocklaunch.blwarps.eventhandlers;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.block.tileentity.TileEntityTypes;
import org.spongepowered.api.data.manipulator.mutable.tileentity.SignData;
import org.spongepowered.api.data.value.mutable.ListValue;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.text.Text;
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

        TileEntity signEntity = block.getTileEntity().get();
        // Get the SignData
        Optional<SignData> signData = signEntity.get(SignData.class);
        // Shouldn't have to check this
        if (!signData.isPresent()) {
            return;
        }
        // Validate that this is supposed to be a warp sign
        ListValue<Text> lines = signData.get().lines();
        if (!lines.get(1).toPlain().equalsIgnoreCase(ChangeSignEventHandler.WARP_SIGN_PREFIX)) {
            return;
        }
        // Don't need to validate that the warp actually exists --> Command
        // executor will take care
        // of it. (along with permissions)
        String command = "warp " + lines.get(2).toPlain();

        Optional<Player> player = event.getCause().first(Player.class);

        if (!player.isPresent()) {
            return;
        }

        Sponge.getCommandManager().process(player.get(), command);
    }

}
