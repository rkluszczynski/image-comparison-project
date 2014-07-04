package pl.info.rkluszczynski.image.engine.model.metrics;

import pl.info.rkluszczynski.image.core.compare.metric.CompareMetric;

import java.awt.*;

import static pl.info.rkluszczynski.image.engine.config.EngineConstants.MAX_PIXEL_VALUE;

/*
    http://www.mathworks.com/help/vision/ref/psnr.html
 */
public class PSNRColorMetric implements CompareMetric {
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
//        return 20. * Math.log10(pixelMaxValueSquared) - 10. * Math.log10(mseMetricValue / (3. * valuesAmount));
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
        addPointDifference(inputPixel.getRed(), templatePixel.getRed());
        addPointDifference(inputPixel.getGreen(), templatePixel.getGreen());
        addPointDifference(inputPixel.getBlue(), templatePixel.getBlue());
    }

    @Override
    public void addPointDifference(double value1, double value2) {
        mseMetricValue += ((value1 - value2) * (value1 - value2));
        ++valuesAmount;
    }

    @Override
    public String getName() {
        return "PSNR";
    }
}
