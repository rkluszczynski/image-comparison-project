package pl.info.rkluszczynski.test.im4java;


import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.Stream2BufferedImage;
import org.im4java.process.ProcessStarter;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class TestIm4JavaApp {

    private static String RESOURCE_RELATIVE_PATH = "testing-im4java/src/main/resources/";
    private static String GLOBAL_SEARCH_PATH = "D:\\PortableApps\\ImageMagick-6.8.8-7";

    public static void main(String[] args) throws InterruptedException, IOException, IM4JavaException {
        System.out.println(System.getProperty("user.dir"));
        // Overwriting IM4JAVA_TOOLPATH:
        ProcessStarter.setGlobalSearchPath(GLOBAL_SEARCH_PATH);

        runResizeFromFileToFile();
        readBufferedImageFromFile();
    }


    private static void runResizeFromFileToFile() throws IOException, InterruptedException, IM4JavaException {
        // Create command:
        ConvertCmd cmd = new ConvertCmd();

        // Create the operation, add images and operators/options:
        IMOperation op = new IMOperation();
        op.addImage(RESOURCE_RELATIVE_PATH + "image.jpg");
        op.resize(800, 600);
        op.addImage(RESOURCE_RELATIVE_PATH + "tmp-result-image.jpg");

        // Execute the operation:
        cmd.run(op);
    }

    private static void readBufferedImageFromFile() throws InterruptedException, IOException, IM4JavaException {
        IMOperation op = new IMOperation();
        op.addImage();                        // input
        op.addImage("png:-");                 // output: stdout
        String[] images = new String[] {
                RESOURCE_RELATIVE_PATH + "image.jpg"
        };

        // Set up command:
        ConvertCmd convert = new ConvertCmd();
        Stream2BufferedImage s2b = new Stream2BufferedImage();
        convert.setOutputConsumer(s2b);

        // Run command and extract BufferedImage from OutputConsumer:
        convert.run(op,(Object[]) images);
        BufferedImage img = s2b.getImage();

        // Print some info on console:
        System.out.println(img.toString());
    }
}
