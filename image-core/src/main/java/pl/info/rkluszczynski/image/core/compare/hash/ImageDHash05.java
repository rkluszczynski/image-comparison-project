package pl.info.rkluszczynski.image.core.compare.hash;

import org.imgscalr.Scalr;
import pl.info.rkluszczynski.image.core.utils.ImageHelper;

import java.awt.*;
import java.awt.image.BufferedImage;

/*
    My variation of DHash:

    http://www.hackerfactor.com/blog/index.php?/archives/529-Kind-of-Like-That.html
 */
public class ImageDHash05 implements AbstractHash {
    private static int reducedImageSize = 9;

    @Override
    public String getGrayScaleHash(BufferedImage bufferedImage) {
        /* 1. Reduce smallImageSize to 9x9 */
        BufferedImage reducedSizeImage = preProcessImage(bufferedImage);

		/* 2. Reduce colors */
        BufferedImage grayScaleImage = ImageHelper.convertImageToGrayScale(reducedSizeImage);

        /* Prepare color plane */
        double[][] imageColorPlane = new double[reducedImageSize][reducedImageSize];
        for (int iw = 0; iw < grayScaleImage.getWidth(); ++iw) {
            for (int ih = 0; ih < grayScaleImage.getHeight(); ++ih) {
                Color pixelColor = new Color(grayScaleImage.getRGB(iw, ih));
                imageColorPlane[iw][ih] = pixelColor.getGreen();
            }
        }
        return getColorPlaneHash(imageColorPlane);
    }

    @Override
    public String[] getColorHashes(BufferedImage bufferedImage) {
        String[] result = new String[3];

        /* 1. Reduce smallImageSize to 9x9 */
        BufferedImage reducedSizeImage = preProcessImage(bufferedImage);

		/* 2. Iterate over colors */
        for (int rgb = 0; rgb < 3; ++rgb) {
            /* Prepare color plane */
            double[][] imageColorPlane = new double[reducedImageSize][reducedImageSize];

            for (int iw = 0; iw < reducedImageSize; ++iw) {
                for (int ih = 0; ih < reducedImageSize; ++ih) {
                    Color pixelColor = new Color(reducedSizeImage.getRGB(iw, ih));
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
        return "DHash05";
    }

    private String getColorPlaneHash(double[][] imageColorPlane) {
        /* 3. Compute hash bits */
        String imageHash = "";
        for (int x = 1; x < reducedImageSize; x++) {
            for (int y = 1; y < reducedImageSize; y++) {
                imageHash += (imageColorPlane[x][y] > imageColorPlane[x - 1][y] ? "1" : "0");
                imageHash += (imageColorPlane[x][y] > imageColorPlane[x][y - 1] ? "1" : "0");
            }
        }
        /* 4. Return constructed hash */
        return imageHash;
    }

    private BufferedImage preProcessImage(BufferedImage bufferedImage) {
        /* 1. Reduce smallImageSize to 9x8 */
        return Scalr.resize(bufferedImage,
                Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_EXACT, reducedImageSize, reducedImageSize);
    }
}

