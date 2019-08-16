package imgfilter.color;

import org.opencv.core.Scalar;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.ToIntFunction;

public class ColorModelFilter {

    private final Map<ColorSpectrum, ColorFilter> mFilterMap;

    public ColorModelFilter() {
        mFilterMap = new LinkedHashMap<>();
        for (ColorSpectrum type : ColorSpectrum.values()) {
            mFilterMap.put(type, new ColorFilter(type));
        }
    }

    public Scalar getMinValues() {
        return filtersToScalar((filter)->filter.minProperty().get());
    }

    public Scalar getMaxValues() {
        return filtersToScalar((filter)->filter.maxProperty().get());
    }

    private Scalar filtersToScalar(ToIntFunction<ColorFilter> filterToIntFunction) {
        int[] scalar = new int[3];
        int index = 0;
        for (ColorSpectrum type : ColorSpectrum.values()) {
            ColorFilter colorFilter = mFilterMap.get(type);
            scalar[index++] = filterToIntFunction.applyAsInt(colorFilter);
        }

        return new Scalar(scalar[0], scalar[1], scalar[2]);
    }

    public Iterable<? extends Map.Entry<ColorSpectrum, ColorFilter>> filters() {
        return mFilterMap.entrySet();
    }
}
