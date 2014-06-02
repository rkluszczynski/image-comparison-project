package pl.info.rkluszczynski.image.engine.model.comparators;

import pl.info.rkluszczynski.image.engine.tasks.DetectorTaskInput;

import java.awt.image.BufferedImage;

/**
 * Created by Rafal on 2014-06-01.
 */
public interface PatternMatchComparator {

    void initialize(DetectorTaskInput taskInput);

    double calculatePatternMatchScore(
            BufferedImage image,
            int widthPosition,
            int heightPosition
    );
}
