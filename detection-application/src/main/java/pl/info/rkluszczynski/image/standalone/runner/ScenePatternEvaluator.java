package pl.info.rkluszczynski.image.standalone.runner;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import pl.info.rkluszczynski.image.engine.model.BufferedImageMarker;
import pl.info.rkluszczynski.image.engine.model.SessionData;
import pl.info.rkluszczynski.image.engine.tasks.MultiScaleStageTask;
import pl.info.rkluszczynski.image.engine.tasks.input.TasksProperties;
import pl.info.rkluszczynski.image.standalone.db.entities.EvaluationEntity;
import pl.info.rkluszczynski.image.standalone.db.entities.EvaluationImageEntity;
import pl.info.rkluszczynski.image.standalone.db.entities.EvaluationMarkerEntity;
import pl.info.rkluszczynski.image.standalone.db.repositories.EvaluationImageRepository;
import pl.info.rkluszczynski.image.standalone.db.repositories.EvaluationMarkerRepository;
import pl.info.rkluszczynski.image.standalone.db.repositories.EvaluationRepository;
import pl.info.rkluszczynski.image.standalone.exception.CouldNotReadImageFileException;
import pl.info.rkluszczynski.image.standalone.exception.StandaloneApplicationException;

import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.List;

import static pl.info.rkluszczynski.image.standalone.db.ImageProcessingStatus.FAILED;
import static pl.info.rkluszczynski.image.standalone.db.ImageProcessingStatus.NEW;

@Component(value = "scenePatternEvaluator")
public class ScenePatternEvaluator implements StandaloneRunner {
    private static final Logger logger = LoggerFactory.getLogger(ScenePatternEvaluator.class);

    private final String fileSeparator = System.getProperty("file.separator");

    @Autowired
    private EvaluationRepository evaluationRepository;
    @Autowired
    private EvaluationImageRepository imageRepository;
    @Autowired
    private EvaluationMarkerRepository markerRepository;
    @Autowired
    private Environment env;
    @Autowired
    private TasksProperties tasksProperties;

    @Override
    public void run() throws StandaloneApplicationException {
        String markerImagesDirectory = env.getProperty("marker.images.directory", ".") + fileSeparator;
        String sourceImagesDirectory = env.getProperty("source.images.directory", ".") + fileSeparator;
        String outputImagesDirectory = env.getProperty("output.images.directory", ".") + fileSeparator;

        List<EvaluationEntity> evaluationEntities = evaluationRepository.findByStatus(NEW.getCode());
        if (evaluationEntities.size() == 0) {
            logger.info("No entities with NEW status.");
            return;
        }

        for (EvaluationEntity entity : evaluationEntities) {
            long evaluationId = entity.getId();
            logger.info("Processing evaluation: {}", entity);

            List<EvaluationMarkerEntity> markerList = markerRepository.findByEvaluationId(evaluationId);

            if (markerList.size() > 0) {
                logger.info("Found {} markers for evaluation (id={})", markerList.size(), evaluationId);

                List<EvaluationImageEntity> imageList = imageRepository.findByEvaluationId(evaluationId);
                logger.info("Found {} images for evaluation (id={})", imageList.size(), evaluationId);
                for (EvaluationImageEntity imageEntity : imageList) {
                    BufferedImage imageScene = ImagePatternDetector.readImageFromFile(
                            sourceImagesDirectory + fileSeparator + imageEntity.getSourcePath());
                    List<BufferedImageMarker> markers = prepareImageMarkers(markerList, markerImagesDirectory);

                    processOneImageScene(imageScene, markers);
                }
            } else {
                logger.warn("No markers for evaluation (id {})!", evaluationId);
                Date startDate = entity.getStartDate();
                entity.setStartDate(new Date());
                if (startDate != null) {
                    entity.setStatus(FAILED.getCode());
                    entity.setEndDate(new Date());

                    logger.warn("Evaluation (id {}) FAILED", evaluationId);
                } else {
                    logger.warn("Set startDate for evaluation (id {}). "
                            + "If next time markers will be missing, evaluation will be set as FAILED."
                            , evaluationId);
                }
                evaluationRepository.save(entity);
            }
            break;
        }
    }


    private void processOneImageScene(BufferedImage imageScene, List<BufferedImageMarker> markers) {
        for (BufferedImageMarker marker : markers) {
            SessionData sessionData = ImagePatternDetector.createSessionData();
            sessionData.setInputImage(imageScene);
            sessionData.setTemplateImage(marker.getImageMarker());

            MultiScaleStageTask task = ImagePatternDetector.createMultiScaleTask(
                    sessionData, ImagePatternDetector.ABS_COLOR_METRIC);
//            setImageEntityStatus(STARTED, entity);
            task.initialize(tasksProperties);
            task.run();

//            String entityResultPath = createEntityOutputPath(outputImagesDirectory, entity);
//            logger.info(" entityResultPath : {}", entityResultPath);
            BufferedImage resultImage = sessionData.getResultImage();
//            saveBufferedImageAsFile(resultImage, entityResultPath);
//
//            setImageEntityStatus(DONE, entity);
        }

//        try {
//        } catch (StandaloneApplicationException e) {
//            logger.error("Error during processing entity: {}", entity, e);
//            setImageEntityStatus(FAILED, entity);
//        }
    }

    private List<BufferedImageMarker> prepareImageMarkers(List<EvaluationMarkerEntity> markerList, String directory)
            throws CouldNotReadImageFileException {
        List<BufferedImageMarker> imageMarkers = Lists.newArrayList();
        for (EvaluationMarkerEntity entity : markerList) {
            String markerAbsolutePath = directory + fileSeparator + entity.getPath();
            try {
                BufferedImage image = ImagePatternDetector.readImageFromFile(markerAbsolutePath);
                imageMarkers.add(
                        new BufferedImageMarker(image, entity.getToBeFound().intValue())
                );
            } catch (CouldNotReadImageFileException e) {
                String message = "Could not read marker file: " + markerAbsolutePath;
                throw new CouldNotReadImageFileException(message, e);
            }
        }
        return imageMarkers;
    }
}
