package pl.info.rkluszczynski.image.engine.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import pl.info.rkluszczynski.image.engine.model.ImageStatisticData;
import pl.info.rkluszczynski.image.engine.model.ImageStatisticNames;
import pl.info.rkluszczynski.image.engine.model.SessionData;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;

abstract
class AbstractTask extends Thread {
    protected static Logger logger = LoggerFactory.getLogger(AbstractTask.class);

    @Autowired
    @Qualifier("taskExecutor")
    private ThreadPoolTaskExecutor taskExecutor;

    protected SessionData sessionData;


    protected AbstractTask(SessionData sessionData) {
        this.sessionData = sessionData;
    }

    @Override
    public void run() {
        super.run();
        processImageData(sessionData.getInputImage(), sessionData.getTemplateImage());
    }

    abstract
    protected void processImageData(BufferedImage inputImage, BufferedImage templateImage);

    protected void saveResultImage(BufferedImage resultImage) {
        sessionData.setResultImage(resultImage);
    }

    protected void saveStatisticData(ImageStatisticNames statisticName, BigDecimal statisticValue) {
        sessionData.getImageStatistics().add(new ImageStatisticData(statisticName, statisticValue));
    }
}
