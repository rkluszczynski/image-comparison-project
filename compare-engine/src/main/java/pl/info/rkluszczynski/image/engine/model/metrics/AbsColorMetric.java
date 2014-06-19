package pl.info.rkluszczynski.image.engine.model.metrics;

import pl.info.rkluszczynski.image.core.compare.metric.CompareMetric;

import java.awt.*;

public class AbsColorMetric implements CompareMetric {
    private double metricValue;

    @Override
    public void resetValue() {
        metricValue = 0.;
    }

    @Override
    public double calculateValue() {
        return metricValue / 3.;
    }

    @Override
    public void addPixelsDifference(Color inputPixel, Color templatePixel) {
        metricValue += Math.abs(inputPixel.getRed() - templatePixel.getRed());
        metricValue += Math.abs(inputPixel.getGreen() - templatePixel.getGreen());
        metricValue += Math.abs(inputPixel.getBlue() - templatePixel.getBlue());
    }

    @Override
    public String getName() {
        return "ABS";
    }
}
