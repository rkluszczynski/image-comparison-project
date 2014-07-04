package pl.info.rkluszczynski.image.engine.model.strategies;

import pl.info.rkluszczynski.image.engine.tasks.PatternDetectorTask;
import pl.info.rkluszczynski.image.engine.tasks.input.DetectorTaskInput;

/**
 * Created by Rafal on 2014-05-27.
 */
public interface PatternMatchStrategy {

    void initialize(DetectorTaskInput taskInput);

    void putScore(MatchScore matchScore);

    void applyBestScores(PatternDetectorTask detectorTask, DetectorTaskInput taskInput);

}
