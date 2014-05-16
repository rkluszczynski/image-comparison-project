package pl.info.rkluszczynski.image.engine.tasks.metric;

import java.awt.*;

public class AbsMetric implements Metric {

    private double metricValue = 0.;

    @Override
    public double calculateValue() {
        return metricValue;
    }

    @Override
    public void addPixelsDifference(Color inputPixel, Color templatePixel) {
        metricValue += (Math.abs(inputPixel.getRed() - templatePixel.getRed()) / 3.);
        metricValue += (Math.abs(inputPixel.getGreen() - templatePixel.getGreen()) / 3.);
        metricValue += (Math.abs(inputPixel.getBlue() - templatePixel.getBlue()) / 3.);
    }
}
