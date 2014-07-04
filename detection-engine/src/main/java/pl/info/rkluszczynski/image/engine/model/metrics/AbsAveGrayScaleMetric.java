package pl.info.rkluszczynski.image.engine.model.metrics;

import pl.info.rkluszczynski.image.engine.config.EngineConstants;

import java.awt.*;

public class AbsAveGrayScaleMetric extends GrayScaleMetric {
    private double metricValue;
    private double valuesAmount;

    @Override
    public void resetValue() {
        metricValue = 0.;
        valuesAmount = 0.;
    }

    @Override
    public double calculateValue() {
        return (metricValue / valuesAmount) / maxValue();
    }

    @Override
    public double maxValue() {
        return EngineConstants.MAX_PIXEL_VALUE;
    }

    @Override
    public void addPixelsDifference(Color inputPixel, Color templatePixel) {
        double grayScaleInputValue = getGrayScaleAverageValue(inputPixel);
        double grayScaleTemplateValue = getGrayScaleAverageValue(templatePixel);

        addPointDifference(grayScaleInputValue, grayScaleTemplateValue);
    }

    @Override
    public void addPointDifference(double value1, double value2) {
        metricValue += Math.abs(value1 - value2);
        ++valuesAmount;
    }

    @Override
    public String getName() {
        return "ABS_GS";
    }
}
