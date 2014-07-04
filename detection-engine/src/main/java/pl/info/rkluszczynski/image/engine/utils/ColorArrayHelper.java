package pl.info.rkluszczynski.image.engine.utils;

import java.awt.*;

/**
 * Created by Rafal on 2014-06-22.
 */
final
public class ColorArrayHelper {
    public static double[] calculateColorsMeans(Color[][] colorArray) {
        return calculateColorsMeans(colorArray, new ColorPredicate<Color>() {
            @Override
            public boolean apply(Color input, int iw, int ih) {
                return input != null;
            }
        });
    }

    public static double[] calculateColorsMeans(Color[][] colorArray, ColorPredicate<? super Color> predicate) {
        double redSum = 0.;
        double greenSum = 0.;
        double blueSum = 0.;
        double validColorPointsAmount = 0.;

        for (int iw = 0; iw < colorArray.length; ++iw) {
            for (int ih = 0; ih < colorArray[0].length; ++ih) {
                Color color = colorArray[iw][ih];
                if (predicate.apply(color, iw, ih)) {
                    redSum += color.getRed();
                    greenSum += color.getGreen();
                    blueSum += color.getBlue();
                    ++validColorPointsAmount;
                }
            }
        }
        double[] colorMeans = new double[3];
        colorMeans[0] = redSum / validColorPointsAmount;
        colorMeans[1] = greenSum / validColorPointsAmount;
        colorMeans[2] = blueSum / validColorPointsAmount;
        return colorMeans;
    }

    public static double[] calculateColorsStddev(Color[][] colorArray) {
        double[] colorMeans = calculateColorsMeans(colorArray);
        double redSquaredSum = 0.;
        double greenSquaredSum = 0.;
        double blueSquaredSum = 0.;
        double validColorPointsAmount = 0.;

        for (int iw = 0; iw < colorArray.length; ++iw) {
            for (int ih = 0; ih < colorArray[0].length; ++ih) {
                Color color = colorArray[iw][ih];
                if (color != null) {
                    redSquaredSum += ((color.getRed() - colorMeans[0]) * (color.getRed() - colorMeans[0]));
                    greenSquaredSum += ((color.getGreen() - colorMeans[1]) * (color.getGreen() - colorMeans[1]));
                    blueSquaredSum += ((color.getBlue() - colorMeans[2]) * (color.getBlue() - colorMeans[2]));
                    ++validColorPointsAmount;
                }
            }
        }
        double[] colorStddevs = new double[3];
        colorStddevs[0] = Math.sqrt(redSquaredSum / validColorPointsAmount);
        colorStddevs[1] = Math.sqrt(greenSquaredSum / validColorPointsAmount);
        colorStddevs[2] = Math.sqrt(blueSquaredSum / validColorPointsAmount);
        return colorStddevs;
    }
}
