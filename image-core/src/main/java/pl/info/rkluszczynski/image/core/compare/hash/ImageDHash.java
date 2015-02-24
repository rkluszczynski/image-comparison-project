package pl.info.rkluszczynski.image.core.compare.hash;

import org.imgscalr.Scalr;
import pl.info.rkluszczynski.image.core.utils.ImageHelper;

import java.awt.*;
import java.awt.image.BufferedImage;

/*
    http://www.hackerfactor.com/blog/index.php?/archives/529-Kind-of-Like-That.html
 */
public class ImageDHash implements AbstractHash {
    private static int reducedImageHeightSize = 8;
    private static int reducedImageWidthSize = reducedImageHeightSize + 1;

    @Override
    public String getGrayScaleHash(BufferedImage bufferedImage) {
        /* 1. Reduce smallImageSize to 9x8 */
        BufferedImage reducedSizeImage = preProcessImage(bufferedImage);

		/* 2. Reduce colors */
        BufferedImage grayScaleImage = ImageHelper.convertImageToGrayScale(reducedSizeImage);

        /* Prepare color plane */
        double[][] imageColorPlane = new double[reducedImageWidthSize][reducedImageHeightSize];
        for (int iw = 0; iw < grayScaleImage.getWidth(); ++iw) {
            for (int ih = 0; ih < grayScaleImage.getHeight(); ++ih) {
                Color pixelColor = new Color(grayScaleImage.getRGB(iw, ih));
                imageColorPlane[iw][ih] = pixelColor.getGreen();
            }
        }
        return getColorPlaneHash(imageColorPlane);
    }

    private BufferedImage preProcessImage(BufferedImage bufferedImage) {
        /* 1. Reduce smallImageSize to 9x8 */
        return Scalr.resize(bufferedImage,
                Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_EXACT, reducedImageWidthSize, reducedImageHeightSize);
    }

    @Override
    public String[] getColorHashes(BufferedImage bufferedImage) {
        String[] result = new String[3];

        /* 1. Reduce smallImageSize to 8x8 */
        BufferedImage smallImage = preProcessImage(bufferedImage);

		/* 2. Iterate over colors */
        for (int rgb = 0; rgb < 3; ++rgb) {
            /* Prepare color plane */
            double[][] imageColorPlane = new double[reducedImageWidthSize][reducedImageHeightSize];

            for (int iw = 0; iw < reducedImageWidthSize; ++iw) {
                for (int ih = 0; ih < reducedImageHeightSize; ++ih) {
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
        return "DHash";
    }

    private String getColorPlaneHash(double[][] imageColorPlane) {
        /* 3. Compute hash bits */
        String imageHash = "";
        for (int x = 1; x < reducedImageWidthSize; x++) {
            for (int y = 0; y < reducedImageHeightSize; y++) {
                imageHash += (imageColorPlane[x][y] > imageColorPlane[x - 1][y] ? "1" : "0");
            }
        }
        /* 4. Return constructed hash */
        return imageHash;
    }
}

