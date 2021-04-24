package imgfilter.ui;

import imgfilter.color.ColorFilter;
import imgfilter.color.ColorModel;
import imgfilter.color.ColorSpectrum;
import imgfilter.color.ColorModelFilter;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
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

    private final ColorModelFilter mColorModelFilter;

    private final Menu mStreamMenu;

    private final ImageView mImageView;
    private final ImageView mProcessed;
    private final List<ColorControl> mColorControls;

    private final VBox mColorControlsParent;

    public Window(double imageWidth, double imageHeight, ColorModelFilter colorModelFilter) {
        mColorModelFilter = colorModelFilter;

        mImageView = new ImageView();
        mImageView.setFitWidth(imageWidth);
        mImageView.setFitHeight(imageHeight);
        mProcessed = new ImageView();
        mProcessed.setFitWidth(imageWidth);
        mProcessed.setFitHeight(imageHeight);
        mColorControls = new ArrayList<>();

        mColorControlsParent = new VBox();
        mColorControlsParent.setSpacing(5.0);

        VBox colorControlsRoot = new VBox();
        colorControlsRoot.setSpacing(10.0);
        colorControlsRoot.getChildren().addAll(mColorControlsParent);

        ComboBox<ColorModel> colorModelComboBox = new ComboBox<>();
        colorModelComboBox.getItems().addAll(ColorModel.values());
        colorModelComboBox.getSelectionModel().select(0);
        colorModelComboBox.getSelectionModel().selectedItemProperty()
                .addListener((obs, o, n)-> {
            loadColorControls(n);
        });
        colorControlsRoot.getChildren().add(colorModelComboBox);

        loadColorControls(colorModelComboBox.getSelectionModel().getSelectedItem());

        HBox imageView = new HBox();
        imageView.setSpacing(10.0);
        imageView.getChildren().addAll(mImageView, mProcessed);

        mStreamMenu = new Menu("Stream");

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(mStreamMenu);

        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(imageView);
        root.setRight(colorControlsRoot);

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

    private void loadColorControls(ColorModel model) {
        mColorControls.clear();
        mColorControlsParent.getChildren().clear();

        mColorModelFilter.switchModel(model);

        for (Map.Entry<ColorSpectrum, ColorFilter> entry : mColorModelFilter.filters()) {
            ColorSpectrum colorSpectrum = entry.getKey();
            ColorFilter colorFilter = entry.getValue();

            ColorControl min = new ColorControl(colorSpectrum.getName(), colorSpectrum.getRange(), colorFilter.minProperty(), colorSpectrum.getRange().start);
            ColorControl max = new ColorControl(colorSpectrum.getName(), colorSpectrum.getRange(), colorFilter.maxProperty(), colorSpectrum.getRange().end);

            mColorControlsParent.getChildren().addAll(min, max);
            mColorControls.add(min);
            mColorControls.add(max);
        }
    }
}
