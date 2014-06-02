package pl.info.rkluszczynski.image.engine.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

import static pl.info.rkluszczynski.image.engine.config.EngineConstants.TRANSPARENT_THRESHOLD_ALPHA_VALUE;

public class BufferedImageWrapper {
    private final BufferedImage image;
    private final Raster alphaRaster;

    public BufferedImageWrapper(BufferedImage image) {
        this.image = image;
        alphaRaster = image.getAlphaRaster();
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
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
        if (sample == null || sample.length == 0 || sample[0] > TRANSPARENT_THRESHOLD_ALPHA_VALUE) {
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
}
