package pl.info.rkluszczynski.image.core;


import com.google.common.collect.Lists;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Image histogram equalization
 * <p>
 * Author: Bostjan Cigan (http://zerocool.is-a-geek.net)
 */
final
public class HistogramEQ {
    private static BufferedImage original, equalized;

    public static void main(String[] args) throws IOException {
        File original_f = new File(args[0] + ".png");
        String output_f = args[1];
        original = ImageIO.read(original_f);
        equalized = histogramEqualization(original, true);
        writeImage(output_f);
    }

    private static void writeImage(String output) throws IOException {
        File file = new File(output + ".png");
        ImageIO.write(equalized, "png", file);
    }

    public static BufferedImage histogramEqualization(BufferedImage original, boolean color) {
        int red;
        int green;
        int blue;
        int alpha;
        int gray;
        int newPixel = 0;

        // Get the Lookup table for histogram equalization
        List<double[]> histLUT = histogramEqualizationLUT(original);

        BufferedImage histogramEQ = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

        for (int i = 0; i < original.getWidth(); i++) {
            for (int j = 0; j < original.getHeight(); j++) {
                // Get pixels by R, G, B
                alpha = new Color(original.getRGB(i, j)).getAlpha();
                red = new Color(original.getRGB(i, j)).getRed();
                green = new Color(original.getRGB(i, j)).getGreen();
                blue = new Color(original.getRGB(i, j)).getBlue();

                if (color) {
                    // Set new pixel values using the histogram lookup table
                    red = (int) histLUT.get(0)[red];
                    green = (int) histLUT.get(1)[green];
                    blue = (int) histLUT.get(2)[blue];

                    // Return back to original format
                    newPixel = colorToRGB(alpha, red, green, blue);
                } else {
                    gray = (red + green + blue) / 3;
                    gray = (int) histLUT.get(3)[gray];

                    newPixel = colorToRGB(alpha, gray, gray, gray);
                }
                // Write pixels into image
                histogramEQ.setRGB(i, j, newPixel);
            }
        }
        return histogramEQ;
    }

    // Get the histogram equalization lookup table for separate R, G, B channels
    private static List<double[]> histogramEqualizationLUT(BufferedImage input) {
        // Get an image histogram - calculated values by R, G, B channels
        List<double[]> imageHist = imageHistogram(input);
        // Create the lookup table
        List<double[]> imageLUT = Lists.newArrayList();

        // Fill the lookup table
        double[] rhistogram = new double[256];
        double[] ghistogram = new double[256];
        double[] bhistogram = new double[256];
        double[] ahistogram = new double[256];

        for (int i = 0; i < rhistogram.length; i++) rhistogram[i] = 0;
        for (int i = 0; i < ghistogram.length; i++) ghistogram[i] = 0;
        for (int i = 0; i < bhistogram.length; i++) bhistogram[i] = 0;
        for (int i = 0; i < ahistogram.length; i++) ahistogram[i] = 0;

        double sumr = 0;
        double sumg = 0;
        double sumb = 0;
        double suma = 0;

        // Calculate the scale factor
        float scale_factor = (float) (255.0 / (input.getWidth() * input.getHeight()));

        for (int i = 0; i < rhistogram.length; i++) {
            sumr += imageHist.get(0)[i];
            int valr = (int) (sumr * scale_factor);
            if (valr > 255) {
                rhistogram[i] = 255;
            } else rhistogram[i] = valr;

            sumg += imageHist.get(1)[i];
            int valg = (int) (sumg * scale_factor);
            if (valg > 255) {
                ghistogram[i] = 255;
            } else ghistogram[i] = valg;

            sumb += imageHist.get(2)[i];
            int valb = (int) (sumb * scale_factor);
            if (valb > 255) {
                bhistogram[i] = 255;
            } else bhistogram[i] = valb;

            suma += imageHist.get(3)[i];
            int vala = (int) (suma * scale_factor);
            if (vala > 255) {
                ahistogram[i] = 255;
            } else ahistogram[i] = vala;
        }
        imageLUT.add(rhistogram);
        imageLUT.add(ghistogram);
        imageLUT.add(bhistogram);
        imageLUT.add(ahistogram);
        return imageLUT;
    }

    // Return an ArrayList containing histogram values for separate R, G, B channels
    public static List<double[]> imageHistogram(BufferedImage input) {
        double[] rhistogram = new double[256];
        double[] ghistogram = new double[256];
        double[] bhistogram = new double[256];
        double[] ahistogram = new double[256];

        for (int i = 0; i < rhistogram.length; i++) rhistogram[i] = 0.;
        for (int i = 0; i < ghistogram.length; i++) ghistogram[i] = 0.;
        for (int i = 0; i < bhistogram.length; i++) bhistogram[i] = 0.;
        for (int i = 0; i < ahistogram.length; i++) ahistogram[i] = 0.;

        for (int i = 0; i < input.getWidth(); i++) {
            for (int j = 0; j < input.getHeight(); j++) {
                int red = new Color(input.getRGB(i, j)).getRed();
                int green = new Color(input.getRGB(i, j)).getGreen();
                int blue = new Color(input.getRGB(i, j)).getBlue();

                int gray = (red + green + blue) / 3;

                // Increase the values of colors
                rhistogram[red]++;
                ghistogram[green]++;
                bhistogram[blue]++;
                ahistogram[gray]++;
            }
        }
        List<double[]> hist = Lists.newArrayList();
        hist.add(rhistogram);
        hist.add(ghistogram);
        hist.add(bhistogram);
        hist.add(ahistogram);
        return hist;
    }

    // Convert R, G, B, Alpha to standard 8 bit
    private static int colorToRGB(int alpha, int red, int green, int blue) {
        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red;
        newPixel = newPixel << 8;
        newPixel += green;
        newPixel = newPixel << 8;
        newPixel += blue;
        return newPixel;
    }
}
