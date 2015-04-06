package pl.info.rkluszczynski.image.core.ciratefi.calculators;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class GsImageWrapper {
    private final BufferedImage image;
    private final Raster alphaRaster;

    public GsImageWrapper(BufferedImage image) {
        this.image = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics g = this.image.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();

        alphaRaster = this.image.getAlphaRaster();
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }

    public int getValue(int iw, int ih) {
        return image.getRGB(iw, ih) & 0xFF;
    }

    public Color getPixelColor(int iw, int ih) {
        return new Color(image.getRGB(iw, ih));
    }

    public boolean treatPixelAsAlpha(int iw, int ih) {
        if (alphaRaster == null) {
            // there is no Alpha channel:
            return false;
        }
        int[] sample = alphaRaster.getPixel(iw, ih, (int[]) null);
        if (sample == null || sample.length == 0 || sample[0] > 0x04) {
            // alpha value is gt TRANSPARENT_THRESHOLD_ALPHA_VALUE (pixel should not be transparent):
            return false;
        }
        // pixel is transparent:
        return true;
    }

    public int countNonAlphaPixels() {
        int nonAlphaPixels = 0;
        for (int iw = 0; iw < image.getWidth(); ++iw) {
            for (int ih = 0; ih < image.getHeight(); ++ih) {
                if (!treatPixelAsAlpha(iw, ih)) {
                    ++nonAlphaPixels;
                }
            }
        }
        return nonAlphaPixels;
    }

    public BufferedImage getBufferedImage() {
        return image;
    }
}
