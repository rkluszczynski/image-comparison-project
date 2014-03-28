package pl.info.rkluszczynski.image.engine.utils;

import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;

@Component
@Qualifier("imageSizeScaleProcessor")
public class ImageSizeScaleProcessor {

    public BufferedImage getImageScaledToWidthOnlyIfLarger(BufferedImage image, int targetWidth) {
        double originalWidth = image.getWidth();
        if (originalWidth > targetWidth) {
            double originalHeight = image.getHeight();
            int targetHeight = (int) Math.round(originalHeight * targetWidth / originalWidth);
            return Scalr.resize(image, Scalr.Method.QUALITY, targetWidth, targetHeight);
        }
        return image;
    }

    public static BufferedImage getScaledImage(BufferedImage image, int targetWidth, int targetHeight) {
        return Scalr.resize(image, Scalr.Method.QUALITY, targetWidth, targetHeight);
    }
}
