package pl.info.rkluszczynski.image.engine.model.comparators;

import pl.info.rkluszczynski.image.engine.model.metrics.Metric;
import pl.info.rkluszczynski.image.engine.utils.BufferedImageWrapper;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PixelDifferenceComparator extends AbstractPatternComparator {

    @Override
    public double calculatePatternMatchScore(BufferedImage image, int widthPosition, int heightPosition) {
        BufferedImageWrapper patternWrapper = taskInput.getPatternWrapper();
        Metric metric = taskInput.getMetric();

        assert metric != null;
        metric.resetValue();
        for (int piw = 0; piw < patternWrapper.getWidth(); ++piw) {
            for (int pih = 0; pih < patternWrapper.getHeight(); ++pih) {
                if (!patternWrapper.treatPixelAsAlpha(piw, pih)) {
                    Color scaledInputImagePixelValue = new Color(
                            image.getRGB(widthPosition + piw, heightPosition + pih)
                    );

                    metric.addPixelsDifference(
                            scaledInputImagePixelValue,
                            patternWrapper.getPixelColor(piw, pih)
                    );
                }
            }
        }
        return metric.calculateValue();
    }
}
