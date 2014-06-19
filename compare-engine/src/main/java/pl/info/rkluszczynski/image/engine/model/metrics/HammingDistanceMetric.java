package pl.info.rkluszczynski.image.engine.model.metrics;

import pl.info.rkluszczynski.image.core.compare.metric.CompareMetric;

import java.awt.*;

public class HammingDistanceMetric implements CompareMetric {
    private int differentPixels;
    private int pixelsNumber;

    @Override
    public void resetValue() {
        differentPixels = 0;
        pixelsNumber = 0;
    }

    @Override
    public double calculateValue() {
        return (double) differentPixels / (3. * pixelsNumber);
    }

    @Override
    public void addPixelsDifference(Color inputPixel, Color templatePixel) {
        differentPixels += (inputPixel.getRed() == templatePixel.getRed()) ? 0 : 1;
        differentPixels += (inputPixel.getGreen() == templatePixel.getGreen()) ? 0 : 1;
        differentPixels += (inputPixel.getBlue() == templatePixel.getBlue()) ? 0 : 1;
        ++pixelsNumber;
    }

    @Override
    public String getName() {
        return "HammingDistance";
    }
}
