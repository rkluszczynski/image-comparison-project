package pl.info.rkluszczynski.image.engine.model.comparators;

import pl.info.rkluszczynski.image.compare.metric.CompareMetric;
import pl.info.rkluszczynski.image.engine.tasks.input.DetectorTaskInput;

/**
 * Created by Rafal on 2014-06-01.
 */
abstract class AbstractMatchComparator implements PatternMatchComparator {

    private final CompareMetric metric;
    private DetectorTaskInput taskInput;

    public AbstractMatchComparator(CompareMetric metric) {
        this.metric = metric;
    }

    @Override
    public void initialize(DetectorTaskInput taskInput) {
        this.taskInput = taskInput;
    }

    public DetectorTaskInput getTaskInput() {
        return taskInput;
    }

    public CompareMetric getMetric() {
        return metric;
    }

//    private Metric createMetricInstance() {
//        try {
//            Type type = this
//                    .getClass()
//                    .getGenericSuperclass();
//            ParameterizedType paramType = (ParameterizedType) type;
//            Class<T> clazz = (Class<T>) paramType
//                    .getActualTypeArguments()[0];
//            return clazz.newInstance();
//        } catch (InstantiationException | IllegalAccessException e) {
//            logger.error("Error creating metric instance", e);
//            return null;
//        }
//    }
}
