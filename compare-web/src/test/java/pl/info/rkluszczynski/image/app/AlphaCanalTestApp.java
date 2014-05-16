package pl.info.rkluszczynski.image.app;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Rafal on 2014-05-15.
 */
public class AlphaCanalTestApp {

    public static void main(String[] args) throws IOException {
        System.out.println(System.getProperty("user.dir"));
        BufferedImage image;

        image = ImageIO.read(new File("compare-web/src/main/resources/image-data/patterns/bon-pattern1-shelfstoper.png"));

        Color pixel00 = new Color(image.getRGB(0, 0));
        System.out.println("   red : " + pixel00.getRed());
        System.out.println(" green : " + pixel00.getGreen());
        System.out.println("  blue : " + pixel00.getBlue());
        System.out.println(" alpha : " + pixel00.getAlpha());

        Color pixelHalfHalf = new Color(image.getRGB(image.getWidth() / 2, image.getHeight() / 2));
        System.out.println("   red : " + pixelHalfHalf.getRed());
        System.out.println(" green : " + pixelHalfHalf.getGreen());
        System.out.println("  blue : " + pixelHalfHalf.getBlue());
        System.out.println(" alpha : " + pixelHalfHalf.getAlpha());
    }
}
