package pl.info.rkluszczynski.image.dsp;

import static java.lang.Math.*;

/* Awesome description of 2D DCT:
    http://www.developer.com/java/other/article.php/3634156/Understanding-the-2D-Discrete-Cosine-Transform-in-Java.htm
 */
final
public class DCT {
    private static final double inverseSqrt2 = 1. / sqrt(2);

    private DCT() {
    }

    public static void forward(double[] x, double[] y) {
        int N = x.length;

        // Outer loop interates on frequency values.
        for (int k = 0; k < N; k++) {
            double sum = 0.0;

            // Inner loop iterates on time-series points.
            for (int n = 0; n < N; ++n) {
                double arg = PI * k * (2.0 * n + 1) / (2 * N);
                double cosine = cos(arg);
                double product = x[n] * cosine;
                sum += product;
            } // end inner loop

            double alpha = (k == 0) ? inverseSqrt2 : 1.;
            y[k] = sum * alpha * sqrt(2.0 / N);
        } // end outer loop
    }

    public static void inverse(double[] y, double[] x) {
        int N = y.length;

        // Outer loop interates on time values.
        for (int n = 0; n < N; ++n) {
            double sum = 0.0;

            // Inner loop iterates on frequency values
            for (int k = 0; k < N; k++) {
                double arg = PI * k * (2.0 * n + 1) / (2 * N);
                double cosine = cos(arg);
                double product = y[k] * cosine;

                double alpha = (k == 0) ? inverseSqrt2 : 1.;
                sum += alpha * product;
            } // end inner loop
            x[n] = sum * sqrt(2.0 / N);
        } // end outer loop
    }
}
