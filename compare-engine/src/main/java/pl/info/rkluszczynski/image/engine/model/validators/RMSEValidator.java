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
    public MatchDecision validate(Color[][] patternArray, Color[][] subImageArray) {
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
            return MatchDecision.VALID_MATCH;
        } else if (matchValue < possibleMatchThreshold) {
            return MatchDecision.PROBABLY_MATCH;
        }
        return MatchDecision.NOT_A_CHANCE;
    }
}
