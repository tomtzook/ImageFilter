package imgfilter;

import imgfilter.color.ColorModelFilter;
import imgfilter.stream.ImageStream;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.function.Consumer;

public class ProcessTask implements Runnable {

    private final ImageStream mImageStream;
    private final Consumer<Mat> mUnprocessedConsumer;
    private final Consumer<Mat> mOutputConsumer;
    private final ColorModelFilter mColorModelFilter;

    public ProcessTask(ImageStream imageStream, Consumer<Mat> unprocessedConsumer, Consumer<Mat> outputConsumer, ColorModelFilter colorModelFilter) {
        mImageStream = imageStream;
        mUnprocessedConsumer = unprocessedConsumer;
        mOutputConsumer = outputConsumer;
        mColorModelFilter = colorModelFilter;
    }

    @Override
    public void run() {
        Mat mat = mImageStream.get();
        if (mat == null) {
            return;
        }

        mUnprocessedConsumer.accept(mat);

        Mat output = new Mat();

        Imgproc.cvtColor(mat, output, Imgproc.COLOR_RGB2HSV);
        Core.inRange(
                output,
                mColorModelFilter.getMinValues(),
                mColorModelFilter.getMaxValues(),
                output);

        mOutputConsumer.accept(output);
    }
}
