package com.blocklaunch.blwarps.data;

import static com.blocklaunch.blwarps.data.WarpDataQueries.NAME;
import static com.blocklaunch.blwarps.data.WarpDataQueries.WORLD;
import static com.blocklaunch.blwarps.data.WarpDataQueries.X;
import static com.blocklaunch.blwarps.data.WarpDataQueries.Y;
import static com.blocklaunch.blwarps.data.WarpDataQueries.Z;

import com.blocklaunch.blwarps.Warp;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.util.persistence.DataBuilder;
import org.spongepowered.api.util.persistence.InvalidDataException;

import java.util.Optional;

public class WarpBuilder implements DataBuilder<Warp> {

    @Override
    public Optional<Warp> build(DataView container) throws InvalidDataException {
        if (container.contains(NAME, WORLD, X, Y, Z)) {
            Warp warp = new Warp(
                    container.getString(NAME).get(),
                    container.getString(WORLD).get(),
                    new Vector3d(
                            container.getInt(X).get().intValue(),
                            container.getInt(Y).get().intValue(),
                            container.getInt(Z).get().intValue()));
            return Optional.of(warp);
        }
        return Optional.empty();
    }
}
