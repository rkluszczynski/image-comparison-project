package pl.info.rkluszczynski.image.utils;

import java.awt.*;

/**
 * Created by Rafal on 2014-04-18.
 */
public class HSVColor {
    private double hue;
    private double saturation;
    private double value;

    public HSVColor(int color) {
        this(new Color(color));
    }

    public HSVColor(Color color) {
        calculateHSV(color);
    }

    public double getHue() {
        return hue;
    }

    public double getSaturation() {
        return saturation;
    }

    public double getValue() {
        return value;
    }

    private void calculateHSV(Color color) {
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();

        float[] hsbValues = new float[3];
        Color.RGBtoHSB(red, green, blue, hsbValues);

        hue = hsbValues[0];
        saturation = hsbValues[1];
        value = hsbValues[2];
    }
}
