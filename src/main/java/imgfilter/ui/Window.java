package imgfilter.ui;

import imgfilter.color.ColorFilter;
import imgfilter.color.ColorSpectrum;
import imgfilter.color.ColorModelFilter;
import javafx.scene.Parent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Window extends Parent {

    private final Menu mStreamMenu;

    private final ImageView mImageView;
    private final ImageView mProcessed;
    private final List<ColorControl> mColorControls;

    public Window(double imageWidth, double imageHeight, ColorModelFilter colorModelFilter) {
        mImageView = new ImageView();
        mImageView.setFitWidth(imageWidth);
        mImageView.setFitHeight(imageHeight);
        mProcessed = new ImageView();
        mProcessed.setFitWidth(imageWidth);
        mProcessed.setFitHeight(imageHeight);
        mColorControls = new ArrayList<>();

        VBox colorControls = new VBox();
        colorControls.setSpacing(5.0);
        for (Map.Entry<ColorSpectrum, ColorFilter> entry : colorModelFilter.filters()) {
            ColorSpectrum colorSpectrum = entry.getKey();
            ColorFilter colorFilter = entry.getValue();

            ColorControl min = new ColorControl(colorSpectrum.name(), colorSpectrum.range(), colorFilter.minProperty(), colorSpectrum.range().start);
            ColorControl max = new ColorControl(colorSpectrum.name(), colorSpectrum.range(), colorFilter.maxProperty(), colorSpectrum.range().end);

            colorControls.getChildren().addAll(min, max);
            mColorControls.add(min);
            mColorControls.add(max);
        }

        HBox imageView = new HBox();
        imageView.setSpacing(10.0);
        imageView.getChildren().addAll(mImageView, mProcessed);

        mStreamMenu = new Menu("Stream");

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(mStreamMenu);

        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(imageView);
        root.setRight(colorControls);

        getChildren().add(root);
    }

    public void addStreamSwitchingControl(String name, Runnable task) {
        MenuItem menuItem = new MenuItem(name);
        menuItem.setOnAction((e)->task.run());

        mStreamMenu.getItems().add(menuItem);
    }

    public void setUnprocessedImage(Mat image) {
        mImageView.setImage(matToImage(image));
    }

    public void setProcessedImage(Mat image) {
        mProcessed.setImage(matToImage(image));
    }

    public void resetControls() {
        mColorControls.forEach(ColorControl::reset);
    }

    private Image matToImage(Mat mat) {
        MatOfByte buffer = new MatOfByte();
        Imgcodecs.imencode(".png", mat, buffer);
        return new Image(new ByteArrayInputStream(buffer.toArray()));
    }
}
