package pl.info.rkluszczynski.image.app;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

/**
 * Created by Rafal on 2014-05-15.
 */
public class AlphaCanalTestApp {

    public static void main(String[] args) throws IOException {
        System.out.println(System.getProperty("user.dir"));
        BufferedImage image;

        image = ImageIO.read(new File("compare-web/src/main/resources/image-data/bon-pattern2-wobler.png"));
        image = ImageIO.read(new File("compare-web/src/main/resources/image-data/zubr-pattern2-shelfstoper.png"));

        System.out.println("isAlphaPremultiplied: " + image.isAlphaPremultiplied());
        Raster raster = image.getAlphaRaster();
        if (raster == null) {
            System.err.println("there is no Alpha channel!!!!!!!!!");
        } else {
            for (int iw = 0; iw < image.getWidth(); ++iw) {
                System.out.print(":");
                for (int ih = 0; ih < image.getHeight(); ++ih) {
                    System.out.printf(" %3d", image.getRaster().getPixel(iw, ih, (int[]) null)[0]);
                }
                System.out.println();
            }
        }
    }
}
