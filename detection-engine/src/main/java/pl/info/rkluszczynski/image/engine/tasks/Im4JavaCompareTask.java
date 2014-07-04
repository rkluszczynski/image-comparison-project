package pl.info.rkluszczynski.image.engine.tasks;

import org.im4java.core.CompareCmd;
import org.im4java.core.IMOperation;
import org.im4java.core.Stream2BufferedImage;
import org.im4java.process.StandardStream;
import pl.info.rkluszczynski.image.engine.model.SessionData;
import pl.info.rkluszczynski.image.engine.tasks.input.TasksProperties;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;

import static pl.info.rkluszczynski.image.engine.model.ImageStatisticNames.DUMMY_RESULT;


public class Im4JavaCompareTask extends AbstractDetectorTask {
    private static String GLOBAL_SEARCH_PATH = "D:\\PortableApps\\ImageMagick-6.8.8-7";

    private BufferedImage resultImage;

    public Im4JavaCompareTask(SessionData sessionData) {
        super(sessionData, null);
        logger.info(">>> " + sessionData.getSession().getServletContext().getContextPath());
//        ProcessStarter.setGlobalSearchPath(GLOBAL_SEARCH_PATH);
    }

    @Override
    public void prepareImageData(BufferedImage inputImage, BufferedImage patternImage) {
    }

    @Override
    public void initialize(TasksProperties tasksProperties) {
    }

    @Override
    public void processImageData(BufferedImage inputImage, BufferedImage templateImage) {
        IMOperation compareOperation = new IMOperation();
        compareOperation.addImage();
        compareOperation.addImage();
        compareOperation.metric("RMSE");
        compareOperation.addImage("png:-");

        // Set up command:
        CompareCmd compare = new CompareCmd();
        compare.setErrorConsumer(StandardStream.STDERR);
        Stream2BufferedImage s2b = new Stream2BufferedImage();
        compare.setOutputConsumer(s2b);

        // Run command and extract BufferedImage from OutputConsumer:
        try {
            compare.run(compareOperation, inputImage, templateImage);
        } catch (Exception e) {
            logger.error("Unable to run convert operation!", e);
        }
        resultImage = s2b.getImage();
    }

    @Override
    public void storeResults() {
        saveResultImage(resultImage);
        saveStatisticData(DUMMY_RESULT, BigDecimal.ZERO);
    }
}
