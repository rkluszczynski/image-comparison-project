package pl.info.rkluszczynski.image.engine.model.metrics;

import java.awt.*;

import static pl.info.rkluszczynski.image.engine.config.EngineConstants.MAX_PIXEL_VALUE;

public class PSNRAveGrayScaleMetric extends GrayScaleMetric {
    private static double pixelMaxValueSquared = MAX_PIXEL_VALUE * MAX_PIXEL_VALUE;

    private double mseMetricValue;
    private double valuesAmount;

    @Override
    public void resetValue() {
        mseMetricValue = 0.;
        valuesAmount = 0.;
    }

    @Override
    public double calculateValue() {
//        return 20. * Math.log10(MAX_PIXEL_VALUE) - 10. * Math.log10(mseMetricValue / valuesAmount);
//        return 20. * Math.log10(MAX_PIXEL_VALUE / Math.sqrt(mseMetricValue / valuesAmount));
        double mse = mseMetricValue / valuesAmount;
        mse = Math.max(mse, 1.); // in case there is 0
        double psnr = 10. * Math.log10(pixelMaxValueSquared / mse);
        return (maxValue() - psnr) / maxValue();
    }

    @Override
    public double maxValue() {
        return 10. * Math.log10(pixelMaxValueSquared);
    }

    @Override
    public void addPixelsDifference(Color inputPixel, Color templatePixel) {
        int grayScaleInputValue = getGrayScaleAverageValue(inputPixel);
        int grayScaleTemplateValue = getGrayScaleAverageValue(templatePixel);
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
        return "PSNR_GS";
    }
}
