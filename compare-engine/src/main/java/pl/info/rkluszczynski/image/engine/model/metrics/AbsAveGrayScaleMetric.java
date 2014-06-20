package pl.info.rkluszczynski.image.engine.model.metrics;

import pl.info.rkluszczynski.image.engine.config.EngineConstants;

import java.awt.*;

public class AbsAveGrayScaleMetric extends GrayScaleMetric {
    private double metricValue;
    private double pixelsNumber;

    @Override
    public void resetValue() {
        metricValue = 0.;
        pixelsNumber = 0.;
    }

    @Override
    public double calculateValue() {
        return (metricValue / pixelsNumber) / maxValue();
    }

    @Override
    public double maxValue() {
        return EngineConstants.MAX_PIXEL_VALUE;
    }

    @Override
    public void addPixelsDifference(Color inputPixel, Color templatePixel) {
        double grayScaleInputValue = getGrayScaleAverageValue(inputPixel);
        double grayScaleTemplateValue = getGrayScaleAverageValue(templatePixel);

        metricValue += Math.abs(grayScaleTemplateValue - grayScaleInputValue);
        ++pixelsNumber;
    }

    @Override
    public String getName() {
        return "ABS_GS";
    }
}
