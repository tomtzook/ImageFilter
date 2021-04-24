package imgfilter.color;

import org.opencv.core.Range;

public class ColorSpectrum {

    private final String mName;
    private final Range mRange;

    public ColorSpectrum(String name, Range range) {
        mName = name;
        mRange = range;
    }

    public String getName() {
        return mName;
    }

    public Range getRange() {
        return mRange;
    }
}
