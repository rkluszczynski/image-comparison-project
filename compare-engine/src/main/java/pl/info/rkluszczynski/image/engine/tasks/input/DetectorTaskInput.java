package pl.info.rkluszczynski.image.engine.tasks.input;

import pl.info.rkluszczynski.image.engine.model.comparators.PatternMatchComparator;
import pl.info.rkluszczynski.image.engine.model.strategies.PatternMatchStrategy;
import pl.info.rkluszczynski.image.engine.tasks.multiscale.QueryImageWrapper;
import pl.info.rkluszczynski.image.engine.utils.BufferedImageWrapper;

import java.awt.image.BufferedImage;

/**
 * Created by Rafal on 2014-06-01.
 */
public class DetectorTaskInput {
    private final PatternMatchComparator comparator;
    private final PatternMatchStrategy strategy;

    private BufferedImageWrapper patternWrapper;
    private BufferedImage resultImage;
    private QueryImageWrapper queryImageWrapper;

    public DetectorTaskInput(PatternMatchComparator comparator, PatternMatchStrategy strategy) {
        this.comparator = comparator;
        this.strategy = strategy;
    }

    public void initialize() {
        comparator.initialize(this);
        strategy.initialize(this);
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

    public void setResultImage(BufferedImage resultImage) {
        this.resultImage = resultImage;
    }

    public BufferedImageWrapper getPatternWrapper() {
        return patternWrapper;
    }

    public void setPatternWrapper(BufferedImageWrapper patternWrapper) {
        this.patternWrapper = patternWrapper;
    }

    public QueryImageWrapper getQueryImageWrapper() {
        return queryImageWrapper;
    }

    public void setQueryImageWrapper(QueryImageWrapper queryImageWrapper) {
        this.queryImageWrapper = queryImageWrapper;
    }
}
