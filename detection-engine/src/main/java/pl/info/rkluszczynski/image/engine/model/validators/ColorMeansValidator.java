package pl.info.rkluszczynski.image.engine.model.validators;

import pl.info.rkluszczynski.image.core.utils.VectorCalc;
import pl.info.rkluszczynski.image.engine.utils.ColorArrayHelper;
import pl.info.rkluszczynski.image.engine.utils.ColorPredicate;

import java.awt.*;

/**
 * Created by Rafal on 2014-06-22.
 */
public class ColorMeansValidator extends AbstractValidator {
    private static double validMatchCosineThreshold = 0.001;
    private static double possibleMatchCosineThreshold = 0.002;

    @Override
    public ValidationDecision validate(final Color[][] patternArray, Color[][] subImageArray) {
        double[] patternMeans = ColorArrayHelper.calculateColorsMeans(patternArray);
        double[] subImageMeans = ColorArrayHelper.calculateColorsMeans(subImageArray,
                new ColorPredicate<Color>() {
                    @Override
                    public boolean apply(Color input, int iw, int ih) {
                        assert input != null;
                        return patternArray[iw][ih] != null;
                    }
                }
        );

        double dotProduct = VectorCalc.calculateDotProduct(patternMeans, subImageMeans);
        double patternMeansLength = VectorCalc.calculateLength(patternMeans);
        double subImageMeansLength = VectorCalc.calculateLength(subImageMeans);

        double cosine = dotProduct / (patternMeansLength * subImageMeansLength);

        double matchValue = 1. - cosine;
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
}
