package com.blocklaunch.blwarps.data;

import static com.blocklaunch.blwarps.data.WarpKeys.WARP;

import com.blocklaunch.blwarps.Warp;
import jersey.repackaged.com.google.common.base.Preconditions;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractSingleData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.Optional;

public class WarpData extends AbstractSingleData<Warp, WarpData, ImmutableWarpData> {

    protected WarpData(Warp value) {
        super(value, WARP);
    }

    @Override
    public WarpData copy() {
        return new WarpData(this.getValue());
    }

    @Override
    public Optional<WarpData> fill(DataHolder dataHolder, MergeFunction mergeFn) {
        WarpData warpData = Preconditions.checkNotNull(mergeFn).merge(copy(), dataHolder.get(WarpData.class).orElse(copy()));
        return Optional.of(set(WARP, warpData.get(WARP).get()));
    }

    @Override
    public Optional<WarpData> from(DataContainer container) {
        if (container.contains(WARP.getQuery())) {
            return Optional.of(set(WARP, container.getSerializable(WARP.getQuery(), Warp.class).orElse(getValue())));
        }
        return Optional.empty();
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    @Override
    public ImmutableWarpData asImmutable() {
        return new ImmutableWarpData(this.getValue());
    }

    @Override
    public int compareTo(WarpData arg0) {
        return 0;
    }

    @Override
    protected Value<Warp> getValueGetter() {
        return Sponge.getRegistry().getValueFactory().createValue(WARP, getValue());
    }

    @Override
    public DataContainer toContainer() {
        return super.toContainer().set(WARP, getValue());
    }

}
