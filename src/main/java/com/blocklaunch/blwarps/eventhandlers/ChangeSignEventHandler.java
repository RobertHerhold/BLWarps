package com.blocklaunch.blwarps.eventhandlers;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Util;
import com.blocklaunch.blwarps.Warp;
import org.spongepowered.api.data.value.mutable.ListValue;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.tileentity.ChangeSignEvent;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class ChangeSignEventHandler {

    private BLWarps plugin;

    public static final String WARP_SIGN_PREFIX = "[Warp]";

    public ChangeSignEventHandler(BLWarps plugin) {
        this.plugin = plugin;
    }

    @Listener
    public void signChange(ChangeSignEvent event) throws Exception {
        ListValue<Text> lines = event.getText().lines();

        // Check that the first line is [warp]
        if (lines.get(0).toPlain().equalsIgnoreCase(WARP_SIGN_PREFIX)) {
            // Second line has to be the name of the warp
            String warpName = lines.get(1).toPlain();
            Optional<Warp> optWarp = this.plugin.getWarpManager().getOne(warpName);
            if (!optWarp.isPresent()) {
                return;
            }
            event.getText().set(Util.generateWarpSignData(optWarp.get()).getValues());
        }

    }

}
