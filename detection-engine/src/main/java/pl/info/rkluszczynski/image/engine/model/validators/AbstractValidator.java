package pl.info.rkluszczynski.image.engine.model.validators;

import pl.info.rkluszczynski.image.engine.utils.BufferedImageConverter;
import pl.info.rkluszczynski.image.engine.utils.BufferedImageWrapper;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Rafal on 2014-06-22.
 */
abstract class AbstractValidator implements MatchValidator {

    @Override
    public ValidationDecision validate(BufferedImageWrapper patternImage, BufferedImage matchSubImage) {
        Color[][] colorArray1 = BufferedImageConverter.convertBufferedImageToColorArray(patternImage);
        Color[][] colorArray2 = BufferedImageConverter.convertBufferedImageToColorArray(matchSubImage);
        return validate(colorArray1, colorArray2);
    }
}
