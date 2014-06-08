package pl.info.rkluszczynski.image.engine.model.comparators;

import pl.info.rkluszczynski.image.compare.metric.CompareMetric;
import pl.info.rkluszczynski.image.engine.tasks.input.DetectorTaskInput;
import pl.info.rkluszczynski.image.engine.utils.BufferedImageWrapper;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePHashComparator extends AbstractMatchComparator {

    public ImagePHashComparator(CompareMetric metric) {
        super(metric);
    }

    @Override
    public double calculatePatternMatchScore(BufferedImage image, int widthPosition, int heightPosition) {
        CompareMetric metric = getMetric();
        DetectorTaskInput taskInput = getTaskInput();
        BufferedImageWrapper patternWrapper = taskInput.getPatternWrapper();

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
