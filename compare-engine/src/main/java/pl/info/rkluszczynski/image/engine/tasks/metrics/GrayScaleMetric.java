package pl.info.rkluszczynski.image.engine.tasks.metrics;

import java.awt.*;

abstract
public class GrayScaleMetric implements Metric {

    public int getGrayScaleAverageValue(Color colorPixel) {
        int grayValue = colorPixel.getRed();
        grayValue += colorPixel.getGreen();
        grayValue += colorPixel.getBlue();
        return grayValue / 3;
    }
}
