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
        for (int iw = 0; iw < imageColorArray1.length; ++iw)
            for (int ih = 0; ih < imageColorArray1[0].length; ++ih)
                if (imageColorArray1[iw][ih] == null) throw new RuntimeException();
        Color[][] imageColorArray2 = convertImageToColorArray(image2);
        for (int iw = 0; iw < imageColorArray2.length; ++iw)
            for (int ih = 0; ih < imageColorArray2[0].length; ++ih)
                if (imageColorArray2[iw][ih] == null) throw new RuntimeException();

        Color[] imageColorOneDimensionalArray1 = convertArrayToOneDimension(imageColorArray1);
        for (int i = 0; i < imageColorArray1.length; ++i) {
            if (imageColorArray1[i] == null) throw new RuntimeException();
        }
        Color[] imageColorOneDimensionalArray2 = convertArrayToOneDimension(imageColorArray2);
        for (int i = 0; i < imageColorArray2.length; ++i) {
            if (imageColorArray2[i] == null) throw new RuntimeException();
        }

        double[] resultArray;
        double resultValue;

        resultArray = SampleCorrelation.calculateForRGB(imageColorOneDimensionalArray1, imageColorOneDimensionalArray2);
        logger.info("Sample Correlation Coefficients for RGB: {}", Arrays.toString(resultArray));

        resultArray = PersonCorrelation.calculateForRGB(imageColorOneDimensionalArray1, imageColorOneDimensionalArray2);
        logger.info("Person Correlation Coefficients for RGB: {}", Arrays.toString(resultArray));

        resultValue = calculateGrayScalePHash(image1, image2);
        logger.info("GrayScale PHash distance: {}", resultValue);

        resultArray = calculateColorPHashes(image1, image2);
        logger.info("Color PHashes distances for RGB: {}", Arrays.toString(resultArray));
    }

    private static double[] calculateColorPHashes(BufferedImage image1, BufferedImage image2) {
        ImagePHash05 imagePHash = new ImagePHash05();
        String[] pHashes1 = imagePHash.getColorHashes(image1);
        String[] pHashes2 = imagePHash.getColorHashes(image2);
        double[] result = new double[3];
        for (int i = 0; i < 3; ++i)
            result[i] = HammingDistance.calculate(pHashes1[i], pHashes2[i]);
        return result;
    }

    private static double calculateGrayScalePHash(BufferedImage image1, BufferedImage image2) {
        ImagePHash05 imagePHash = new ImagePHash05();
        String pHash1 = imagePHash.getGrayScaleHash(image1);
        String pHash2 = imagePHash.getGrayScaleHash(image2);
        return HammingDistance.calculate(pHash1, pHash2);
    }

    private static Color[] convertArrayToOneDimension(Color[][] imageColorArray) {
        int N = imageColorArray.length * imageColorArray[0].length;
        Color[] oneDimensionalColorArray = new Color[N];
        for (int iw = 0; iw < imageColorArray.length; ++iw) {
            int columnHeight = imageColorArray[0].length;
            for (int ih = 0; ih < columnHeight; ++ih) {
                oneDimensionalColorArray[iw * columnHeight + ih] =
                        new Color(imageColorArray[iw][ih].getRGB());
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
