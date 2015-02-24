package pl.info.rkluszczynski.image.engine.model.validators;

import pl.info.rkluszczynski.image.engine.utils.BufferedImageWrapper;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Rafal on 2014-06-22.
 */
public interface MatchValidator {

    ValidationDecision validate(BufferedImageWrapper patternImage, BufferedImage imageMatch);

    ValidationDecision validate(Color[][] patternArray, Color[][] subImageArray);

    String getName();

}
