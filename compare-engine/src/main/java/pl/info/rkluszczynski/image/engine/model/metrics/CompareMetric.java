package pl.info.rkluszczynski.image.engine.model.metrics;

import java.awt.*;

public interface CompareMetric {

    public void resetValue();

    public double calculateValue();

    public void addPixelsDifference(Color inputPixel, Color templatePixel);

    public String getName();

}
