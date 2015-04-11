package pl.info.rkluszczynski.image.core.ciratefi.calculators;

import javaxt.io.Image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.IOException;
import java.util.Arrays;

public class GsQueryImage {
    private final BufferedImage image;
    private final Raster alphaRaster;

    public GsQueryImage(BufferedImage image) {
//        this.image = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
//        Graphics g = this.image.getGraphics();
//        g.drawImage(image, 0, 0, null);
//        g.dispose();
        this.image = image;
        alphaRaster = image.getAlphaRaster();
    }

    public GsQueryImage(Image image) {
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

    public GsQueryImage[] getImageRotations(int[] degrees) {
        GsQueryImage[] gsQueryImages = new GsQueryImage[degrees.length];
        for (int i = 0; i < degrees.length; ++i) {
            Image img = new Image(getBufferedImage());
            img.rotate(degrees[i]);

            gsQueryImages[i] = new GsQueryImage(img);
        }
        return gsQueryImages;
    }

    public static void main(String[] args) throws IOException {
        String imgPath = "image-core/src/test/resources/image-data/";

        javaxt.io.Image image = new Image(imgPath + "zubr-pattern1-poster.png");
        System.out.println(image.getWidth() + " x " + image.getHeight());
        System.out.println(Arrays.toString(image.getCorners()));
        image.rotate(30);
        System.out.println(image.getWidth() + " x " + image.getHeight());
        System.out.println(Arrays.toString(image.getCorners()));

//        image.saveAs(imgPath + "output.png");

        GsQueryImage gsQueryImage = new GsQueryImage(image);
        System.out.println(gsQueryImage.treatPixelAsAlpha(0, 0));
        System.out.println(gsQueryImage.getPixelColor(0, 0));

        int x = gsQueryImage.getWidth() / 2;
        int y = gsQueryImage.getHeight() / 2;
        System.out.println(gsQueryImage.treatPixelAsAlpha(x, y));
        System.out.println(gsQueryImage.getPixelColor(x, y));
        System.out.println(gsQueryImage.getValue(x, y));

        System.out.println("DONE");
    }
}
