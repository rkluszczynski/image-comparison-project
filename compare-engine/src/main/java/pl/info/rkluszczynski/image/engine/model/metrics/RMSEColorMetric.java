package pl.info.rkluszczynski.image.engine.model.metrics;

import pl.info.rkluszczynski.image.core.compare.metric.CompareMetric;
import pl.info.rkluszczynski.image.engine.config.EngineConstants;

import java.awt.*;

public class RMSEColorMetric implements CompareMetric {
    private double metricValue;
    private double valuesAmount;

    @Override
    public void resetValue() {
        metricValue = 0.;
        valuesAmount = 0.;
    }

    @Override
    public double calculateValue() {
        return Math.sqrt(metricValue / valuesAmount) / maxValue();
    }

    @Override
    public double maxValue() {
        return EngineConstants.MAX_PIXEL_VALUE;
    }

    @Override
    public void addPixelsDifference(Color inputPixel, Color templatePixel) {
        addPointDifference(inputPixel.getRed(), templatePixel.getRed());
        addPointDifference(inputPixel.getGreen(), templatePixel.getGreen());
        addPointDifference(inputPixel.getBlue(), templatePixel.getBlue());
    }

    @Override
    public void addPointDifference(double value1, double value2) {
        metricValue += ((value1 - value2) * (value1 - value2));
        ++valuesAmount;
    }

    @Override
    public String getName() {
        return "RMSE";
    }
}
