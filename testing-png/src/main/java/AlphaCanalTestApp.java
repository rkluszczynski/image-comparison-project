import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class AlphaCanalTestApp {
    private static final String TESTING_PNG_TMP_RESULT_OUT_PNG = "testing-png/tmp-result-out.png";

    public static void main(String[] args) throws IOException {
        System.out.println(System.getProperty("user.dir"));
        BufferedImage image;

        image = ImageIO.read(new File(TESTING_PNG_TMP_RESULT_OUT_PNG));
        image = ImageIO.read(new File("compare-web/src/main/resources/image-data/bon2.png"));
        //image = ImageIO.read(new URL("http://upload.wikimedia.org/wikipedia/commons/archive/4/47/20100130232511!PNG_transparency_demonstration_1.png"));          System.out.println("isAlphaPremultiplied: " + image.isAlphaPremultiplied());

//        image = readBufferedImage();

        Raster raster = image.getAlphaRaster();
        if (raster == null) {
            System.err.println("there is no Alpha channel!!!!!!!!!");
        } else {
            System.out.println("Alpha channel found !");

            float[] sample = null;
            for (int iw = 0; iw < raster.getWidth(); iw++) {
                for (int ih = 0; ih < raster.getHeight(); ih++) {
                    sample = raster.getPixel(iw, ih, sample);
                    for (int i = 0; i < sample.length; i++) {
                        System.out.print(sample[i] + "/");
                    }
                    System.out.print(" ");
                }
                System.out.println();
            }
        }

//        image.coerceData(true);
//        for (int iw = 0; iw < image.getWidth(); ++iw) {
//            for (int ih = 0; ih < image.getHeight(); ++ih) {
//                System.out.print(" " + new Color(image.getRGB(iw, ih)).getAlpha());
//            }
//            System.out.println();
//        }
//        System.out.println("isAlphaPremultiplied: " + image.isAlphaPremultiplied());
    }


    private static BufferedImage readBufferedImage() throws IOException {
        BufferedImage in = ImageIO.read(
                new URL("http://upload.wikimedia.org/wikipedia/commons/archive/4/47/20100130232511!PNG_transparency_demonstration_1.png")
//            new File("compare-web/src/main/resources/image-data/qq.png")
        );

        Graphics2D gin = in.createGraphics();
        GraphicsConfiguration gc = gin.getDeviceConfiguration();
        gin.dispose();

        BufferedImage out = gc.createCompatibleImage(in.getWidth(), in.getHeight(), Transparency.BITMASK);
        Graphics2D g2d = out.createGraphics();
        g2d.setComposite(AlphaComposite.Src);
        g2d.drawImage(in, 0, 0, in.getWidth(), in.getHeight(), null);
        g2d.dispose();

        return out;
    }
}
