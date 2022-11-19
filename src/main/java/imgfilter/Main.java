package imgfilter;

import com.castle.nio.temp.TempPath;
import com.castle.nio.zip.OpenZip;
import com.castle.nio.zip.Zip;
import com.castle.util.java.JavaSources;
import com.castle.util.os.OperatingSystem;
import com.castle.util.os.System;
import imgfilter.color.ColorModel;
import imgfilter.color.ColorModelFilter;
import imgfilter.ui.ProcessingControl;
import imgfilter.ui.Window;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.opencv.core.Core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

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
        primaryStage.setTitle("Image Filter");
        primaryStage.setOnCloseRequest((e) -> {
            mProcessingControl.shutdown();
        });
        primaryStage.show();
    }

    public static void main(String[] args) {
        loadNatives();
        launch(args);
    }

    private static void loadNatives() {
        Natives.load(Natives.OPENCV_LIBNAME);
    }
}
