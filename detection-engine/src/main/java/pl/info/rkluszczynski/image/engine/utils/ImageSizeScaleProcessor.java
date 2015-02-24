package pl.info.rkluszczynski.image.engine.utils;

import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;

@Component
@Qualifier("imageSizeScaleProcessor")
public class ImageSizeScaleProcessor {

    public static BufferedImage getExactScaledImage(BufferedImage image, int targetWidth, int targetHeight) {
        return Scalr.resize(image, Scalr.Method.QUALITY, Scalr.Mode.FIT_EXACT, targetWidth, targetHeight);
    }

    public BufferedImage getImageScaledToWidthOnlyIfLarger(BufferedImage image, int targetWidth) {
        if (image.getWidth() > targetWidth) {
            return Scalr.resize(image, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH, targetWidth);
        }
        return image;
    }
}
