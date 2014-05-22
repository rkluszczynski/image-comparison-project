package pl.info.rkluszczynski.image.engine.tasks.pyramid;

import com.google.common.collect.Maps;
import pl.info.rkluszczynski.image.engine.model.enums.ImageOrientation;
import pl.info.rkluszczynski.image.engine.model.enums.ImageSizesRatio;
import pl.info.rkluszczynski.image.engine.utils.ImageInformationResolver;

import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Map;

import static pl.info.rkluszczynski.image.engine.model.enums.ImageOrientation.HORIZONTAL;
import static pl.info.rkluszczynski.image.engine.model.enums.ImageSizesRatio.RATIO_2_3;
import static pl.info.rkluszczynski.image.engine.model.enums.ImageSizesRatio.RATIO_3_4;

final
public class SizeSupplier {
    private static final Map<ImageSizesRatio, Integer> suggestedSmallerSize;
    private static final Map<ImageSizesRatio, Integer> suggestedLargerSize;

    static {
        Map<ImageSizesRatio, Integer> suggestedSmallerSizeMap = Maps.newHashMap();
        suggestedSmallerSizeMap.put(RATIO_2_3, 268);
        suggestedSmallerSizeMap.put(RATIO_3_4, 300);

        suggestedSmallerSize = Collections.unmodifiableMap(suggestedSmallerSizeMap);

        Map<ImageSizesRatio, Integer> suggestedLargerSizeMap = Maps.newHashMap();
        suggestedLargerSizeMap.put(RATIO_2_3, 402);
        suggestedLargerSizeMap.put(RATIO_3_4, 400);

        suggestedLargerSize = Collections.unmodifiableMap(suggestedSmallerSizeMap);
    }

    public static int getSuggestedProcessingWidth(BufferedImage image) {
        ImageOrientation orientation = ImageInformationResolver.detectImageOrientation(image);
        ImageSizesRatio sizesRatio = ImageInformationResolver.detectClosestSizesRatio(image);
        return (orientation == HORIZONTAL) ? suggestedLargerSize.get(sizesRatio)
                : suggestedSmallerSize.get(sizesRatio);
    }

    public static int getSuggestedProcessingHeight(BufferedImage image) {
        ImageOrientation orientation = ImageInformationResolver.detectImageOrientation(image);
        ImageSizesRatio sizesRatio = ImageInformationResolver.detectClosestSizesRatio(image);
        return (orientation == HORIZONTAL) ? suggestedSmallerSize.get(sizesRatio)
                : suggestedLargerSize.get(sizesRatio);
    }

    private SizeSupplier() {
    }
}
