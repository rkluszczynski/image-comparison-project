package pl.info.rkluszczynski.image.engine.model.metrics;

import pl.info.rkluszczynski.image.core.compare.metric.CompareMetric;

import java.awt.*;

import static pl.info.rkluszczynski.image.engine.config.EngineConstants.MAX_PIXEL_VALUE;

public class ExpColorMetric implements CompareMetric {
    private static final double EXP_ARG_MAX_VALUE = 16.;
    private static final double EXP_ARG_MAX_NORMALIZER = Math.exp(EXP_ARG_MAX_VALUE);

    private double metricValue;
    private double pixelsNumber;

    @Override
    public void resetValue() {
        metricValue = 0.;
        pixelsNumber = 0.;
    }

    @Override
    public double calculateValue() {
        return metricValue / (EXP_ARG_MAX_NORMALIZER * pixelsNumber);
    }

    @Override
    public double maxValue() {
        return 1.;
    }

    @Override
    public void addPixelsDifference(Color inputPixel, Color templatePixel) {
        double expArg = inputPixel.getRed() - templatePixel.getRed();
        expArg += (inputPixel.getGreen() - templatePixel.getGreen());
        expArg += (inputPixel.getBlue() - templatePixel.getBlue());
        expArg /= (3. * MAX_PIXEL_VALUE);

        metricValue += Math.exp(EXP_ARG_MAX_VALUE * expArg);
        ++pixelsNumber;
    }

    @Override
    public String getName() {
        return "Exp";
    }
}
