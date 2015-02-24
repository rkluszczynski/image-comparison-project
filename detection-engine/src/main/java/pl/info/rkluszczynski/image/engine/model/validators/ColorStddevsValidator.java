package pl.info.rkluszczynski.image.engine.model.validators;

import pl.info.rkluszczynski.image.core.utils.VectorCalc;
import pl.info.rkluszczynski.image.engine.utils.ColorArrayHelper;

import java.awt.*;

/**
 * Created by Rafal on 2014-06-22.
 */
public class ColorStddevsValidator extends AbstractValidator {
    private static double validMatchCosineThreshold = 0.0025;
    private static double possibleMatchCosineThreshold = 0.02;

    @Override
    public ValidationDecision validate(Color[][] patternArray, Color[][] subImageArray) {
        double[] patternStddevs = ColorArrayHelper.calculateColorsStddev(patternArray);
        double[] subImageStddevs = calculateSubImageStddevsForPattern(subImageArray, patternArray);

        double dotProduct = VectorCalc.calculateDotProduct(patternStddevs, subImageStddevs);
        double patternMeansLength = VectorCalc.calculateLength(patternStddevs);
        double subImageMeansLength = VectorCalc.calculateLength(subImageStddevs);

        double cosine = dotProduct / (patternMeansLength * subImageMeansLength);

        double matchValue = 1 - cosine;
        if (matchValue < validMatchCosineThreshold) {
            return new ValidationDecision(ValidationDecision.MatchDecision.VALID_MATCH, matchValue);
        } else if (matchValue < possibleMatchCosineThreshold) {
            return new ValidationDecision(ValidationDecision.MatchDecision.POSSIBLE_MATCH, matchValue);
        }
        return new ValidationDecision(ValidationDecision.MatchDecision.NO_CLEAR_MATCH, matchValue);
    }

    private double[] calculateSubImageStddevsForPattern(Color[][] subImageArray, Color[][] patternArray) {
        double[] colorMeans = calculateSubImageMeansForPattern(subImageArray, patternArray);
        double redSquaredSum = 0.;
        double greenSquaredSum = 0.;
        double blueSquaredSum = 0.;
        double validColorPointsAmount = 0.;

        for (int iw = 0; iw < subImageArray.length; ++iw) {
            for (int ih = 0; ih < subImageArray[0].length; ++ih) {
                if (patternArray[iw][ih] != null) {
                    Color color = subImageArray[iw][ih];
                    redSquaredSum += ((color.getRed() - colorMeans[0]) * (color.getRed() - colorMeans[0]));
                    greenSquaredSum += ((color.getGreen() - colorMeans[1]) * (color.getGreen() - colorMeans[1]));
                    blueSquaredSum += ((color.getBlue() - colorMeans[2]) * (color.getBlue() - colorMeans[2]));
                    ++validColorPointsAmount;
                }
            }
        }
        double[] colorStddevs = new double[3];
        colorStddevs[0] = Math.sqrt(redSquaredSum / validColorPointsAmount);
        colorStddevs[1] = Math.sqrt(greenSquaredSum / validColorPointsAmount);
        colorStddevs[2] = Math.sqrt(blueSquaredSum / validColorPointsAmount);
        return colorStddevs;
    }

    @Override
    public String getName() {
        return "ColorStddevs";
    }

    private double[] calculateSubImageMeansForPattern(Color[][] subImageArray, Color[][] patternArray) {
        double redSum = 0.;
        double greenSum = 0.;
        double blueSum = 0.;
        double validColorPointsAmount = 0.;

        for (int iw = 0; iw < subImageArray.length; ++iw) {
            for (int ih = 0; ih < subImageArray[0].length; ++ih) {
                if (patternArray[iw][ih] != null) {
                    Color color = subImageArray[iw][ih];
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
