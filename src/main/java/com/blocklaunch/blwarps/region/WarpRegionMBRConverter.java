package com.blocklaunch.blwarps.region;

import org.khelekore.prtree.MBRConverter;

public class WarpRegionMBRConverter implements MBRConverter<WarpRegion> {

    private static int NUM_DIMENSIONS = 3;

    @Override
    public int getDimensions() {
        return NUM_DIMENSIONS;
    }

    @Override
    public double getMax(int axis, WarpRegion region) {
        switch (axis) {
            case 0:
                return region.getMaxLoc().getX();
            case 1:
                return region.getMaxLoc().getY();
            case 2:
                return region.getMaxLoc().getZ();
            default:
                return 0;
        }
    }

    @Override
    public double getMin(int axis, WarpRegion region) {
        switch (axis) {
            case 0:
                return region.getMinLoc().getX();
            case 1:
                return region.getMinLoc().getY();
            case 2:
                return region.getMinLoc().getZ();
            default:
                return 0;
        }
    }

}
