package imgfilter.color;

import org.opencv.core.Mat;
import org.opencv.core.Range;
import org.opencv.imgproc.Imgproc;

public enum ColorModel {
    RGB(
            new ColorSpectrum("Red", new Range(0, 255)),
            new ColorSpectrum("Green", new Range(0, 255)),
            new ColorSpectrum("Blue", new Range(0, 255))
    ) {
        @Override
        public void changeColorSpace(Mat source, Mat output) {
            // it is RGB
            source.copyTo(output);
        }
    },
    HSV(
            new ColorSpectrum("Hue", new Range(0, 180)),
            new ColorSpectrum("Saturation", new Range(0, 255)),
            new ColorSpectrum("Value", new Range(0, 255))
    ) {
        @Override
        public void changeColorSpace(Mat source, Mat output) {
            Imgproc.cvtColor(source, output, Imgproc.COLOR_RGB2HSV);
        }
    }
    ;

    private final ColorSpectrum[] mColorSpectrum;

    ColorModel(ColorSpectrum... spectrums) {
        mColorSpectrum = spectrums;
    }

    public ColorSpectrum[] getColorSpectrum() {
        return mColorSpectrum;
    }

    public abstract void changeColorSpace(Mat source, Mat output);
}
