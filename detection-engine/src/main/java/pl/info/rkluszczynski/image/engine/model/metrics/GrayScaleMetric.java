package pl.info.rkluszczynski.image.engine.model.metrics;

import pl.info.rkluszczynski.image.core.compare.metric.CompareMetric;

import java.awt.*;

abstract class GrayScaleMetric implements CompareMetric {

    int getGrayScaleAverageValue(Color colorPixel) {
        int grayValue = colorPixel.getRed();
        grayValue += colorPixel.getGreen();
        grayValue += colorPixel.getBlue();
        return grayValue / 3;
    }
}
