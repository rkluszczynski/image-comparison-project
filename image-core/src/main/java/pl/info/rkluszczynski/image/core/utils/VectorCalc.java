package pl.info.rkluszczynski.image.core.utils;

/**
 * Created by Rafal on 2014-06-22.
 */
final
public class VectorCalc {
    private VectorCalc() {
    }

    public static double calculateLength(double[] vector) {
        double sumOfSquaredCoordinates = vector[0] * vector[0];
        for (int i = 1; i < vector.length; ++i) {
            sumOfSquaredCoordinates += (vector[i] * vector[i]);
        }
        return Math.sqrt(sumOfSquaredCoordinates);
    }

    public static double calculateDotProduct(double[] vector1, double[] vector2) {
        // TODO: check vectors lengths
        double dotProduct = vector1[0] * vector2[0];
        for (int i = 1; i < vector1.length; ++i) {
            dotProduct += (vector1[i] * vector2[i]);
        }
        return dotProduct;
    }
}
