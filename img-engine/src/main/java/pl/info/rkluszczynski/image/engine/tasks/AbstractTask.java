package pl.info.rkluszczynski.image.engine.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import pl.info.rkluszczynski.image.engine.model.SessionData;

import java.awt.image.BufferedImage;

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

    abstract
    protected void processImageData(BufferedImage inputImage, BufferedImage templateImage);

    protected void saveResultImage(BufferedImage resultImage) {
        sessionData.setResultImage(resultImage);
    }

    @Override
    public void run() {
        super.run();
        processImageData(sessionData.getInputImage(), sessionData.getTemplateImage());
    }

//    public void submit() {
//        taskExecutor.execute(this);
//    }
}
