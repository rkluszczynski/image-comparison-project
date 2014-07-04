package pl.info.rkluszczynski.image.engine.model.validators;

import pl.info.rkluszczynski.image.core.utils.VectorCalc;
import pl.info.rkluszczynski.image.engine.utils.ColorArrayHelper;

import java.awt.*;

/**
 * Created by Rafal on 2014-06-22.
 */
public class ColorMeansValidator extends AbstractValidator {
    private static double validMatchCosineThreshold = 0.001;
    private static double possibleMatchCosineThreshold = 0.002;

    @Override
    public ValidationDecision validate(Color[][] patternArray, Color[][] subImageArray) {
        double[] patternMeans = ColorArrayHelper.calculateColorsMeans(patternArray);
        double[] subImageMeans = calculateSubImageMeansForPattern(subImageArray, patternArray);

        double dotProduct = VectorCalc.calculateDotProduct(patternMeans, subImageMeans);
        double patternMeansLength = VectorCalc.calculateLength(patternMeans);
        double subImageMeansLength = VectorCalc.calculateLength(subImageMeans);

        double cosine = dotProduct / (patternMeansLength * subImageMeansLength);

        double matchValue = 1 - cosine;
        if (matchValue < validMatchCosineThreshold) {
            return new ValidationDecision(ValidationDecision.MatchDecision.VALID_MATCH, matchValue);
        } else if (matchValue < possibleMatchCosineThreshold) {
            return new ValidationDecision(ValidationDecision.MatchDecision.POSSIBLE_MATCH, matchValue);
        }
        return new ValidationDecision(ValidationDecision.MatchDecision.NO_CLEAR_MATCH, matchValue);
    }

    @Override
    public String getName() {
        return "ColorMeans";
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
