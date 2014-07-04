package pl.info.rkluszczynski.image.engine.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.info.rkluszczynski.image.engine.model.ImageStatisticData;
import pl.info.rkluszczynski.image.engine.model.ImageStatisticNames;
import pl.info.rkluszczynski.image.engine.model.SessionData;
import pl.info.rkluszczynski.image.engine.tasks.input.DetectorTaskInput;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;

import static pl.info.rkluszczynski.image.engine.model.ImageStatisticNames.CALCULATION_TIME;
import static pl.info.rkluszczynski.image.engine.model.ImageStatisticNames.ERROR_RESULT;

abstract class AbstractDetectorTask extends Thread implements PatternDetectorTask {
    protected static final Logger logger = LoggerFactory.getLogger(AbstractDetectorTask.class);

    private final DetectorTaskInput taskInput;
    private final SessionData sessionData;

    private double taskProgressValue;

    AbstractDetectorTask(SessionData sessionData, DetectorTaskInput taskInput) {
        this.sessionData = sessionData;
        this.taskInput = taskInput;
    }

    @Override
    public void run() {
        super.run();

        long startMillis = System.currentTimeMillis();
        taskProgressValue = 0.;
        try {
            prepareImageData(sessionData.getInputImage(), sessionData.getTemplateImage());
            getTaskInput().initialize();
            processImageData(sessionData.getInputImage(), sessionData.getTemplateImage());
            storeResults();
        } catch (Exception e) {
            saveResultImage(null);
            saveStatisticData(ERROR_RESULT, BigDecimal.ZERO);
            logger.error("Error occurred during execution task: " + getClass().getName(), e);
        }
        taskProgressValue = 1.;
        sessionData.setProgress(100L);
        long processMillis = System.currentTimeMillis() - startMillis;

        saveStatisticData(CALCULATION_TIME, BigDecimal.valueOf(processMillis));
    }

    protected SessionData getSessionData() {
        return sessionData;
    }

    protected DetectorTaskInput getTaskInput() {
        return taskInput;
    }

    protected void saveResultImage(BufferedImage resultImage) {
        sessionData.setResultImage(resultImage);
    }

    @Override
    public void saveStatisticData(ImageStatisticNames statisticName, BigDecimal statisticValue) {
        sessionData.getImageStatistics().add(new ImageStatisticData(statisticName, statisticValue));
    }

    @Override
    public void addProgress(double progressValue) {
        taskProgressValue += progressValue;
        sessionData.setProgress((long) (Math.min(Math.max(taskProgressValue, 0.), 1.) * 100.));
    }
}
