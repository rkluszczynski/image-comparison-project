package pl.info.rkluszczynski.image.engine.tasks.metric;

import java.awt.*;

public interface Metric {

    public double calculateValue();

    public void addPixelsDifference(Color inputPixel, Color templatePixel);

}
