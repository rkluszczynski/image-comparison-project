package pl.info.rkluszczynski.image.ciratefi;

import javaxt.io.Image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class ImageWrapper {
    private final BufferedImage image;
    private final Raster alphaRaster;

    public ImageWrapper(BufferedImage image) {
        this.image = image;
        alphaRaster = image.getAlphaRaster();
    }

    private ImageWrapper(Image image) {
        this.image = image.getBufferedImage();
        alphaRaster = this.image.getAlphaRaster();
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }

    public int getValue(int iw, int ih) {
        return (image.getRGB(iw, ih) & 0x00FF00) >> 8;
    }

    public Color getColor(int iw, int ih) {
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

    public BufferedImage getBufferedImage() {
        return image;
    }

    public ImageWrapper[] getRotations(double[] degrees) {
        ImageWrapper[] gsQueryImages = new ImageWrapper[degrees.length];
        for (int i = 0; i < degrees.length; ++i) {
            Image img = new Image(getBufferedImage());
            img.rotate(degrees[i]);

            gsQueryImages[i] = new ImageWrapper(img);
        }
        return gsQueryImages;
    }
}
