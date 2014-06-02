package pl.info.rkluszczynski.image.engine.model.comparators;

import com.google.common.collect.Lists;
import pl.info.rkluszczynski.image.engine.tasks.input.DetectorTaskInput;

import java.awt.image.BufferedImage;
import java.util.Collection;

/**
 * Created by Rafal on 2014-06-02.
 */
public class SequenceComparator extends AbstractMatchComparator {
    private final Collection<PatternMatchComparator> matchComparators;

    public SequenceComparator(PatternMatchComparator... matchComparators) {
        super(null);
        this.matchComparators = Lists.newArrayList(matchComparators);
    }

    @Override
    public void initialize(DetectorTaskInput taskInput) {
        super.initialize(taskInput);
        for (PatternMatchComparator comparator : matchComparators) {
            comparator.initialize(taskInput);
        }
    }

    @Override
    public double calculatePatternMatchScore(BufferedImage image, int widthPosition, int heightPosition) {
        double scoreSum = 0.;
        for (PatternMatchComparator comparator : matchComparators) {
            double score = comparator.calculatePatternMatchScore(image, widthPosition, heightPosition);
            if (score < 0.) {
                return -1.;
            }
            scoreSum += score;
        }
        return scoreSum;
    }
}
