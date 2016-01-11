package com.blocklaunch.blwarps.data;

import static com.blocklaunch.blwarps.data.WarpKeys.WARP;

import com.blocklaunch.blwarps.Warp;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableSingleData;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

import java.util.Optional;

public class ImmutableWarpData extends AbstractImmutableSingleData<Warp, ImmutableWarpData, WarpData> {

    protected ImmutableWarpData(Warp value) {
        super(value, WARP);
    }

    @Override
    public <E> Optional<ImmutableWarpData> with(Key<? extends BaseValue<E>> key, E value) {
        if (this.supports(key)) {
            return Optional.of(asMutable().set(key, value).asImmutable());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public int compareTo(ImmutableWarpData arg0) {
        return 0;
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    @Override
    protected ImmutableValue<?> getValueGetter() {
        return Sponge.getRegistry().getValueFactory().createValue(WARP, getValue()).asImmutable();
    }

    @Override
    public WarpData asMutable() {
        return new WarpData(this.getValue());
    }

}
