package pl.info.rkluszczynski.image.engine.model.metrics;

import pl.info.rkluszczynski.image.core.compare.metric.CompareMetric;
import pl.info.rkluszczynski.image.engine.config.EngineConstants;

import java.awt.*;

public class RMSEColorMetric implements CompareMetric {
    private double metricValue;
    private double pixelsNumber;

    @Override
    public void resetValue() {
        metricValue = 0.;
        pixelsNumber = 0.;
    }

    @Override
    public double calculateValue() {
        return Math.sqrt(metricValue / (3. * pixelsNumber)) / maxValue();
    }

    @Override
    public double maxValue() {
        return EngineConstants.MAX_PIXEL_VALUE;
    }

    @Override
    public void addPixelsDifference(Color inputPixel, Color templatePixel) {
        metricValue += ((inputPixel.getRed() - templatePixel.getRed()) * (inputPixel.getRed() - templatePixel.getRed()));
        metricValue += ((inputPixel.getGreen() - templatePixel.getGreen()) * (inputPixel.getGreen() - templatePixel.getGreen()));
        metricValue += ((inputPixel.getBlue() - templatePixel.getBlue()) * (inputPixel.getBlue() - templatePixel.getBlue()));
        ++pixelsNumber;
    }

    @Override
    public String getName() {
        return "RMSE";
    }
}
