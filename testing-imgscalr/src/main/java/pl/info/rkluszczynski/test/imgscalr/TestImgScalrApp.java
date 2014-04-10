package pl.info.rkluszczynski.test.imgscalr;


import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TestImgScalrApp {

    private static String IMAGE_RELATIVE_PATH = "testing-imgscalr/src/main/resources/image.jpg";

    public static void main(String[] args) throws IOException {
        System.out.println(System.getProperty("user.dir"));

        BufferedImage img = ImageIO.read(new File(IMAGE_RELATIVE_PATH));
        BufferedImage scaledImg = Scalr.resize(img, Scalr.Method.QUALITY, 150, 100, Scalr.OP_ANTIALIAS);
    }
}
