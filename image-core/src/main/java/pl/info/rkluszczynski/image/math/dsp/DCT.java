package pl.info.rkluszczynski.image.math.dsp;

import java.util.stream.IntStream;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sqrt;

/* Awesome description of 2D DCT:
    http://www.developer.com/java/other/article.php/3634156/Understanding-the-2D-Discrete-Cosine-Transform-in-Java.htm
 */
final
public class DCT {
    private static final double inverseSqrt2 = 1. / sqrt(2);

    private DCT() {
    }

    public static double[] forward(double[] x) {
        // Outer loop interates on frequency values.
        return IntStream.range(0, x.length)
                .mapToDouble(k -> inner(k, x) * sqrt(2.0 / x.length))
                .toArray();
    }

    private static double inner(int k, double[] x) {
        // Inner loop iterates on time-series points.
        double sum = IntStream.range(0, x.length)
                .mapToDouble(i -> x[i] * cos(PI * k * (2. * i + 1) / (2 * x.length)))
                .sum();
        double alpha = (k == 0) ? inverseSqrt2 : 1.;
        return sum * alpha;
    }

    public static double[] inverse(double[] y) {
        int N = y.length;
        double[] x = new double[N];

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
        return x;
    }
}
