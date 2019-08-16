package imgfilter.stream;

import org.opencv.core.Mat;

public class EmptyImage implements ImageStream {

    @Override
    public Mat get() {
        return null;
    }

    @Override
    public void close() {

    }
}
