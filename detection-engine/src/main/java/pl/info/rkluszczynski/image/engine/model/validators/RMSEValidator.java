package pl.info.rkluszczynski.image.engine.model.validators;

import pl.info.rkluszczynski.image.engine.model.metrics.RMSEColorMetric;

import java.awt.*;

/**
 * Created by Rafal on 2014-06-22.
 */
public class RMSEValidator extends AbstractValidator {
    private static double validMatchThreshold = 0.25;
    private static double possibleMatchThreshold = 0.35;

    private RMSEColorMetric rmseColorMetric = new RMSEColorMetric();

    @Override
    public ValidationDecision validate(Color[][] patternArray, Color[][] subImageArray) {
        rmseColorMetric.resetValue();

        for (int iw = 0; iw < patternArray.length; ++iw) {
            for (int ih = 0; ih < patternArray[0].length; ++ih) {
                if (patternArray[iw][ih] != null) {
                    rmseColorMetric.addPixelsDifference(
                            patternArray[iw][ih],
                            subImageArray[iw][ih]
                    );
                }
            }
        }

        double matchValue = rmseColorMetric.calculateValue();
        if (matchValue < validMatchThreshold) {
            return new ValidationDecision(ValidationDecision.MatchDecision.VALID_MATCH, matchValue);
        } else if (matchValue < possibleMatchThreshold) {
            return new ValidationDecision(ValidationDecision.MatchDecision.PROBABLY_MATCH, matchValue);
        }
        return new ValidationDecision(ValidationDecision.MatchDecision.NOT_A_CHANCE, matchValue);
    }

    @Override
    public String getName() {
        return "RMSE";
    }
}
