package pl.info.rkluszczynski.image.engine.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.info.rkluszczynski.image.engine.model.ImageStatisticData;
import pl.info.rkluszczynski.image.engine.model.ImageStatisticNames;
import pl.info.rkluszczynski.image.engine.model.SessionData;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;

import static pl.info.rkluszczynski.image.engine.model.ImageStatisticNames.CALCULATION_TIME;
import static pl.info.rkluszczynski.image.engine.model.ImageStatisticNames.ERROR_RESULT;

abstract class AbstractTask extends Thread {
    protected static Logger logger = LoggerFactory.getLogger(AbstractTask.class);

    protected SessionData sessionData;
    private double taskProgress;


    protected AbstractTask(SessionData sessionData) {
        this.sessionData = sessionData;
    }

    SessionData getSessionData() {
        return sessionData;
    }

    @Override
    public void run() {
        super.run();
        long startMillis = System.currentTimeMillis();
        taskProgress = 0.;
        try {
            processImageData(sessionData.getInputImage(), sessionData.getTemplateImage());
        } catch (Exception e) {
            saveResultImage(null);
            saveStatisticData(ERROR_RESULT, BigDecimal.ZERO);
            logger.error("Error occurred during execution task: " + getClass().getName(), e);
        }
        taskProgress = 1.;
        long processMillis = System.currentTimeMillis() - startMillis;
        saveStatisticData(CALCULATION_TIME, BigDecimal.valueOf(processMillis));
    }

    abstract
    protected void processImageData(BufferedImage inputImage, BufferedImage templateImage);

    protected void saveResultImage(BufferedImage resultImage) {
        sessionData.setResultImage(resultImage);
    }

    protected void saveStatisticData(ImageStatisticNames statisticName, BigDecimal statisticValue) {
        sessionData.getImageStatistics().add(new ImageStatisticData(statisticName, statisticValue));
    }

    protected void addProgress(double progressValue) {
        taskProgress += progressValue;
        sessionData.setProgress((long) (Math.min(Math.max(taskProgress, 0.), 1.) * 100.));
    }
}
