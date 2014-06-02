package pl.info.rkluszczynski.image.engine.model.metrics;

import java.awt.*;

import static pl.info.rkluszczynski.image.engine.config.EngineConstants.MAX_PIXEL_VALUE;

public class PSNRColorMetric implements Metric {
    private double mseMetricValue;
    private double pixelMaxValue;
    private double pixelsNumber;

    @Override
    public void resetValue() {
        mseMetricValue = 0.;
        pixelMaxValue = MAX_PIXEL_VALUE;
        pixelsNumber = 0.;
    }

    @Override
    public double calculateValue() {
        return 20. * Math.log10(pixelMaxValue) - 10. * Math.log10(mseMetricValue / (3. * pixelsNumber));
    }

    @Override
    public void addPixelsDifference(Color inputPixel, Color templatePixel) {
        mseMetricValue += ((inputPixel.getRed() - templatePixel.getRed()) * (inputPixel.getRed() - templatePixel.getRed()));
        mseMetricValue += ((inputPixel.getGreen() - templatePixel.getGreen()) * (inputPixel.getGreen() - templatePixel.getGreen()));
        mseMetricValue += ((inputPixel.getBlue() - templatePixel.getBlue()) * (inputPixel.getBlue() - templatePixel.getBlue()));
        ++pixelsNumber;
    }

    @Override
    public String getName() {
        return "PSNR";
    }
}
