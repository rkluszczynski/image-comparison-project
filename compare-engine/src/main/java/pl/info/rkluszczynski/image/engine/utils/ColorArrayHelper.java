package pl.info.rkluszczynski.image.engine.utils;

import java.awt.*;

/**
 * Created by Rafal on 2014-06-22.
 */
final
public class ColorArrayHelper {
    public static double[] calculateColorsMeans(Color[][] colorArray) {
        double redSum = 0.;
        double greenSum = 0.;
        double blueSum = 0.;
        double validColorPointsAmount = 0.;

        for (int iw = 0; iw < colorArray.length; ++iw) {
            for (int ih = 0; ih < colorArray[0].length; ++ih) {
                Color color = colorArray[iw][ih];
                if (color != null) {
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
}
