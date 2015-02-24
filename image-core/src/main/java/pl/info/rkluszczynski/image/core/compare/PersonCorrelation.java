package pl.info.rkluszczynski.image.core.compare;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.Arrays;

/*

 Based on: http://www.dreamincode.net/forums/topic/126389-pearson-correlation-in-java/
 http://www.statisticshowto.com/what-is-the-pearson-correlation-coefficient/
 */
final
public class PersonCorrelation {
    private static final Logger logger = LoggerFactory.getLogger(PersonCorrelation.class);

    private PersonCorrelation() {
    }

    public static double calculate2(double values1[], double values2[]) {
        if (values1.length != values2.length) {
            throw new IllegalArgumentException("Values arrays length differs!");
        }
        int N = values1.length;
        double result;

        double sum_sq_x = 0;
        double sum_sq_y = 0;
        double sum_coproduct = 0;
        double mean_x = values1[0];
        double mean_y = values2[0];

        for (int i = 2; i < N + 1; ++i) {
            double sweep = (double) (i - 1) / i;
            double delta_x = values1[i - 1] - mean_x;
            double delta_y = values2[i - 1] - mean_y;
            sum_sq_x += delta_x * delta_x * sweep;
            sum_sq_y += delta_y * delta_y * sweep;
            sum_coproduct += delta_x * delta_y * sweep;
            mean_x += delta_x / i;
            mean_y += delta_y / i;
        }

        double pop_sd_x = Math.sqrt(sum_sq_x / N);
        double pop_sd_y = Math.sqrt(sum_sq_y / N);
        double cov_x_y = sum_coproduct / N;
        result = cov_x_y / (pop_sd_x * pop_sd_y);

        logger.trace("Pearson coefficient := {}", result);
        return result;
    }


    public static double calculate(double values1[], double values2[]) {
        if (values1.length != values2.length) {
            throw new IllegalArgumentException("Values arrays length differs!");
        }
        int N = values1.length;
        double result;

        double sum_x = 0;
        double sum_y = 0;
        double sum_x2 = 0;
        double sum_xy = 0;
        double sum_y2 = 0;

        for (int i = 0; i < N; ++i) {
            sum_x += values1[i];
            sum_y += values2[i];
            sum_x2 += (values1[i] * values1[i]);
            sum_xy += (values1[i] * values2[i]);
            sum_y2 += (values2[i] * values2[i]);
        }
        double nominator = N * sum_xy - sum_x * sum_y;
        double denominator2 = (N * sum_x2 - sum_x * sum_x) * (N * sum_y2 - sum_y * sum_y);
        result = nominator / Math.sqrt(denominator2);

        logger.trace("Pearson coefficient := {}", result);
        return result;
    }


    public static double[] calculateForRGB(Color imageArray1[], Color imageArray2[]) {
        if (imageArray1.length != imageArray2.length) {
            throw new IllegalArgumentException("Image arrays length differs!");
        }
        double result[] = new double[3];
        int pixelsCount = imageArray1.length;

        for (int rgb = 0; rgb < 3; ++rgb) {
            double scores1[] = new double[pixelsCount];
            double scores2[] = new double[pixelsCount];

            for (int i = 0; i < pixelsCount; ++i) {
                switch (rgb) {
                    case 0:
                        scores1[i] = imageArray1[i].getRed();
                        scores2[i] = imageArray2[i].getRed();
                        break;
                    case 1:
                        scores1[i] = imageArray1[i].getGreen();
                        scores2[i] = imageArray2[i].getGreen();
                        break;
                    case 2:
                        scores1[i] = imageArray1[i].getBlue();
                        scores2[i] = imageArray2[i].getBlue();
                        break;
                    default:
                        throw new RuntimeException();
                }
            }
            result[rgb] = calculate(scores1, scores2);
        }
        logger.debug("Pearson correlation coefficients for RGB: {}", Arrays.toString(result));
        return result;
    }
}
