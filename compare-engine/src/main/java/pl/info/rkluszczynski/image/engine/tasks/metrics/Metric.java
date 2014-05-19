package pl.info.rkluszczynski.image.engine.tasks.metrics;

import java.awt.*;

public interface Metric {

    public void resetValue();

    public double calculateValue();

    public void addPixelsDifference(Color inputPixel, Color templatePixel);

    public String getName();

}
