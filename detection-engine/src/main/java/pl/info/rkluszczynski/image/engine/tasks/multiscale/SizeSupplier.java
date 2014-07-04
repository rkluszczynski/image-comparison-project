package pl.info.rkluszczynski.image.engine.tasks.multiscale;

import com.google.common.collect.Maps;
import pl.info.rkluszczynski.image.engine.config.EngineConstants;
import pl.info.rkluszczynski.image.engine.model.enums.ImageOrientation;
import pl.info.rkluszczynski.image.engine.model.enums.ImageSizesRatio;
import pl.info.rkluszczynski.image.engine.utils.ImageInformationResolver;

import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Map;

import static pl.info.rkluszczynski.image.engine.model.enums.ImageOrientation.HORIZONTAL;
import static pl.info.rkluszczynski.image.engine.model.enums.ImageSizesRatio.*;

final
public class SizeSupplier {
    private static final Map<ImageSizesRatio, Integer> suggestedSmallerSize;
    private static final Map<ImageSizesRatio, Integer> suggestedLargerSize;

    static {
        Map<ImageSizesRatio, Integer> suggestedSmallerSizeMap = Maps.newHashMap();
        suggestedSmallerSizeMap.put(RATIO_2_3, EngineConstants.RATIO_2_3_SMALLER_SIZE);
        suggestedSmallerSizeMap.put(RATIO_3_4, EngineConstants.RATIO_3_4_SMALLER_SIZE);
        suggestedSmallerSizeMap.put(RATIO_1_1, EngineConstants.RATIO_1_1_SMALLER_SIZE);

        suggestedSmallerSize = Collections.unmodifiableMap(suggestedSmallerSizeMap);

        Map<ImageSizesRatio, Integer> suggestedLargerSizeMap = Maps.newHashMap();
        suggestedLargerSizeMap.put(RATIO_2_3, EngineConstants.RATIO_2_3_LARGER_SIZE);
        suggestedLargerSizeMap.put(RATIO_3_4, EngineConstants.RATIO_3_4_LARGER_SIZE);
        suggestedLargerSizeMap.put(RATIO_1_1, EngineConstants.RATIO_1_1_LARGER_SIZE);

        suggestedLargerSize = Collections.unmodifiableMap(suggestedLargerSizeMap);
    }

    private SizeSupplier() {
    }

    public static int[] getSuggestedProcessingSizes(BufferedImage image) {
        ImageOrientation orientation = ImageInformationResolver.detectImageOrientation(image);
        ImageSizesRatio sizesRatio = ImageInformationResolver.detectClosestSizesRatio(image);
        if (orientation == HORIZONTAL) {
            return new int[]{suggestedLargerSize.get(sizesRatio), suggestedSmallerSize.get(sizesRatio)};
        } else {
            // Handles correctly vertical and square cases:
            return new int[]{suggestedSmallerSize.get(sizesRatio), suggestedLargerSize.get(sizesRatio)};
        }
    }
}
