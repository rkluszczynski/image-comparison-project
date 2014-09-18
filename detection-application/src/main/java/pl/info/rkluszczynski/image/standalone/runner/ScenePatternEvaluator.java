package pl.info.rkluszczynski.image.standalone.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.info.rkluszczynski.image.standalone.db.entities.EvaluationEntity;
import pl.info.rkluszczynski.image.standalone.db.repositories.EvaluationImageRepository;
import pl.info.rkluszczynski.image.standalone.db.repositories.EvaluationMarkerRepository;
import pl.info.rkluszczynski.image.standalone.db.repositories.EvaluationRepository;
import pl.info.rkluszczynski.image.standalone.exception.StandaloneApplicationException;

import java.util.List;

import static pl.info.rkluszczynski.image.standalone.db.ProcessedImageStatus.NEW;

@Component
public class ScenePatternEvaluator implements StandaloneRunner {
    private static final Logger logger = LoggerFactory.getLogger(ScenePatternEvaluator.class);

    @Autowired
    private EvaluationRepository evaluationRepository;
    @Autowired
    private EvaluationImageRepository imageRepository;
    @Autowired
    private EvaluationMarkerRepository markerRepository;

    @Override
    public void run() throws StandaloneApplicationException {
        List<EvaluationEntity> evaluationEntities = evaluationRepository.findByStatus(NEW.getCode());
        if (evaluationEntities.size() == 0) {
            logger.info("No entities with NEW status.");
            return;
        }


        for (EvaluationEntity entity : evaluationEntities) {

        }


    }
}
