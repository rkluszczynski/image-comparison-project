package pl.info.rkluszczynski.image.engine;

import JavaMI.MutualInformation;
import pl.info.rkluszczynski.image.core.compare.SampleCorrelation;

import java.util.List;

/**
 * Created by Rafal on 2014-06-21.
 */
public class HistogramDiffer {
    public static double[] processColorMI(List<double[]> imageHistogramsList1, List<double[]> imageHistogramsList2) {
        double result[] = new double[3];
        for (int i = 0; i < 3; ++i) {
            result[i] = MutualInformation.calculateMutualInformation(imageHistogramsList1.get(i), imageHistogramsList2.get(i));
        }
        return result;
    }

    public static double processGrayScaleMI(List<double[]> imageHistogramsList1, List<double[]> imageHistogramsList2) {
        return MutualInformation.calculateMutualInformation(imageHistogramsList1.get(3), imageHistogramsList2.get(3));
    }

    public static double[] processColorSampleCorr(List<double[]> imageHistogramsList1, List<double[]> imageHistogramsList2) {
        double result[] = new double[3];
        for (int i = 0; i < 3; ++i) {
            result[i] = SampleCorrelation.calculate(imageHistogramsList1.get(i), imageHistogramsList2.get(i));
        }
        return result;
    }

    public static double processGSSampleCorr(List<double[]> imageHistogramsList1, List<double[]> imageHistogramsList2) {
        return SampleCorrelation.calculate(imageHistogramsList1.get(3), imageHistogramsList2.get(3));
    }

    public static double[] processColorPersonCorr(List<double[]> imageHistogramsList1, List<double[]> imageHistogramsList2) {
        double result[] = new double[3];
        for (int i = 0; i < 3; ++i) {
            result[i] = SampleCorrelation.calculate(imageHistogramsList1.get(i), imageHistogramsList2.get(i));
        }
        return result;
    }

    public static double processGSPersonCorr(List<double[]> imageHistogramsList1, List<double[]> imageHistogramsList2) {
        return SampleCorrelation.calculate(imageHistogramsList1.get(3), imageHistogramsList2.get(3));
    }
}
