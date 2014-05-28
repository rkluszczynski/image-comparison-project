package pl.info.rkluszczynski.image.engine.tasks;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.im4java.core.Stream2BufferedImage;
import pl.info.rkluszczynski.image.engine.model.SessionData;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;

import static pl.info.rkluszczynski.image.engine.model.ImageStatisticNames.DUMMY_RESULT;


public class Im4JavaTask extends AbstractTask {
    private static String GLOBAL_SEARCH_PATH = "D:\\PortableApps\\ImageMagick-6.8.8-7";

    public Im4JavaTask(SessionData sessionData) {
        super(sessionData);
//        logger.info(">>> " + sessionData.getSession().getServletContext().getContextPath());
//        ProcessStarter.setGlobalSearchPath(GLOBAL_SEARCH_PATH);
    }

    @Override
    public void processImageData(BufferedImage inputImage, BufferedImage templateImage) {
        IMOperation op = new IMOperation();
        op.addImage();                        // input
        op.blur(2.0).paint(10.0);
        op.addImage("png:-");                 // output: stdout

        // Set up command:
        ConvertCmd convert = new ConvertCmd();
        Stream2BufferedImage s2b = new Stream2BufferedImage();
        convert.setOutputConsumer(s2b);

        // Run command and extract BufferedImage from OutputConsumer:
        try {
            convert.run(op, inputImage);
        } catch (Exception e) {
            logger.error("Unable to run convert operation!", e);
        }
        BufferedImage resultImage = s2b.getImage();

        saveResultImage(resultImage);
        saveStatisticData(DUMMY_RESULT, BigDecimal.ZERO);
    }
}
