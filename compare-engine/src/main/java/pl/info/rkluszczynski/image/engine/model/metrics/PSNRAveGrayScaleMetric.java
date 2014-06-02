package pl.info.rkluszczynski.image.engine.model.metrics;

import java.awt.*;

public class PSNRAveGrayScaleMetric extends GrayScaleMetric {
    private final double pixelMaxValue = 256.;

    private double mseMetricValue;
    private double pixelsNumber;

    @Override
    public void resetValue() {
        mseMetricValue = 0.;
        pixelsNumber = 0.;
    }

    @Override
    public double calculateValue() {
        return 20. * Math.log10(pixelMaxValue) - 10. * Math.log10(mseMetricValue / pixelsNumber);
    }

    @Override
    public void addPixelsDifference(Color inputPixel, Color templatePixel) {
        int grayScaleInputValue = getGrayScaleAverageValue(inputPixel);
        int grayScaleTemplateValue = getGrayScaleAverageValue(templatePixel);
        double diff = (grayScaleTemplateValue - grayScaleInputValue);

        mseMetricValue += (diff * diff);
        pixelsNumber += 1.;
    }

    @Override
    public String getName() {
        return "PSNR_GS";
    }
}
