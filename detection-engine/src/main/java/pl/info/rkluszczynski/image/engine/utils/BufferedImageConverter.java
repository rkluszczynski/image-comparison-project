package pl.info.rkluszczynski.image.engine.utils;

import java.awt.*;
import java.awt.image.BufferedImage;

final
public class BufferedImageConverter {

    private BufferedImageConverter() {
    }

    public static Color[][] convertBufferedImageToColorArray(BufferedImage bufferedImage) {
        int imageWidth = bufferedImage.getWidth();
        int imageHeight = bufferedImage.getHeight();

        Color[][] imageArray = new Color[imageWidth][imageHeight];
        for (int iw = 0; iw < imageWidth; ++iw) {
            for (int ih = 0; ih < imageHeight; ++ih) {
                imageArray[iw][ih] = new Color(bufferedImage.getRGB(iw, ih));
            }
        }
        return imageArray;
    }

    /*
        Sets null in colorArray where Alpha channel is more then set threshold.
     */
    public static Color[][] convertBufferedImageToColorArray(BufferedImageWrapper imageWrapper) {
        int imageWidth = imageWrapper.getWidth();
        int imageHeight = imageWrapper.getHeight();

        Color[][] imageArray = new Color[imageWidth][imageHeight];
        for (int iw = 0; iw < imageWidth; ++iw) {
            for (int ih = 0; ih < imageHeight; ++ih) {
                imageArray[iw][ih] = imageWrapper.treatPixelAsAlpha(iw, ih) ? null
                        : imageWrapper.getPixelColor(iw, ih);
            }
        }
        return imageArray;
    }
}
