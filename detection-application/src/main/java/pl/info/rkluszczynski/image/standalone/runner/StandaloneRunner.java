package pl.info.rkluszczynski.image.standalone.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.info.rkluszczynski.image.core.compare.metric.CompareMetric;
import pl.info.rkluszczynski.image.engine.model.SessionData;
import pl.info.rkluszczynski.image.engine.model.comparators.PatternMatchComparator;
import pl.info.rkluszczynski.image.engine.model.comparators.PixelDifferenceComparator;
import pl.info.rkluszczynski.image.engine.model.comparators.SequenceComparator;
import pl.info.rkluszczynski.image.engine.model.strategies.BestLocalizedMatchStrategy;
import pl.info.rkluszczynski.image.engine.model.strategies.PatternMatchStrategy;
import pl.info.rkluszczynski.image.engine.tasks.MultiScaleStageTask;
import pl.info.rkluszczynski.image.engine.tasks.input.DetectorTaskInput;
import pl.info.rkluszczynski.image.standalone.db.entities.PlanogramEntity;
import pl.info.rkluszczynski.image.standalone.db.entities.ProcessedImageEntity;
import pl.info.rkluszczynski.image.standalone.db.repositories.PlanogramRepository;
import pl.info.rkluszczynski.image.standalone.db.repositories.ProcessedImageRepository;

import java.io.IOException;
import java.util.List;

@Component(value = "mainRunner")
public class StandaloneRunner {
    private static final Logger logger = LoggerFactory.getLogger(StandaloneRunner.class);

    private static final int NEW_IMAGE_STATUS = 0;

    @Autowired
    private ProcessedImageRepository processedImageRepository;
    @Autowired
    private PlanogramRepository planogramRepository;


    public void run() throws IOException {
        List<ProcessedImageEntity> imageEntities = processedImageRepository.findByStatus(NEW_IMAGE_STATUS);
        for (ProcessedImageEntity entity : imageEntities) {
            logger.debug("Processing entity {}", entity);

            PlanogramEntity planogramEntity = planogramRepository.findOne(
                    Long.valueOf(entity.getPlanogramId())
            );

            String planogramPath = planogramEntity.getPath();
            String entitySourcePath = entity.getSourcepath();
            String entityResultPath = entity.getProcessedpath();

        }
    }


    private SessionData createSessionData() {
        SessionData sessionData = new SessionData(null);
        return sessionData;
    }

    private Runnable createMultiScaleTask(SessionData sessionData, CompareMetric metric) {
        PatternMatchStrategy matchStrategy = new BestLocalizedMatchStrategy();
        PatternMatchComparator matchComparator = new SequenceComparator(
                new PixelDifferenceComparator(metric)
        );
        DetectorTaskInput detectorTaskInput = new DetectorTaskInput(matchComparator, matchStrategy);
        return new MultiScaleStageTask(sessionData, detectorTaskInput);
    }
}
