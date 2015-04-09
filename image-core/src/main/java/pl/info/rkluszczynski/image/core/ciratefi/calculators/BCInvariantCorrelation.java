package pl.info.rkluszczynski.image.core.ciratefi.calculators;

import java.util.Arrays;
import java.util.stream.IntStream;

public class BCInvariantCorrelation {

    public static double calculate(double values1[], double values2[]) {
        if (values1.length != values2.length) {
            throw new IllegalArgumentException("Values arrays length differs!");
        }

        double mean1 = Arrays.stream(values1)
                .average()
                .getAsDouble();
        double mean2 = Arrays.stream(values2)
                .average()
                .getAsDouble();

        double[] w1 = Arrays.stream(values1)
                .map(v -> v - mean1)
                .toArray();
        double[] w2 = Arrays.stream(values2)
                .map(v -> v - mean2)
                .toArray();

        double nw1 = Arrays.stream(w1)
                .map(w -> w * w)
                .sum();
        double nw2 = Arrays.stream(w2)
                .map(w -> w * w)
                .sum();

        double nominator = IntStream.range(0, values1.length)
                .mapToDouble(i -> w1[i] * w2[i])
                .sum();

        double result = nominator / (nw1 * nw2);
        return result;
    }
}
