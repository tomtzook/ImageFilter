package imgfilter.ui;

import javafx.beans.property.IntegerProperty;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.opencv.core.Range;

public class ColorControl extends VBox {

    private final Label mValueLbl;
    private final Slider mSlider;

    private final int mInitialValue;

    public ColorControl(String name, Range range, IntegerProperty valueProperty, int value) {
        mInitialValue = value;

        mValueLbl = new Label();
        mSlider = new Slider();

        HBox top = new HBox();
        top.setSpacing(5.0);
        top.getChildren().add(new Label(name));
        top.getChildren().add(mValueLbl);
        getChildren().add(top);

        getChildren().add(mSlider);

        mSlider.setMin(range.start);
        mSlider.setMax(range.end);
        mSlider.setValue(value);
        mValueLbl.setText(String.valueOf(value));
        mSlider.setBlockIncrement(1.0);
        mSlider.valueProperty().addListener((obv, o, n) -> {
            valueProperty.set(n.intValue());
            mValueLbl.setText(String.valueOf(n.intValue()));
        });
    }

    public void reset() {
        mSlider.setValue(mInitialValue);
    }
}
