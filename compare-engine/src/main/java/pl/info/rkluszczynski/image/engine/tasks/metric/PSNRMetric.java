package pl.info.rkluszczynski.image.engine.tasks.metric;

import java.awt.*;

public class PSNRMetric implements Metric {

    private double mseMetricValue = 0.;
    private double pixelMaxValue = 0.;

    @Override
    public double calculateValue() {
        return 20. * Math.log10(pixelMaxValue) - 10. * Math.log10(mseMetricValue);
    }

    @Override
    public void addPixelsDifference(Color inputPixel, Color templatePixel) {
        mseMetricValue += ((inputPixel.getRed() - templatePixel.getRed()) * (inputPixel.getRed() - templatePixel.getRed()));
        mseMetricValue += ((inputPixel.getGreen() - templatePixel.getGreen()) * (inputPixel.getGreen() - templatePixel.getGreen()));
        mseMetricValue += ((inputPixel.getBlue() - templatePixel.getBlue()) * (inputPixel.getBlue() - templatePixel.getBlue()));

        pixelMaxValue = Math.max(pixelMaxValue, inputPixel.getRed());
        pixelMaxValue = Math.max(pixelMaxValue, inputPixel.getGreen());
        pixelMaxValue = Math.max(pixelMaxValue, inputPixel.getBlue());

        pixelMaxValue = Math.max(pixelMaxValue, templatePixel.getRed());
        pixelMaxValue = Math.max(pixelMaxValue, templatePixel.getGreen());
        pixelMaxValue = Math.max(pixelMaxValue, templatePixel.getBlue());
    }
}
