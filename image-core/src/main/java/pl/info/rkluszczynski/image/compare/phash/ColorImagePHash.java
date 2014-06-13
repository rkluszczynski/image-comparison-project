package pl.info.rkluszczynski.image.compare.phash;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.Arrays;

/**
 * Created by Rafal on 2014-06-13.
 */
public class ColorImagePHash {
    private static final Logger logger = LoggerFactory.getLogger(ColorImagePHash.class);


    public static double[] calculateForRGB(Color imageArray1[][], Color imageArray2[][]) {
        double result[] = new double[3];
        int width = imageArray1.length;
        int height = imageArray1[0].length;

        for (int rgb = 0; rgb < 3; ++rgb) {
            double colorPlane1[][] = new double[width][height];
            double colorPlane2[][] = new double[width][height];

            for (int iw = 0; iw < width; ++iw) {
                for (int ih = 0; ih < height; ++ih) {
                    switch (rgb) {
                        case 0:
                            colorPlane1[iw][ih] = imageArray1[iw][ih].getRed();
                            colorPlane2[iw][ih] = imageArray2[iw][ih].getRed();
                            break;
                        case 1:
                            colorPlane1[iw][ih] = imageArray1[iw][ih].getGreen();
                            colorPlane2[iw][ih] = imageArray2[iw][ih].getGreen();
                            break;
                        case 2:
                            colorPlane1[iw][ih] = imageArray1[iw][ih].getBlue();
                            colorPlane2[iw][ih] = imageArray2[iw][ih].getBlue();
                            break;
                        default:
                            throw new RuntimeException();
                    }
                }
                result[rgb] = calculate(colorPlane1, colorPlane2);
            }
        }
        logger.info("PHash coefficients for RGB: {}", Arrays.toString(result));
        return result;
    }

    private static double calculate(double[][] colorPlane1, double[][] colorPlane2) {
        ImagePHash05 imagePHash = new ImagePHash05();


        return 0;
    }
}