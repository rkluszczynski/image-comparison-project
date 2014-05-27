package pl.info.rkluszczynski.image.engine.tasks.strategy;

import pl.info.rkluszczynski.image.engine.tasks.AbstractTask;
import pl.info.rkluszczynski.image.engine.tasks.metrics.Metric;
import pl.info.rkluszczynski.image.engine.utils.BufferedImageWrapper;

import java.awt.image.BufferedImage;

/**
 * Created by Rafal on 2014-05-27.
 */
public interface BestMatchStrategy {
    public void put(double result, int iw, int ih, double scaleFactor);

    void applyBestResults(BufferedImage resultImage, BufferedImageWrapper templateImageWrapper, AbstractTask processingTask, Metric metric);
}
