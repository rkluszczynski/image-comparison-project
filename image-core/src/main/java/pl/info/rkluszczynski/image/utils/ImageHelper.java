package pl.info.rkluszczynski.image.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

/**
 * Helper class for BufferedImage management.
 *
 * @author Rafal
 */
final
public class ImageHelper {

    public static BufferedImage deepCopy(BufferedImage bufferedImage) {
        ColorModel colorModel = bufferedImage.getColorModel();
        boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
        WritableRaster raster = bufferedImage.copyData(null);
        return new BufferedImage(colorModel, raster, isAlphaPremultiplied, null);
    }

    public static BufferedImage scaleImagePixelsValue(BufferedImage bufferedImage, double scaleRatio) {
        BufferedImage image = deepCopy(bufferedImage);
        for (int iw = 0; iw < image.getWidth(); ++iw) {
            for (int ih = 0; ih < image.getHeight(); ++ih) {
                Color imagePixelColor = new Color(bufferedImage.getRGB(iw, ih));

                Color newPixelColor = new Color(
                        (int) Math.min(Math.max(scaleRatio * imagePixelColor.getRed(), 0.), 255.),
                        (int) Math.min(Math.max(scaleRatio * imagePixelColor.getGreen(), 0.), 255.),
                        (int) Math.min(Math.max(scaleRatio * imagePixelColor.getBlue(), 0.), 255.),
                        imagePixelColor.getAlpha()
                );
                image.setRGB(iw, ih, newPixelColor.getRGB());
            }
        }
        return image;
    }

    private ImageHelper() {
    }
}
