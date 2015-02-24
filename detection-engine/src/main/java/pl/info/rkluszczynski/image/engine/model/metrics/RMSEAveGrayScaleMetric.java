package pl.info.rkluszczynski.image.engine.model.metrics;

import pl.info.rkluszczynski.image.engine.config.EngineConstants;

import java.awt.*;

public class RMSEAveGrayScaleMetric extends GrayScaleMetric {
    private double mseMetricValue;
    private double valuesAmount;

    @Override
    public void resetValue() {
        mseMetricValue = 0.;
        valuesAmount = 0.;
    }

    @Override
    public double calculateValue() {
        return Math.sqrt(mseMetricValue / valuesAmount) / maxValue();
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
        double diff = (value1 - value2);
        mseMetricValue += (diff * diff);
        ++valuesAmount;
    }

    @Override
    public String getName() {
        return "RMSE_GS";
    }
}
