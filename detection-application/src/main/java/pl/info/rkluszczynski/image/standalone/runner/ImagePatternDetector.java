package pl.info.rkluszczynski.image.standalone.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import pl.info.rkluszczynski.image.core.compare.metric.CompareMetric;
import pl.info.rkluszczynski.image.engine.model.SessionData;
import pl.info.rkluszczynski.image.engine.model.comparators.PatternMatchComparator;
import pl.info.rkluszczynski.image.engine.model.comparators.PixelDifferenceComparator;
import pl.info.rkluszczynski.image.engine.model.comparators.SequenceComparator;
import pl.info.rkluszczynski.image.engine.model.metrics.AbsColorMetric;
import pl.info.rkluszczynski.image.engine.model.strategies.BestLocalizedMatchStrategy;
import pl.info.rkluszczynski.image.engine.model.strategies.PatternMatchStrategy;
import pl.info.rkluszczynski.image.engine.tasks.MultiScaleStageTask;
import pl.info.rkluszczynski.image.engine.tasks.input.DetectorTaskInput;
import pl.info.rkluszczynski.image.engine.tasks.input.TasksProperties;
import pl.info.rkluszczynski.image.standalone.db.ProcessedImageStatus;
import pl.info.rkluszczynski.image.standalone.db.entities.PlanogramEntity;
import pl.info.rkluszczynski.image.standalone.db.entities.ProcessedImageEntity;
import pl.info.rkluszczynski.image.standalone.db.repositories.PlanogramRepository;
import pl.info.rkluszczynski.image.standalone.db.repositories.ProcessedImageRepository;
import pl.info.rkluszczynski.image.standalone.exception.CouldNotReadImageFileException;
import pl.info.rkluszczynski.image.standalone.exception.CouldNotWriteImageFileException;
import pl.info.rkluszczynski.image.standalone.exception.StandaloneApplicationException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static pl.info.rkluszczynski.image.standalone.db.ProcessedImageStatus.*;

@Component(value = "imagePatternDetector")
public class ImagePatternDetector implements StandaloneRunner {
    private static final Logger logger = LoggerFactory.getLogger(ImagePatternDetector.class);

    private static final CompareMetric ABS_COLOR_METRIC = new AbsColorMetric();
    private static final String RESULT_IMAGE_FORMAT = "PNG";

    private final String fileSeparator = System.getProperty("file.separator");

    @Autowired
    private ProcessedImageRepository processedImageRepository;
    @Autowired
    private PlanogramRepository planogramRepository;
    @Autowired
    private Environment env;
    @Autowired
    private TasksProperties tasksProperties;

    @Override
    public void run() throws StandaloneApplicationException {
        String templateImagesDirectory = env.getProperty("template.images.directory", ".") + fileSeparator;
        String sourceImagesDirectory = env.getProperty("source.images.directory", ".") + fileSeparator;
        String outputImagesDirectory = env.getProperty("output.images.directory", ".") + fileSeparator;

        List<ProcessedImageEntity> imageEntities = processedImageRepository.findByStatus(NEW.getCode());
        if (imageEntities.size() == 0) {
            logger.info("No entities with NEW status.");
            return;
        }

        for (ProcessedImageEntity entity : imageEntities) {
            logger.info("Processing entity {}", entity);
            PlanogramEntity planogramEntity = planogramRepository.findOne(
                    Long.valueOf(entity.getPlanogramId())
            );

            String planogramPath = templateImagesDirectory + planogramEntity.getPath();
            String entitySourcePath = sourceImagesDirectory + entity.getSourcepath();

            logger.info("    planogramPath : {}", planogramPath);
            logger.info(" entitySourcePath : {}", entitySourcePath);

            try {
                BufferedImage inputImage = readImageFromFile(entitySourcePath);
                BufferedImage templateImage = readImageFromFile(planogramPath);

                SessionData sessionData = createSessionData();
                sessionData.setInputImage(inputImage);
                sessionData.setTemplateImage(templateImage);

                MultiScaleStageTask task = createMultiScaleTask(sessionData, ABS_COLOR_METRIC);
                setImageEntityStatus(STARTED, entity);
                task.initialize(tasksProperties);
                task.run();

                String entityResultPath = createEntityOutputPath(outputImagesDirectory, entity);
                logger.info(" entityResultPath : {}", entityResultPath);
                BufferedImage resultImage = sessionData.getResultImage();
                saveBufferedImageAsFile(resultImage, entityResultPath);

                setImageEntityStatus(DONE, entity);
            } catch (StandaloneApplicationException e) {
                logger.error("Error during processing entity: {}", entity, e);
                setImageEntityStatus(FAILED, entity);
            }
//            break;
        }
    }

    private void setImageEntityStatus(ProcessedImageStatus status, ProcessedImageEntity entity) {
        logger.info("Setting status code {} ({}) for entity with id={}",
                status.getCode(), status.getDescription(), entity.getId());
        entity.setStatus(status.getCode());
        processedImageRepository.save(entity);
    }

    private void saveBufferedImageAsFile(BufferedImage image, String path) throws CouldNotWriteImageFileException {
        try {
            ImageIO.write(image, RESULT_IMAGE_FORMAT, new File(path));
        } catch (IOException e) {
            String message = "Could not write result image to file: " + path;
            throw new CouldNotWriteImageFileException(message, e);
        }
    }

    private String createEntityOutputPath(String directory, ProcessedImageEntity entity) {
        String outputPath = entity.getProcessedpath();
        if (outputPath == null) {
            File outputFile = new File(entity.getSourcepath());
            String fileDirPath = outputFile.getParentFile().getPath();
            String fileBaseName = outputFile.getName();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String dateString = sdf.format(new Date());

            outputPath = fileDirPath + fileSeparator + "output-" + dateString + "-" + fileBaseName;

            logger.info("No result path value! Saving '{}'", outputPath);
            entity.setProcessedpath(outputPath);
            processedImageRepository.save(entity);
        }
        return directory + outputPath;
    }

    private BufferedImage readImageFromFile(String imageFilePath) throws CouldNotReadImageFileException {
        try {
            return ImageIO.read(new File(imageFilePath));
        } catch (IOException e) {
            String message = "Could not read file: " + imageFilePath;
            throw new CouldNotReadImageFileException(message, e);
        }
    }


    private SessionData createSessionData() {
        SessionData sessionData = new SessionData(null);
        return sessionData;
    }

    private MultiScaleStageTask createMultiScaleTask(SessionData sessionData, CompareMetric metric) {
        PatternMatchStrategy matchStrategy = new BestLocalizedMatchStrategy();
        PatternMatchComparator matchComparator = new SequenceComparator(
                new PixelDifferenceComparator(metric)
        );
        DetectorTaskInput detectorTaskInput = new DetectorTaskInput(matchComparator, matchStrategy);
        return new MultiScaleStageTask(sessionData, detectorTaskInput);
    }
}
