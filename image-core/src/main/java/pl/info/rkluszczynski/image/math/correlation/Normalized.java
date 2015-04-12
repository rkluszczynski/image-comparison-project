package pl.info.rkluszczynski.image.math.correlation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.stream.IntStream;

final
public class Normalized {

    public static double calculate(double values1[], double values2[]) {
        if (values1.length != values2.length) {
            throw new IllegalArgumentException("Values arrays MUST be of equal length!");
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

        double result = nominator / (Math.sqrt(nw1) * Math.sqrt(nw2));
        logger.debug("Normalized correlation value = {}", result);
        return result;
    }

    private Normalized() {
    }

    private static final Logger logger = LoggerFactory.getLogger(Normalized.class);
}
