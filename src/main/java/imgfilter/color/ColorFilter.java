package imgfilter.color;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ColorFilter {

    private final IntegerProperty mMin;
    private final IntegerProperty mMax;

    public ColorFilter(ColorSpectrum filterType) {
        mMin = new SimpleIntegerProperty(filterType.range().start);
        mMax = new SimpleIntegerProperty(filterType.range().end);
    }

    public IntegerProperty minProperty() {
        return mMin;
    }

    public IntegerProperty maxProperty() {
        return mMax;
    }
}
