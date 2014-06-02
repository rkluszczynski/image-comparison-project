package pl.info.rkluszczynski.image.engine.model.comparators;

import pl.info.rkluszczynski.image.engine.tasks.DetectorTaskInput;

/**
 * Created by Rafal on 2014-06-01.
 */
abstract class AbstractPatternComparator implements PatternMatchComparator {
    DetectorTaskInput taskInput;

    @Override
    public void initialize(DetectorTaskInput taskInput) {
        this.taskInput = taskInput;
    }
}
