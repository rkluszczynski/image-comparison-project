package pl.info.rkluszczynski.image.engine.model.validators;

import pl.info.rkluszczynski.image.engine.utils.BufferedImageWrapper;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Rafal on 2014-06-22.
 */
public interface MatchValidator {

    MatchDecision validate(BufferedImageWrapper patternImage, BufferedImage imageMatch);

    MatchDecision validate(Color[][] patternArray, Color[][] subImageArray);

}
