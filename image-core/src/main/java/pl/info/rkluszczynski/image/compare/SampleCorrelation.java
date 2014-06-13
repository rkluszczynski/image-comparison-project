package pl.info.rkluszczynski.image.compare;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DIC (Digital Image Correlation):
 * <p/>
 * http://www.statisticshowto.com/what-is-the-correlation-coefficient-formula/
 */
final
public class SampleCorrelation {
    private static final Logger logger = LoggerFactory.getLogger(SampleCorrelation.class);

    private SampleCorrelation() {
    }

    public static double calculate(double values1[], double values2[]) {
        if (values1.length != values2.length) {
            throw new IllegalArgumentException("Values arrays length differs!");
        }
        int N = values1.length;
        double result;

        double sum_values1 = 0;
        double sum_values2 = 0;
        for (int i = 0; i < N; ++i) {
            sum_values1 += values1[i];
            sum_values2 += values2[i];
        }
        double mean1 = sum_values1 / N;
        double mean2 = sum_values2 / N;

        double sum_diff_values1 = 0;
        double sum_diff_values2 = 0;
        double sum_diff_product = 0;
        for (int i = 0; i < N; ++i) {
            sum_diff_values1 += ((values1[i] - mean1) * (values1[i] - mean1));
            sum_diff_values2 += ((values2[i] - mean2) * (values2[i] - mean2));
            sum_diff_product += ((values1[i] - mean1) * (values2[i] - mean2));
        }
        double covariance = sum_diff_product / (N - 1);
        double stddev1 = Math.sqrt(sum_diff_values1 / (N - 1));
        double stddev2 = Math.sqrt(sum_diff_values2 / (N - 1));

        result = covariance / (stddev1 * stddev2);
        logger.info("sample correlation coefficient := {}", result);
        return result;
    }
}
