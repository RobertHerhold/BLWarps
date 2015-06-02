package com.blocklaunch.blwarps.eventhandlers;

import java.util.List;

import org.spongepowered.api.data.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.tileentity.SignData;
import org.spongepowered.api.event.EventHandler;
import org.spongepowered.api.event.block.tileentity.SignChangeEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Warp;
import com.google.common.base.Optional;

public class SignChangeEventHandler implements EventHandler<SignChangeEvent> {

    private BLWarps plugin;

    public static final String WARP_SIGN_PREFIX = "[Warp]";

    public SignChangeEventHandler(BLWarps plugin) {
        this.plugin = plugin;
    }

    @Override
    public void handle(SignChangeEvent event) throws Exception {
        List<Text> lines = event.getNewData().getLines();
        // Check that the first line is [warp]
        if (Texts.toPlain(lines.get(0)).equalsIgnoreCase(WARP_SIGN_PREFIX)) {
            // Second line has to be the name of the warp
            String warpName = Texts.toPlain(lines.get(1));
            Optional<Warp> optWarp = plugin.getWarpManager().getWarp(warpName);
            if (!optWarp.isPresent()) {
                return;
            }
            event.setNewData(generateWarpSignData(optWarp.get()));
        }

    }

    /**
     * @param warp the warp to represent on the SignData
     * @return the formatted SignData indicating a valid warp sign
     */
    private SignData generateWarpSignData(Warp warp) {
        Optional<DataManipulatorBuilder<SignData>> builder = plugin.getGame().getRegistry().getManipulatorRegistry().getBuilder(SignData.class);
        SignData signData = builder.get().create();

        signData.setLine(0, Texts.of());
        signData.setLine(1, Texts.of(TextColors.DARK_BLUE, WARP_SIGN_PREFIX));
        signData.setLine(2, Texts.of(TextColors.GOLD, warp.getName()));
        signData.setLine(3, Texts.of());

        return signData;

    }

}
