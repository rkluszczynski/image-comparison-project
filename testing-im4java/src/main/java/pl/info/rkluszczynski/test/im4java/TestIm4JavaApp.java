package pl.info.rkluszczynski.test.im4java;


import org.im4java.core.*;
import org.im4java.process.ProcessStarter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TestIm4JavaApp {

    private static String RESOURCE_RELATIVE_PATH = "testing-im4java/src/main/resources/";
    private static String GLOBAL_SEARCH_PATH = "D:\\PortableApps\\ImageMagick-6.8.8-7";

    public static void main(String[] args) throws Exception {
        System.out.println(System.getProperty("user.dir"));
        // Overwriting IM4JAVA_TOOLPATH:
        ProcessStarter.setGlobalSearchPath(GLOBAL_SEARCH_PATH);

//        runResizeFromFileToFile();
//        readConvertedBufferedImageFromFile();

//        convertBufferedImageToBufferedImage();
        subImageSearchFromBufferedImagesToBufferedImage();
    }


    private static void runResizeFromFileToFile() throws IOException, InterruptedException, IM4JavaException {
        // Create command:
        ConvertCmd cmd = new ConvertCmd();

        // Create the operation, add images and operators/options:
        IMOperation op = new IMOperation();
        op.addImage(RESOURCE_RELATIVE_PATH + "input/image.png");
        op.resize(800, 600);
        op.addImage(RESOURCE_RELATIVE_PATH + "tmp-result-image.jpg");

        // Execute the operation:
        cmd.run(op);
    }

    private static void readConvertedBufferedImageFromFile() throws InterruptedException, IOException, IM4JavaException {
        IMOperation op = new IMOperation();
        op.addImage();                        // input
        op.addImage("png:-");                 // output: stdout
        String[] images = new String[] {
                RESOURCE_RELATIVE_PATH + "input/image.png"
        };

        // Set up command:
        ConvertCmd convert = new ConvertCmd();
        Stream2BufferedImage s2b = new Stream2BufferedImage();
        convert.setOutputConsumer(s2b);

        // Run command and extract BufferedImage from OutputConsumer:
        convert.run(op, (Object[]) images);
        BufferedImage img = s2b.getImage();

        // Print some info on console:
        System.out.println(img.toString());
    }

    private static void convertBufferedImageToBufferedImage() throws Exception {
        BufferedImage inputImage = ImageIO.read(
                new File(RESOURCE_RELATIVE_PATH + "input/image.png")
        );

        IMOperation op = new IMOperation();
        op.addImage();                        // input
        op.blur(2.0).paint(10.0);
        op.addImage("png:-");                 // output: stdout

        // Set up command:
        ConvertCmd convert = new ConvertCmd();
        Stream2BufferedImage s2b = new Stream2BufferedImage();
        convert.setOutputConsumer(s2b);

        // Run command and extract BufferedImage from OutputConsumer:
        convert.run(op, inputImage);
        BufferedImage resultImage = s2b.getImage();

        System.out.println(resultImage.toString());
        ImageIO.write(resultImage, "PNG",
                new File(RESOURCE_RELATIVE_PATH + "tmp-result-blurred.png")
        );
    }

    private static void subImageSearchFromBufferedImagesToBufferedImage() throws Exception {
        BufferedImage inputImage = ImageIO.read(
                new File(RESOURCE_RELATIVE_PATH + "input/bon-image1.png")
        );
        BufferedImage templateImage = ImageIO.read(
                new File(RESOURCE_RELATIVE_PATH + "input/bon-template1.png")
        );

        /* Metrics:
  AE     absolute error count, number of different pixels (-fuzz effected)
  FUZZ   mean color distance
  MAE    mean absolute error (normalized), average channel error distance
  MEPP   mean error per pixel (normalized mean error, normalized peak error)
  MSE    mean error squared, average of the channel error squared
  NCC    normalized cross correlation
  PAE    peak absolute (normalized peak absolute)
  PHASH  perceptual hash
  PSNR   peak signal to noise ratio
  RMSE   root mean squared (normalized root mean squared)
         */

        IMOperation op = new IMOperation();
        op.addImage();
        op.addImage();
        op.metric("RMSE").subimageSearch();
        op.addImage("png:-");                 // output: stdout

        CompareCmd compareCmd = new CompareCmd();
        Stream2BufferedImage stream2BufferedImage = new Stream2BufferedImage();
        compareCmd.setOutputConsumer(stream2BufferedImage);

        compareCmd.run(op, inputImage, templateImage);
        BufferedImage resultImage = stream2BufferedImage.getImage();

        System.out.println(resultImage.toString());
        ImageIO.write(resultImage, "PNG",
                new File(RESOURCE_RELATIVE_PATH + "tmp-result-subimg.png")
        );
    }
}
