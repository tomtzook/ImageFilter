package imgfilter.color;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.ToIntFunction;

public class ColorModelFilter {

    private ColorModel mModel;
    private final Map<ColorSpectrum, ColorFilter> mFilterMap;

    public ColorModelFilter() {
        mModel = null;
        mFilterMap = new LinkedHashMap<>();
    }

    public ColorModel getModel() {
        return mModel;
    }

    public void switchModel(ColorModel model) {
        mModel = model;
        mFilterMap.clear();
        for (ColorSpectrum type : model.getColorSpectrum()) {
            mFilterMap.put(type, new ColorFilter(type));
        }
    }

    public void filter(Mat source, Mat output) {
        if (mModel == null) {
            return;
        }

        mModel.changeColorSpace(source, output);
        Core.inRange(
                output,
                getMinValues(),
                getMaxValues(),
                output);
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
        for (ColorSpectrum type : mModel.getColorSpectrum()) {
            ColorFilter colorFilter = mFilterMap.get(type);
            scalar[index++] = filterToIntFunction.applyAsInt(colorFilter);
        }

        return new Scalar(scalar[0], scalar[1], scalar[2]);
    }

    public Iterable<? extends Map.Entry<ColorSpectrum, ColorFilter>> filters() {
        return mFilterMap.entrySet();
    }
}
