package pl.info.rkluszczynski.test.imgscalr;


import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TestApp {

    public static void main(String[] args) throws IOException {
        System.out.println(System.getProperty("user.dir"));

        BufferedImage img = ImageIO.read(new File("testing-imgscalr/src/main/resources/image.jpg"));
        BufferedImage scaledImg = Scalr.resize(img, Scalr.Method.QUALITY, 150, 100, Scalr.OP_ANTIALIAS);
    }
}
