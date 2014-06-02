package pl.info.rkluszczynski.image.engine.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.info.rkluszczynski.image.engine.model.enums.ImageOrientation;
import pl.info.rkluszczynski.image.engine.model.enums.ImageSizesRatio;

import java.awt.image.BufferedImage;

import static pl.info.rkluszczynski.image.engine.model.enums.ImageOrientation.*;
import static pl.info.rkluszczynski.image.engine.model.enums.ImageSizesRatio.*;

final
public class ImageInformationResolver {
    private static final Logger logger = LoggerFactory.getLogger(ImageInformationResolver.class);

    private static final double ratioValueOf2x3 = 2. / 3.;
    private static final double ratioValueOf3x4 = 3. / 4.;
    private static final double ratioValueOf1x1 = 1.;

    private ImageInformationResolver() {
    }

    public static ImageSizesRatio detectClosestSizesRatio(BufferedImage image) {
        double smallerImageSize = (double) Math.min(image.getWidth(), image.getHeight());
        double biggerImageSize = (double) Math.max(image.getWidth(), image.getHeight());
        double sizesRatio = smallerImageSize / biggerImageSize;

        double diffWithRatio2x3 = Math.abs(ratioValueOf2x3 - sizesRatio);
        double diffWithRatio3x4 = Math.abs(ratioValueOf3x4 - sizesRatio);
        double diffWithRatio1x1 = Math.abs(ratioValueOf1x1 - sizesRatio);

        double minimalDiff = Math.min(
                Math.min(diffWithRatio2x3, diffWithRatio3x4), diffWithRatio1x1
        );

        ImageSizesRatio result = null;
        if (minimalDiff == diffWithRatio3x4) {
            result = RATIO_3_4;
        } else if (minimalDiff == diffWithRatio2x3) {
            result = RATIO_2_3;
        } else if (minimalDiff == diffWithRatio1x1) {
            result = RATIO_1_1;
        }
        assert result != null;
        logger.info("Detected image ratio {} with value {}", result.name(), sizesRatio);
        return result;
    }

    public static ImageOrientation detectImageOrientation(BufferedImage image) {
        ImageOrientation result;
        if (image.getWidth() == image.getHeight()) {
            result = SQUARE;
        } else if (image.getWidth() < image.getHeight()) {
            result = VERTICAL;
        } else {
            result = HORIZONTAL;
        }
        logger.info("Detected orientation {} for width {} and height {}", result.name(), image.getWidth(), image.getHeight());
        return result;
    }
}
