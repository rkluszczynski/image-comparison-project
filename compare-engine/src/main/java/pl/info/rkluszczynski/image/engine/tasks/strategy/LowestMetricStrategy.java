package pl.info.rkluszczynski.image.engine.tasks.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.info.rkluszczynski.image.engine.model.ImageStatisticNames;
import pl.info.rkluszczynski.image.engine.tasks.AbstractTask;
import pl.info.rkluszczynski.image.engine.tasks.metrics.Metric;
import pl.info.rkluszczynski.image.engine.utils.BufferedImageWrapper;
import pl.info.rkluszczynski.image.engine.utils.DrawHelper;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;

/**
 * Created by Rafal on 2014-05-27.
 */
public class LowestMetricStrategy implements BestMatchStrategy {
    protected static Logger logger = LoggerFactory.getLogger(LowestMetricStrategy.class);

    private final BufferedImage resultImage;
    private final BufferedImageWrapper templateImageWrapper;

    double bestResult = Double.MAX_VALUE;
    int bestLeftPosition = -1;
    int bestTopPosition = -1;
    double bestScaleFactor = -1;

    public LowestMetricStrategy(BufferedImage resultImage, BufferedImageWrapper templateImageWrapper) {
        this.resultImage = resultImage;
        this.templateImageWrapper = templateImageWrapper;
    }

    @Override
    public void put(double result, int iw, int ih, double scaleFactor) {
        if (result < bestResult) {
            bestResult = result;
            bestLeftPosition = iw;
            bestTopPosition = ih;
            bestScaleFactor = scaleFactor;
        }
    }

    @Override
    public void applyBestResults(AbstractTask processingTask, Metric metric) {
        if (bestResult < Double.MAX_VALUE) {
            logger.info("Best result at level: " + bestResult + " at (" + bestLeftPosition + ", " + bestTopPosition + ")");

            double matchDivisor = 256. * templateImageWrapper.countNonAlphaPixels();

            ImageStatisticNames statisticName = ImageStatisticNames.valueOf(String.format("METRIC_VALUE_%s", metric.getName()));
            processingTask.saveStatisticData(statisticName, BigDecimal.valueOf(bestResult / matchDivisor));

            DrawHelper.drawRectangleOnImage(resultImage, bestLeftPosition, bestTopPosition, templateImageWrapper.getWidth(), templateImageWrapper.getHeight(), bestScaleFactor);

            bestResult = Double.MAX_VALUE;
            bestLeftPosition = -1;
            bestTopPosition = -1;
            bestScaleFactor = -1;
        }
    }
}
