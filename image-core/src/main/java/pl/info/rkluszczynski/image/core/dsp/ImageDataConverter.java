package pl.info.rkluszczynski.image.core.dsp;

import java.awt.*;
import java.awt.image.BufferedImage;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

final
public class ImageDataConverter {

    private ImageDataConverter() {
    }

    public static int[][][] bufferedImageToIntArray(BufferedImage bufferedImage) {
        int imgWidth = bufferedImage.getWidth();
        int imgHeight = bufferedImage.getHeight();
        int[][][] imageIntArray = new int[imgHeight][imgWidth][4];

        for (int iw = 0; iw < imgWidth; ++iw) {
            for (int ih = 0; ih < imgHeight; ++ih) {
                Color pixel = new Color(bufferedImage.getRGB(iw, ih));

                imageIntArray[ih][iw][1] = pixel.getRed();
                imageIntArray[ih][iw][2] = pixel.getGreen();
                imageIntArray[ih][iw][3] = pixel.getBlue();
                imageIntArray[ih][iw][0] = pixel.getAlpha();
            }
        }
        return imageIntArray;
    }

    public static BufferedImage intArrayToBufferedImage(int[][][] imageIntArray) {
        int imgWidth = imageIntArray.length;
        int imgHeight = imageIntArray[0].length;
        BufferedImage bufferedImage = new BufferedImage(imgWidth, imgHeight, TYPE_INT_ARGB);

        for (int iw = 0; iw < imgWidth; ++iw) {
            for (int ih = 0; ih < imgHeight; ++ih) {
                Color pixel = new Color(
                        imageIntArray[ih][iw][1],
                        imageIntArray[ih][iw][2],
                        imageIntArray[ih][iw][3],
                        imageIntArray[ih][iw][0]
                );
                bufferedImage.setRGB(iw, ih, pixel.getRGB());
            }
        }
        return bufferedImage;
    }
}
