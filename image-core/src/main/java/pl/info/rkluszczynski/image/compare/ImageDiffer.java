package pl.info.rkluszczynski.image.compare;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.info.rkluszczynski.image.compare.phash.HammingDistance;
import pl.info.rkluszczynski.image.compare.phash.ImagePHash05;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 * Created by Rafal on 2014-06-08.
 */
public class ImageDiffer {
    private static final Logger logger = LoggerFactory.getLogger(ImageDiffer.class);

    public static void calculateDifferStatistics(BufferedImage image1, BufferedImage image2) {
        Color[][] imageColorArray1 = convertImageToColorArray(image1);
        Color[][] imageColorArray2 = convertImageToColorArray(image2);

        Color[] imageColorOneDimensionalArray1 = convertArrayToOneDimension(imageColorArray1);
        Color[] imageColorOneDimensionalArray2 = convertArrayToOneDimension(imageColorArray2);

        double[] resultArray;
        double resultValue;

        resultArray = SampleCorrelation.calculateForRGB(imageColorOneDimensionalArray1, imageColorOneDimensionalArray2);
        logger.info("Sample Correlation Coefficients for RGB: {}", Arrays.toString(resultArray));

        resultArray = PersonCorrelation.calculateForRGB(imageColorOneDimensionalArray1, imageColorOneDimensionalArray2);
        logger.info("Person Correlation Coefficients for RGB: {}", Arrays.toString(resultArray));

        resultValue = calculatePHash(image1, image2);
        logger.info("GrayScale PHash distance: {}", resultValue);


    }

    private static double calculatePHash(BufferedImage image1, BufferedImage image2) {
        ImagePHash05 imagePHash = new ImagePHash05();
        String pHash1 = imagePHash.getHash(image1);
        String pHash2 = imagePHash.getHash(image2);
        return HammingDistance.calculate(pHash1, pHash2);
    }

    private static Color[] convertArrayToOneDimension(Color[][] imageColorArray) {
        int N = imageColorArray.length * imageColorArray[0].length;
        Color[] oneDimensionalColorArray = new Color[N];
        for (int iw = 0; iw < imageColorArray.length; ++iw) {
            for (int ih = 0; ih < imageColorArray[0].length; ++ih) {
                oneDimensionalColorArray[iw * imageColorArray.length + ih] = imageColorArray[iw][ih];
            }
        }
        return oneDimensionalColorArray;
    }

    private static Color[][] convertImageToColorArray(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        Color[][] imageColorArray = new Color[width][height];
        for (int iw = 0; iw < width; ++iw) {
            for (int ih = 0; ih < height; ++ih) {
                imageColorArray[iw][ih] = new Color(image.getRGB(iw, ih));
            }
        }
        return imageColorArray;
    }
}
