package pl.info.rkluszczynski.image.engine.tasks.metric;

import java.awt.*;

public class AbsMetric implements Metric {

    private double metricValue = 0.;

    @Override
    public double calculateValue() {
        return metricValue / 3;
    }

    @Override
    public void addPixelsDifference(Color inputPixel, Color templatePixel) {
        metricValue += Math.abs(inputPixel.getRed() - templatePixel.getRed());
        metricValue += Math.abs(inputPixel.getGreen() - templatePixel.getGreen());
        metricValue += Math.abs(inputPixel.getBlue() - templatePixel.getBlue());
    }
}
