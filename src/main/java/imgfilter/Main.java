package imgfilter;

import imgfilter.color.ColorModelFilter;
import imgfilter.ui.ProcessingControl;
import imgfilter.ui.Window;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.opencv.core.Core;

public class Main extends Application {

    private Window mWindow;
    private ProcessingControl mProcessingControl;

    @Override
    public void start(Stage primaryStage) throws Exception {
        ColorModelFilter colorModelFilter = new ColorModelFilter();
        mWindow = new Window(300, 350, colorModelFilter);
        mProcessingControl = new ProcessingControl(mWindow, primaryStage, colorModelFilter);

        Scene scene = new Scene(mWindow, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest((e) -> {
            mProcessingControl.shutdown();
        });
        primaryStage.show();
    }

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        launch(args);
    }
}
