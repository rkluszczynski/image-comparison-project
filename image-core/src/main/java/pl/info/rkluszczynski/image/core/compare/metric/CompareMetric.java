package pl.info.rkluszczynski.image.core.compare.metric;

import java.awt.*;

public interface CompareMetric {

    public void resetValue();

    public double calculateValue();

    public double maxValue();

    public void addPixelsDifference(Color inputPixel, Color templatePixel);

    public void addPointDifference(double value1, double value2);

    public String getName();

}
