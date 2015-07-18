package com.blocklaunch.blwarps.eventhandlers;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Warp;
import com.google.common.base.Optional;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.block.tileentity.SignChangeEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;

import java.util.List;

public class SignChangeEventHandler {

    private BLWarps plugin;

    public static final String WARP_SIGN_PREFIX = "[Warp]";

    public SignChangeEventHandler(BLWarps plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void signChange(SignChangeEvent event) throws Exception {
        List<Text> lines = event.getNewData().getLines();
        // Check that the first line is [warp]
        if (Texts.toPlain(lines.get(0)).equalsIgnoreCase(WARP_SIGN_PREFIX)) {
            // Second line has to be the name of the warp
            String warpName = Texts.toPlain(lines.get(1));
            Optional<Warp> optWarp = this.plugin.getWarpManager().getOne(warpName);
            if (!optWarp.isPresent()) {
                return;
            }
            event.setNewData(this.plugin.getUtil().generateWarpSignData(optWarp.get()));
        }

    }

}
