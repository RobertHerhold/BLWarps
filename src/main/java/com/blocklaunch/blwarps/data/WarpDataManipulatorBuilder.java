package com.blocklaunch.blwarps.data;

import static com.blocklaunch.blwarps.data.WarpKeys.WARP;

import com.blocklaunch.blwarps.Warp;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.util.persistence.InvalidDataException;

import java.util.Optional;

public class WarpDataManipulatorBuilder implements DataManipulatorBuilder<WarpData, ImmutableWarpData> {

    @Override
    public Optional<WarpData> build(DataView container) throws InvalidDataException {
        if (!container.contains(WARP.getQuery())) {
            return Optional.empty();
        }
        Warp warp = container.getSerializable(WARP.getQuery(), Warp.class).get();
        return Optional.of(new WarpData(warp));
    }

    @Override
    public WarpData create() {
        return new WarpData(new Warp());
    }

    public WarpData createFrom(Warp warp) {
        return new WarpData(warp);
    }

    @Override
    public Optional<WarpData> createFrom(DataHolder dataHolder) {
        return create().fill(dataHolder);
    }

}
