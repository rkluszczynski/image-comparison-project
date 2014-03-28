package pl.info.rkluszczynski.image.engine.tasks;


import pl.info.rkluszczynski.image.engine.model.SessionData;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;

import static pl.info.rkluszczynski.image.engine.model.ImageStatisticNames.DUMMY_RESULT;

public class CopyInputImageTask extends AbstractTask {
    public CopyInputImageTask(SessionData sessionData) {
        super(sessionData);
    }

    @Override
    public void processImageData(BufferedImage inputImage, BufferedImage templateImage) {
        try {
            Thread.sleep(9000L);
        } catch (InterruptedException e) {
            logger.warn("Sleep interrupted", e);
        }
        saveResultImage(inputImage);
        saveStatisticData(DUMMY_RESULT, BigDecimal.ZERO);
    }
}
