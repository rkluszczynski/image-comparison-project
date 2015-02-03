package pl.info.rkluszczynski.image.standalone.runner;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import pl.info.rkluszczynski.image.engine.model.BufferedImageMarker;
import pl.info.rkluszczynski.image.engine.model.SessionData;
import pl.info.rkluszczynski.image.engine.model.strategies.BestLocalizedMatchStrategy;
import pl.info.rkluszczynski.image.engine.model.strategies.MatchScore;
import pl.info.rkluszczynski.image.engine.tasks.MultiScaleStageTask;
import pl.info.rkluszczynski.image.engine.tasks.input.DetectorTaskInput;
import pl.info.rkluszczynski.image.engine.tasks.input.TasksProperties;
import pl.info.rkluszczynski.image.engine.utils.DrawHelper;
import pl.info.rkluszczynski.image.standalone.db.ProcessingStatus;
import pl.info.rkluszczynski.image.standalone.db.entities.EvaluationEntity;
import pl.info.rkluszczynski.image.standalone.db.entities.EvaluationImageEntity;
import pl.info.rkluszczynski.image.standalone.db.entities.EvaluationMarkerEntity;
import pl.info.rkluszczynski.image.standalone.db.repositories.EvaluationImageRepository;
import pl.info.rkluszczynski.image.standalone.db.repositories.EvaluationMarkerRepository;
import pl.info.rkluszczynski.image.standalone.db.repositories.EvaluationRepository;
import pl.info.rkluszczynski.image.standalone.exception.CouldNotReadImageFileException;
import pl.info.rkluszczynski.image.standalone.exception.StandaloneApplicationException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static pl.info.rkluszczynski.image.standalone.db.ProcessingStatus.*;

@Component(value = "scenePatternEvaluator")
public class ScenePatternEvaluator implements StandaloneRunner {
    private static final Logger logger = LoggerFactory.getLogger(ScenePatternEvaluator.class);

    private static final Color DEFAULT_MARKER_COLOR = Color.BLACK;

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
            logger.info("No entities with status {}.", NEW.name());
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

                setEvaluationStatus(STARTED, entity);
                List<Integer> resultScores = Lists.newArrayList();
                try {
                    for (EvaluationImageEntity imageEntity : imageList) {
                        logger.info("Evaluating scene {}", imageEntity);

                        BufferedImage imageScene = ImagePatternDetector.readImageFromFile(
                                sourceImagesDirectory + fileSeparator + imageEntity.getSourcePath());
                        Map<EvaluationMarkerEntity, BufferedImageMarker> markers = prepareImageMarkers(markerList, markerImagesDirectory);

                        List<Double> matchScores = Lists.newArrayList();
                        BufferedImage resultImage = processOneImageScene(imageScene, markers, matchScores);
                        logger.debug("Markers result scores: {}", matchScores);

                        int resultScore = determineResultScore(matchScores);
                        logger.info("Result value for scene: {}", resultScore);
                        resultScores.add(resultScore);

                        String entityResultPath = createEntityOutputPath(outputImagesDirectory, imageEntity);
                        logger.info(" entityResultPath : {}", entityResultPath);
                        ImagePatternDetector.saveBufferedImageAsFile(resultImage, entityResultPath);
                    }
                    setEvaluationResult(resultScores, entity);
                    setEvaluationStatus(DONE, entity);
                } catch (Exception e) {
                    logger.error("Error during processing entity: {}", entity, e);
                    setEvaluationStatus(FAILED, entity);

                    String message = String.format("Error with processing evaluation %s", entity);
                    throw new StandaloneApplicationException(message, e);
                }
            } else {
                logger.warn("No markers for evaluation (id {})!", evaluationId);
                Date startDate = entity.getStartDate();
                entity.setStartDate(new Date());
                if (startDate != null) {
                    setEvaluationStatus(FAILED, entity);
                } else {
                    logger.warn("Set startDate for evaluation (id {}). "
                            + "If next time markers will be missing, evaluation will be set as FAILED."
                            , evaluationId);
                    evaluationRepository.save(entity);
                }
            }
//            break;
        }
    }

    private void setEvaluationResult(List<Integer> scores, EvaluationEntity entity) {
        Integer averageResultScore = 0;
        for (Integer score : scores) {
            averageResultScore += score;
        }
        averageResultScore /= scores.size();

        entity.setResult(averageResultScore);
    }

    private int determineResultScore(List<Double> matchScores) {
        Double min = Collections.min(matchScores);
        return new Double(min * 100.f).intValue();
    }

    private void setEvaluationStatus(ProcessingStatus status, EvaluationEntity entity) {
        logger.info("Setting status code {} ({}) for entity with id={}",
                status.getCode(), status.getDescription(), entity.getId());
        entity.setStatus(status.getCode());
        Date now = new Date();
        switch (status) {
            case STARTED:
                entity.setStartDate(now);
                break;
            case DONE:
            case FAILED:
                entity.setEndDate(now);
                break;
            default:
                logger.warn("Unknown status {}, skipping setting dates", status);
        }
        evaluationRepository.save(entity);
    }

    private String createEntityOutputPath(String directory, EvaluationImageEntity entity) {
        String outputPath = entity.getProcessedPath();
        if (outputPath == null) {
            File outputFile = new File(entity.getSourcePath());
            String fileDirPath = outputFile.getParentFile().getPath();
            String fileBaseName = outputFile.getName();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String dateString = sdf.format(new Date());

            outputPath = fileDirPath + fileSeparator + "output-" + dateString + "-" + fileBaseName;

            logger.info("No result path value! Saving '{}'", outputPath);
            entity.setProcessedPath(outputPath);
            imageRepository.save(entity);
        }
        return directory + outputPath;
    }

    private BufferedImage processOneImageScene(BufferedImage imageScene,
                                               Map<EvaluationMarkerEntity, BufferedImageMarker> markers,
                                               List<Double> markersScores
    ) {
        BufferedImage resultImage = MultiScaleStageTask.createScaledResultImage(imageScene);

        for (Map.Entry<EvaluationMarkerEntity, BufferedImageMarker> entry : markers.entrySet()) {
            EvaluationMarkerEntity entity = entry.getKey();
            BufferedImageMarker marker = entry.getValue();

            logger.info("Matching scene against marker {}", entity);

            SessionData sessionData = ImagePatternDetector.createSessionData();
            sessionData.setInputImage(imageScene);
            sessionData.setTemplateImage(marker.getImageMarker());

            MultiScaleStageTask task = ImagePatternDetector.createMultiScaleTask(
                    sessionData, ImagePatternDetector.ABS_COLOR_METRIC);
            task.initialize(tasksProperties);
            task.run();

            DetectorTaskInput taskInput = task.getTaskInput();
            BestLocalizedMatchStrategy matchStrategy = (BestLocalizedMatchStrategy) taskInput.getStrategy();

            List<MatchScore> validResults = Lists.newArrayList();
            List<MatchScore> possibleResults = Lists.newArrayList();
            matchStrategy.determineValidAndPossibleResults(taskInput, validResults, possibleResults);


//            BufferedImage resultImage = sessionData.getResultImage();
            long maxOccurrencesCount = marker.getOccurrencesCount();
            long count = 0;
            double resultScore = Double.MIN_VALUE;

            Collections.sort(validResults);
            logger.info("   validResults: {}", validResults);
            Stroke straightStroke = new BasicStroke(3.f);
            for (MatchScore score : validResults) {
                if (count == maxOccurrencesCount) {
                    break;
                }
                resultScore = Math.max(resultScore, score.getScore());

                DrawHelper.drawRectangleOnImage(resultImage,
                        score.getWidthPosition(), score.getHeightPosition(),
                        marker.getImageMarker().getWidth(), marker.getImageMarker().getHeight(),
                        score.getScaleFactor());
                DrawHelper.makeBrighterRectangleOnImage(resultImage,
                        score.getWidthPosition(), score.getHeightPosition(),
                        marker.getImageMarker().getWidth(), marker.getImageMarker().getHeight(),
                        score.getScaleFactor(), marker.getMarkerColor(), straightStroke);
                ++count;
            }

            Collections.sort(possibleResults);
            logger.info("possibleResults: {}", possibleResults);
            Stroke dashedStroke = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
            for (MatchScore score : possibleResults) {
                if (count == maxOccurrencesCount) {
                    break;
                }
                resultScore = Math.max(resultScore, score.getScore());

                DrawHelper.makeBrighterRectangleOnImage(resultImage,
                        score.getWidthPosition(), score.getHeightPosition(),
                        marker.getImageMarker().getWidth(), marker.getImageMarker().getHeight(),
                        score.getScaleFactor(), marker.getMarkerColor(), dashedStroke);
                ++count;
            }

            markersScores.add(resultScore);
            logger.info("Minimal score for scene is {}", resultScore);

            String detectedMessage = String.format("Detected %d match(es) for marker %s. Expected %d.",
                    count, entity, extractOccurrencesCount(entity));
            if (count == 0) {
                logger.warn(detectedMessage);
            } else {
                logger.info(detectedMessage);
            }
            entity.setFound(Long.valueOf(count));
            markerRepository.save(entity);
        }
        return resultImage;
    }

    private Map<EvaluationMarkerEntity, BufferedImageMarker> prepareImageMarkers(
            List<EvaluationMarkerEntity> markerList, String directory
    ) throws CouldNotReadImageFileException {
        Map<EvaluationMarkerEntity, BufferedImageMarker> imageMarkersMap = Maps.newHashMap();
        for (EvaluationMarkerEntity entity : markerList) {
            String markerAbsolutePath = directory + fileSeparator + entity.getPath();
            try {
                BufferedImage image = ImagePatternDetector.readImageFromFile(markerAbsolutePath);
                BufferedImageMarker marker = new BufferedImageMarker(image, extractOccurrencesCount(entity));
                marker.setMarkerColor(convertColorString(entity.getColor()));

                imageMarkersMap.put(entity, marker);
            } catch (CouldNotReadImageFileException e) {
                String message = "Could not read marker file: " + markerAbsolutePath;
                throw new CouldNotReadImageFileException(message, e);
            }
        }
        return imageMarkersMap;
    }

    private long extractOccurrencesCount(EvaluationMarkerEntity entity) {
        Long toBeFound = entity.getToBeFound();
        if (toBeFound == null || toBeFound < 1L) {
            logger.warn("Occurrences count for marker {} is null or less than 1. Assuming 1!", entity);
            return 1L;
        }
        return toBeFound.longValue();
    }

    private Color convertColorString(String colorString) {
        if (Strings.isNullOrEmpty(colorString)) {
            logger.warn("String was null or empty. Assigning color: {}.", DEFAULT_MARKER_COLOR.toString());
            return DEFAULT_MARKER_COLOR;
        }
        try {
            return new Color(Integer.parseInt(colorString, 16));
        } catch (NumberFormatException e) {
            logger.warn("Problem with converting string '{}' to color. "
                    + "Please use hex format without '0x' prefix (example: '0000FF' is blue)", colorString);
            return DEFAULT_MARKER_COLOR;
        }
    }
}
