package imgfilter.color;

import org.opencv.core.Range;

public enum ColorSpectrum {
    HUE(new Range(0, 180)),
    SATURATION(new Range(0, 255)),
    VALUE(new Range(0, 255));

    private final Range mRange;

    ColorSpectrum(Range range) {
        mRange = range;
    }

    public Range range() {
        return mRange;
    }
}
