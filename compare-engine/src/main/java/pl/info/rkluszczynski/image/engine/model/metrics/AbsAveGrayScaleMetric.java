package pl.info.rkluszczynski.image.engine.model.metrics;

import java.awt.*;

public class AbsAveGrayScaleMetric extends GrayScaleMetric {
    private double metricValue;

    @Override
    public void resetValue() {
        metricValue = 0.;
    }

    @Override
    public double calculateValue() {
        return metricValue;
    }

    @Override
    public void addPixelsDifference(Color inputPixel, Color templatePixel) {
        double grayScaleInputValue = getGrayScaleAverageValue(inputPixel);
        double grayScaleTemplateValue = getGrayScaleAverageValue(templatePixel);

        metricValue += Math.abs(grayScaleTemplateValue - grayScaleInputValue);
    }

    @Override
    public String getName() {
        return "ABS_GS";
    }
}
