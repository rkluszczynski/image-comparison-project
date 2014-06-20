package pl.info.rkluszczynski.image.engine.model.metrics;

import pl.info.rkluszczynski.image.engine.config.EngineConstants;

import java.awt.*;

public class RMSEAveGrayScaleMetric extends GrayScaleMetric {
    private double mseMetricValue;
    private double pixelsNumber;

    @Override
    public void resetValue() {
        mseMetricValue = 0.;
        pixelsNumber = 0.;
    }

    @Override
    public double calculateValue() {
        return Math.sqrt(mseMetricValue / pixelsNumber) / maxValue();
    }

    @Override
    public double maxValue() {
        return EngineConstants.MAX_PIXEL_VALUE;
    }

    @Override
    public void addPixelsDifference(Color inputPixel, Color templatePixel) {
        double grayScaleInputValue = getGrayScaleAverageValue(inputPixel);
        double grayScaleTemplateValue = getGrayScaleAverageValue(templatePixel);

        double diff = (grayScaleTemplateValue - grayScaleInputValue);
        mseMetricValue += (diff * diff);
        ++pixelsNumber;
    }

    @Override
    public String getName() {
        return "RMSE_GS";
    }
}
