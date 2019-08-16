package imgfilter.ui;

import imgfilter.ProcessTask;
import imgfilter.color.ColorModelFilter;
import imgfilter.stream.EmptyImage;
import imgfilter.stream.ImageStream;
import imgfilter.stream.selectors.CameraStreamSelector;
import imgfilter.stream.selectors.StaticImageSelector;
import imgfilter.stream.selectors.StreamSelector;
import javafx.stage.Stage;

import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class ProcessingControl {

    private final Window mWindow;
    private final ColorModelFilter mColorModelFilter;

    private final ScheduledExecutorService mExecutorService;
    private final AtomicReference<Future<?>> mRunFuture;

    private ImageStream mImageStream;

    public ProcessingControl(Window window, Stage stage, ColorModelFilter colorModelFilter) {
        mWindow = window;
        mWindow.addStreamSwitchingControl("Load Image", ()->{
            selectNewStream(new StaticImageSelector(stage));
        });
        mWindow.addStreamSwitchingControl("Open Camera", ()->{
            selectNewStream(new CameraStreamSelector());
        });

        mColorModelFilter = colorModelFilter;

        mExecutorService = Executors.newScheduledThreadPool(1);
        mRunFuture = new AtomicReference<>(null);

        mImageStream = new EmptyImage();
    }


    public synchronized void start() {
        Future<?> future = mExecutorService.scheduleAtFixedRate(
                new ProcessTask(
                        mImageStream,
                        mWindow::setUnprocessedImage,
                        mWindow::setProcessedImage, mColorModelFilter),
                100,
                100,
                TimeUnit.MILLISECONDS);
        mRunFuture.set(future);
    }

    public synchronized void stop() {
        Future<?> future = mRunFuture.getAndSet(null);
        if (future != null) {
            future.cancel(true);
        }
    }

    public synchronized void shutdown() {
        stop();
        mExecutorService.shutdownNow();

        mImageStream.close();
    }

    private void selectNewStream(StreamSelector selector) {
        stop();
        try {
            Optional<ImageStream> optionalImageStream = selector.selectNew();
            if (!optionalImageStream.isPresent()) {
                // canceled
                return;
            }

            ImageStream newStream = optionalImageStream.get();
            switchStream(newStream);
            mWindow.resetControls();
        } catch (Exception e) {
            e.printStackTrace();
            ErrorDialog.show("Error", "Error Switching Streams", e);
        } finally {
            start();
        }
    }

    private void switchStream(ImageStream newStream) {
        ImageStream old = mImageStream;
        mImageStream = newStream;
        old.close();
    }
}
