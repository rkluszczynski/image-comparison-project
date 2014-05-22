package pl.info.rkluszczynski.image.engine.utils;

import pl.info.rkluszczynski.image.engine.model.enums.ImageOrientation;
import pl.info.rkluszczynski.image.engine.model.enums.ImageSizesRatio;

import java.awt.image.BufferedImage;

import static pl.info.rkluszczynski.image.engine.model.enums.ImageOrientation.*;
import static pl.info.rkluszczynski.image.engine.model.enums.ImageSizesRatio.RATIO_2_3;
import static pl.info.rkluszczynski.image.engine.model.enums.ImageSizesRatio.RATIO_3_4;

final
public class ImageInformationResolver {
    private static double ratioValueOf2x3 = 2. / 3.;
    private static double ratioValueOf3x4 = 3. / 4.;

    public static ImageSizesRatio detectClosestSizesRatio(BufferedImage image) {
        double smallerImageSize = (double) Math.min(image.getWidth(), image.getHeight());
        double biggerImageSize = (double) Math.max(image.getWidth(), image.getHeight());
        double sizesRatio = smallerImageSize / biggerImageSize;

        double diffWithRatio2x3 = Math.abs(ratioValueOf2x3 - sizesRatio);
        double diffWithRatio3x4 = Math.abs(ratioValueOf3x4 - sizesRatio);

        return (diffWithRatio2x3 < diffWithRatio3x4) ? RATIO_2_3 : RATIO_3_4;
    }

    public static ImageOrientation detectImageOrientation(BufferedImage image) {
        if (image.getWidth() == image.getHeight()) {
            return SQUARE;
        } else if (image.getWidth() < image.getHeight()) {
            return VERTICAL;
        } else {
            return HORIZONTAL;
        }
    }

    private ImageInformationResolver() {
    }
}
