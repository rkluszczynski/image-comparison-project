package pl.info.rkluszczynski.image.engine.model.metrics;

import java.awt.*;

public class NRMSEColorMetric extends RMSEColorMetric {
    private double maxValue;
    private double minValue;

    @Override
    public void resetValue() {
        super.resetValue();
        maxValue = Double.MIN_VALUE;
        minValue = Double.MAX_VALUE;
    }

    @Override
    public double calculateValue() {
        return (super.calculateValue() * super.maxValue()) / (maxValue - minValue);
    }

    @Override
    public double maxValue() {
        return 1.;
    }

    @Override
    public void addPixelsDifference(Color inputPixel, Color templatePixel) {
        super.addPixelsDifference(inputPixel, templatePixel);

        maxValue = Math.max(maxValue, inputPixel.getRed());
        maxValue = Math.max(maxValue, templatePixel.getRed());
        maxValue = Math.max(maxValue, inputPixel.getGreen());
        maxValue = Math.max(maxValue, templatePixel.getGreen());
        maxValue = Math.max(maxValue, inputPixel.getBlue());
        maxValue = Math.max(maxValue, templatePixel.getBlue());

        minValue = Math.min(minValue, inputPixel.getRed());
        minValue = Math.min(minValue, templatePixel.getRed());
        minValue = Math.min(minValue, inputPixel.getGreen());
        minValue = Math.min(minValue, templatePixel.getGreen());
        minValue = Math.min(minValue, inputPixel.getBlue());
        minValue = Math.min(minValue, templatePixel.getBlue());
    }

    @Override
    public String getName() {
        return "NRMSE";
    }
}
