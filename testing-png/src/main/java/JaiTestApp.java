import javax.imageio.ImageIO;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.SampleModel;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class JaiTestApp {
    private static final String TESTING_PNG_TMP_RESULT_OUT_PNG = "testing-png/tmp-result-out.png";

    public static void main(String[] args) throws IOException {
        System.out.println(System.getProperty("user.dir"));
        createExampleImageWithAlpha();

        BufferedImage image;
        String fileName = TESTING_PNG_TMP_RESULT_OUT_PNG;
//        fileName = "testing-png/src/main/resources/bon2.png";
        PlanarImage planarImage = JAI.create("fileload", fileName);

        SampleModel sm = planarImage.getSampleModel();
        System.out.println("\t                       sm.getNumBands(): " + sm.getNumBands());
        System.out.println("\t planarImage.getColorModel().hasAlpha(): " + planarImage.getColorModel().hasAlpha());

        image = planarImage.getAsBufferedImage();


        URL url = new URL("http://upload.wikimedia.org/wikipedia/commons/archive/4/47/20100130232511!PNG_transparency_demonstration_1.png");
        PlanarImage pi = JAI.create("URL", url);
//        image = pi.getAsBufferedImage();


        for (int iw = 0; iw < image.getWidth(); ++iw) {
            for (int ih = 0; ih < image.getHeight(); ++ih) {
                System.out.print(" " + new Color(image.getRGB(iw, ih)).getAlpha());
            }
            System.out.println();
        }
        System.out.println("isAlphaPremultiplied: " + image.isAlphaPremultiplied());
    }

    private static void createExampleImageWithAlpha() throws IOException {
        int n = 100;
        BufferedImage bufferedImage = new BufferedImage(n, n, BufferedImage.TYPE_INT_ARGB);
        for (int iw = 0; iw < n; ++iw) {
            for (int ih = 0; ih < n; ++ih) {
                Color color = new Color(255, 255, 255, iw < ih ? 255 : 0);
                bufferedImage.setRGB(iw, ih, color.getRGB());
            }
        }
        ImageIO.write(bufferedImage, "PNG", new File(TESTING_PNG_TMP_RESULT_OUT_PNG));
    }
}
