package pl.info.rkluszczynski.image.engine;

import JavaMI.MutualInformation;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.info.rkluszczynski.image.core.HistogramEQ;
import pl.info.rkluszczynski.image.core.compare.PersonCorrelation;
import pl.info.rkluszczynski.image.core.compare.SampleCorrelation;
import pl.info.rkluszczynski.image.core.compare.hash.*;
import pl.info.rkluszczynski.image.core.compare.metric.CompareMetric;
import pl.info.rkluszczynski.image.engine.model.metrics.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by Rafal on 2014-06-08.
 */
public class ImageDiffer {
    private static final Logger logger = LoggerFactory.getLogger(ImageDiffer.class);

    public static void calculateDifferStatistics(BufferedImage image1, BufferedImage image2) {
        calculateDifferStatistics(image1, image2, null, null);
    }

    public static void calculateDifferStatistics(BufferedImage image1, BufferedImage image2, List<String> entryColNames, List<Double> stats) {
        Locale.setDefault(Locale.ENGLISH);

        Color[][] imageColorArray1 = convertImageToColorArray(image1);
        Color[][] imageColorArray2 = convertImageToColorArray(image2);

        Color[] imageColorOneDimensionalArray1 = convertArrayToOneDimension(imageColorArray1);
        Color[] imageColorOneDimensionalArray2 = convertArrayToOneDimension(imageColorArray2);

        List<double[]> imageHistogramsList1 = HistogramEQ.imageHistogram(image1);
        List<double[]> imageHistogramsList2 = HistogramEQ.imageHistogram(image2);

        BufferedImage equalizedImage1 = HistogramEQ.histogramEqualization(image1, true);
        BufferedImage equalizedImage2 = HistogramEQ.histogramEqualization(image2, true);

        List<double[]> eqImageHistogramsList1 = HistogramEQ.imageHistogram(equalizedImage1);
        List<double[]> eqImageHistogramsList2 = HistogramEQ.imageHistogram(equalizedImage2);

        double[] resultArray;
        double resultValue;

        CompareMetric[] compareMetrics = {
                new AbsAveGrayScaleMetric(), new PSNRAveGrayScaleMetric(), new RMSEAveGrayScaleMetric(),
                new AbsColorMetric(), new ExpColorMetric(), new PSNRColorMetric(), new RMSEColorMetric(),
                new NRMSEColorMetric()
        };
        for (CompareMetric metric : compareMetrics) {
            logger.info(calculateMetricValue(imageColorArray1, imageColorArray2, metric, entryColNames, stats));
        }

        resultArray = SampleCorrelation.calculateForRGB(imageColorOneDimensionalArray1, imageColorOneDimensionalArray2);
        logger.info("  Sample Correlation Coefficients for RGB: {}", getResultArrayString(resultArray, "sampleCorr", entryColNames, stats));

        resultArray = PersonCorrelation.calculateForRGB(imageColorOneDimensionalArray1, imageColorOneDimensionalArray2);
        logger.info("  Person Correlation Coefficients for RGB: {}", getResultArrayString(resultArray, "personCorr", entryColNames, stats));

        AbstractHash[] abstractHashes = {
                new ImagePHash05(), new ImageAHash(), new ImageDHash(), new ImageDHash05()
        };
        for (AbstractHash hashCalculator : abstractHashes) {
            String hashName = hashCalculator.getHashName();

            resultValue = calculateGrayScalePHash(image1, image2, hashCalculator);
            logger.info(String.format("               GrayScale %7s distance: %s", hashName,
                    getResultValueString(resultValue, String.format("GS::%s", hashName), entryColNames, stats)));

            resultArray = calculateColorPHashes(image1, image2, hashCalculator);
            logger.info("          Color {} distances for RGB: {}", String.format("%7s", hashName),
                    getResultArrayString(resultArray, String.format("C::%s", hashName), entryColNames, stats));
        }

        resultArray = calculateMI4RGB(imageColorOneDimensionalArray1, imageColorOneDimensionalArray2);
        logger.info("        Mutual Information values for RGB: {}", getResultArrayString(resultArray, "MI", entryColNames, stats));

        resultArray = HistogramDiffer.processColorSampleCorr(imageHistogramsList1, imageHistogramsList2);
        logger.info("    Histogram Sample Corr. Coeff. for RGB: {}", getResultArrayString(resultArray, "C::histSampleCorr", entryColNames, stats));

        resultValue = HistogramDiffer.processGSSampleCorr(imageHistogramsList1, imageHistogramsList2);
        logger.info("     Histogram Sample Corr. Coeff. for GS: {}", getResultValueString(resultValue, "GS::histSampleCorr", entryColNames, stats));

        resultArray = HistogramDiffer.processColorPersonCorr(imageHistogramsList1, imageHistogramsList2);
        logger.info("    Histogram Person Corr. Coeff. for RGB: {}", getResultArrayString(resultArray, "C::histPersonCorr", entryColNames, stats));

        resultValue = HistogramDiffer.processGSPersonCorr(imageHistogramsList1, imageHistogramsList2);
        logger.info("     Histogram Person Corr. Coeff. for GS: {}", getResultValueString(resultValue, "GS::histPersonCorr", entryColNames, stats));

        resultArray = HistogramDiffer.processColorSampleCorr(eqImageHistogramsList1, eqImageHistogramsList2);
        logger.info(" Eq.Histogram Sample Corr. Coeff. for RGB: {}", getResultArrayString(resultArray, "C::eqHistSampleCorr", entryColNames, stats));

        resultValue = HistogramDiffer.processGSSampleCorr(eqImageHistogramsList1, eqImageHistogramsList2);
        logger.info("  Eq.Histogram Sample Corr. Coeff. for GS: {}", getResultValueString(resultValue, "GS::eqHistSampleCorr", entryColNames, stats));

        resultArray = HistogramDiffer.processColorPersonCorr(eqImageHistogramsList1, eqImageHistogramsList2);
        logger.info(" Eq.Histogram Person Corr. Coeff. for RGB: {}", getResultArrayString(resultArray, "C::eqHistPersonCorr", entryColNames, stats));

        resultValue = HistogramDiffer.processGSPersonCorr(eqImageHistogramsList1, eqImageHistogramsList2);
        logger.info("  Eq.Histogram Person Corr. Coeff. for GS: {}", getResultValueString(resultValue, "GS::eqHistPersonCorr", entryColNames, stats));

        resultArray = HistogramDiffer.processColorMI(imageHistogramsList1, imageHistogramsList2);
        logger.info("                     Histogram MI for RGB: {}", getResultArrayString(resultArray, "C::histMI", entryColNames, stats));

        resultValue = HistogramDiffer.processGSSampleCorr(imageHistogramsList1, imageHistogramsList2);
        logger.info("                      Histogram MI for GS: {}", getResultValueString(resultValue, "GS::histMI", entryColNames, stats));

        resultArray = HistogramDiffer.processColorMI(eqImageHistogramsList1, eqImageHistogramsList2);
        logger.info("                  Eq.Histogram MI for RGB: {}", getResultArrayString(resultArray, "C::eqHistMI", entryColNames, stats));

        resultValue = HistogramDiffer.processGSSampleCorr(eqImageHistogramsList1, eqImageHistogramsList2);
        logger.info("                   Eq.Histogram MI for GS: {}", getResultValueString(resultValue, "GS::eqHistMI", entryColNames, stats));

        resultValue = ImageBlockDiffCalc.processImageBlockByBlock(image1, image2);
        if (entryColNames != null) {
            entryColNames.add("Block");
        }
        if (stats != null) {
            stats.add(resultValue);
        }
        logger.info(String.format("                 Block max distance: %.3f", resultValue));
    }

    private static String getResultValueString(double resultValue, String metricName, List<String> entryColNames, List<Double> stats) {
        if (entryColNames != null) {
            entryColNames.add(metricName);
        }
        if (stats != null) {
            stats.add(resultValue);
        }
        return String.format("%.3f", resultValue);
    }

    private static String calculateMetricValue(Color[][] imageArray1, Color[][] imageArray2, CompareMetric metric, List<String> entryColNames, List<Double> stats) {
        metric.resetValue();
        for (int iw = 0; iw < imageArray1.length; ++iw) {
            int columnHeight = imageArray1[0].length;
            for (int ih = 0; ih < columnHeight; ++ih) {
                metric.addPixelsDifference(imageArray1[iw][ih], imageArray2[iw][ih]);
            }
        }
        double value = metric.calculateValue();
        if (entryColNames != null) {
            entryColNames.add(metric.getName());
        }
        if (stats != null) {
            stats.add(value);
        }
        return
                String.format("                  Metric %10s value: %.3f", metric.getName(), value);
    }

    private static String getResultArrayString(double[] resultArray, String metricName, List<String> entryColNames, List<Double> stats) {
        List<Double> resultsList = Lists.newArrayList();
        double resultSum = 0;
        for (int i = 0; i < resultArray.length; ++i) {
            resultsList.add(new Double(resultArray[i]));
            resultSum += resultArray[i];
        }
        double resultAvg = resultSum / resultArray.length;

        Collections.sort(resultsList);
        double median = resultsList.get(resultsList.size() / 2);
        if (resultsList.size() % 2 == 0) {
            median += resultsList.get(resultsList.size() / 2 - 1);
            median /= 2;
        }

        if (entryColNames != null) {
            for (int i = 0; i < resultArray.length; ++i) {
                entryColNames.add(String.format("%s[%d]", metricName, i));
            }
            entryColNames.add(String.format("%s::AVG", metricName));
            entryColNames.add(String.format("%s::Median", metricName));
        }
        if (stats != null) {
            for (int i = 0; i < resultArray.length; ++i) {
                stats.add(new Double(resultArray[i]));
            }
            stats.add(resultAvg);
            stats.add(median);
        }
        return String.format("%s, mean=%.3f, median=%.3f", covertArrayToString(resultArray), resultAvg, median);
    }

    private static String covertArrayToString(double[] a) {
        if (a == null)
            return "null";
        int iMax = a.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            b.append(String.format("%.3f", a[i]));
            if (i == iMax)
                return b.append(']').toString();
            b.append(", ");
        }
    }

    private static double[] calculateMI4RGB(Color[] imageArray1, Color[] imageArray2) {
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
            result[rgb] = MutualInformation.calculateMutualInformation(scores1, scores2);
        }
        logger.debug("Mutual Information values for RGB: {}", Arrays.toString(result));
        return result;
    }

    private static double[] calculateColorPHashes(BufferedImage image1, BufferedImage image2, AbstractHash hashCalc) {
        String[] pHashes1 = hashCalc.getColorHashes(image1);
        String[] pHashes2 = hashCalc.getColorHashes(image2);
        double maxValue = pHashes1[0].length();
        double[] result = new double[3];
        for (int i = 0; i < 3; ++i)
            result[i] = HammingDistance.calculate(pHashes1[i], pHashes2[i]) / maxValue;
        return result;
    }

    private static double calculateGrayScalePHash(BufferedImage image1, BufferedImage image2, AbstractHash hashCalc) {
        String pHash1 = hashCalc.getGrayScaleHash(image1);
        String pHash2 = hashCalc.getGrayScaleHash(image2);
        double maxValue = pHash1.length();
        return HammingDistance.calculate(pHash1, pHash2) / maxValue;
    }

    private static Color[] convertArrayToOneDimension(Color[][] imageColorArray) {
        int N = imageColorArray.length * imageColorArray[0].length;
        Color[] oneDimensionalColorArray = new Color[N];
        for (int iw = 0; iw < imageColorArray.length; ++iw) {
            int columnHeight = imageColorArray[0].length;
            for (int ih = 0; ih < columnHeight; ++ih) {
                oneDimensionalColorArray[iw * columnHeight + ih] =
                        new Color(imageColorArray[iw][ih].getRGB());
            }
        }
        return oneDimensionalColorArray;
    }

    public static Color[][] convertImageToColorArray(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        Color[][] imageColorArray = new Color[width][height];
        for (int iw = 0; iw < width; ++iw) {
            for (int ih = 0; ih < height; ++ih) {
                imageColorArray[iw][ih] = new Color(image.getRGB(iw, ih));
            }
        }
        return imageColorArray;
    }
}
