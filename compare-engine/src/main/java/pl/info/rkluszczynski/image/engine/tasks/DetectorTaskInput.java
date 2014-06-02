package pl.info.rkluszczynski.image.engine.tasks;

import pl.info.rkluszczynski.image.engine.model.comparators.PatternMatchComparator;
import pl.info.rkluszczynski.image.engine.model.metrics.Metric;
import pl.info.rkluszczynski.image.engine.model.strategies.PatternMatchStrategy;
import pl.info.rkluszczynski.image.engine.utils.BufferedImageWrapper;

import java.awt.image.BufferedImage;

/**
 * Created by Rafal on 2014-06-01.
 */
public class DetectorTaskInput {
    private final Metric metric;
    private final PatternMatchComparator comparator;
    private final PatternMatchStrategy strategy;

    private BufferedImageWrapper patternWrapper;
    private BufferedImage resultImage;


    public DetectorTaskInput(Metric metric, PatternMatchComparator comparator, PatternMatchStrategy strategy) {
        this.metric = metric;
        this.comparator = comparator;
        this.strategy = strategy;
    }


    public Metric getMetric() {
        return metric;
    }

    public PatternMatchComparator getComparator() {
        return comparator;
    }

    public PatternMatchStrategy getStrategy() {
        return strategy;
    }

    public BufferedImage getResultImage() {
        return resultImage;
    }

    public BufferedImageWrapper getPatternWrapper() {
        return patternWrapper;
    }


    public void setPatternWrapper(BufferedImageWrapper patternWrapper) {
        this.patternWrapper = patternWrapper;
    }

    public void setResultImage(BufferedImage resultImage) {
        this.resultImage = resultImage;
    }
}
