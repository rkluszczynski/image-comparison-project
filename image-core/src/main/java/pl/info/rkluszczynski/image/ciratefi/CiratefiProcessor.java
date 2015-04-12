package pl.info.rkluszczynski.image.ciratefi;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.stream.IntStream;

public class CiratefiProcessor {

    public CiratefiResult process(BufferedImage image, BufferedImage query, double[] scales, double[] angles) {
        ImageWrapper imageWrapper = new ImageWrapper(image);
        ImageWrapper queryWrapper = new ImageWrapper(query);

        ImageWrapper[] TQ = queryWrapper.getRotations(angles);


        return null;
    }

    public static void main(String[] args) throws IOException {
        String imgPath = "image-core/src/test/resources/image-data/";

        BufferedImage image = ImageIO.read(new File(imgPath + "zubr-image.png"));
        BufferedImage query = ImageIO.read(new File(imgPath + "zubr-pattern1-poster.png"));

        double[] angles = IntStream.range(0, 36).mapToDouble(i -> i * 10.0).toArray();
        double[] scales = {1.0};

        CiratefiResult result = new CiratefiProcessor().process(image, query, scales, angles);
        System.out.println(result);

        System.out.println("DONE");
    }
}
