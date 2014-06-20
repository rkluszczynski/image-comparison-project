package pl.info.rkluszczynski.image.core.compare.hash;

import com.google.common.collect.Lists;
import org.imgscalr.Scalr;
import pl.info.rkluszczynski.image.core.utils.ImageHelper;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

/*
    http://www.hackerfactor.com/blog/index.php?/archives/432-Looks-Like-It.html
 */
public class ImageAHash implements AbstractHash {
    private static int reducedImageSize = 8;

    @Override
    public String getGrayScaleHash(BufferedImage bufferedImage) {
        /* 1. Reduce smallImageSize to 8x8 */
        BufferedImage smallImage = preProcessImage(bufferedImage);

		/* 2. Reduce colors */
        BufferedImage reducedImage = ImageHelper.convertImageToGrayScale(smallImage);

        /* Prepare color plane */
        double[][] imageColorPlane = new double[reducedImageSize][reducedImageSize];
        for (int iw = 0; iw < reducedImage.getWidth(); ++iw) {
            for (int ih = 0; ih < reducedImage.getHeight(); ++ih) {
                Color pixelColor = new Color(reducedImage.getRGB(iw, ih));
                imageColorPlane[iw][ih] = pixelColor.getGreen();
            }
        }
        return getColorPlaneHash(imageColorPlane);
    }

    private BufferedImage preProcessImage(BufferedImage bufferedImage) {
        /* 1. Reduce smallImageSize to 8x8 */
        return Scalr.resize(bufferedImage,
                Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_EXACT, reducedImageSize, reducedImageSize);
    }

    @Override
    public String[] getColorHashes(BufferedImage bufferedImage) {
        String[] result = new String[3];

        /* 1. Reduce smallImageSize to 8x8 */
        BufferedImage smallImage = preProcessImage(bufferedImage);

		/* 2. Iterate over colors */
        for (int rgb = 0; rgb < 3; ++rgb) {
        /* Prepare color plane */
            double[][] imageColorPlane = new double[reducedImageSize][reducedImageSize];

            for (int iw = 0; iw < reducedImageSize; ++iw) {
                for (int ih = 0; ih < reducedImageSize; ++ih) {
                    Color pixelColor = new Color(smallImage.getRGB(iw, ih));
                    int colorValue = -1;
                    switch (rgb) {
                        case 0:
                            colorValue = pixelColor.getRed();
                            break;
                        case 1:
                            colorValue = pixelColor.getGreen();
                            break;
                        case 2:
                            colorValue = pixelColor.getBlue();
                            break;
                        default:
                            throw new RuntimeException();
                    }
                    imageColorPlane[iw][ih] = colorValue;
                }
            }
            result[rgb] = getColorPlaneHash(imageColorPlane);
        }
        return result;
    }

    @Override
    public String getHashName() {
        return "AHash";
    }

    private String getColorPlaneHash(double[][] imageColorPlane) {
        double threshold = 0.;

		/* 3. Compute the threshold value */
        ArrayList<Double> list = Lists.newArrayList();
        double sum = 0.;
        for (int x = 0; x < reducedImageSize; x++) {
            for (int y = 0; y < reducedImageSize; y++) {
                list.add(imageColorPlane[x][y]);
                sum += imageColorPlane[x][y];
            }
        }
        Collections.sort(list);
//        threshold = list.get(reducedImageSize * reducedImageSize / 2); // MEDIAN
        threshold = sum / (reducedImageSize * reducedImageSize); // MEAN

		/* 4. Compute hash bits */
        String imageHash = "";
        for (int x = 0; x < reducedImageSize; x++) {
            for (int y = 0; y < reducedImageSize; y++) {
//                if (x != 0 && y != 0)
                imageHash += (imageColorPlane[x][y] > threshold ? "1" : "0");
            }
        }

        /* 5. Return constructed hash */
        return imageHash;
    }
}

