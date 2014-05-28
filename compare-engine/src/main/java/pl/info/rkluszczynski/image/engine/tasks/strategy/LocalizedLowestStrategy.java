package pl.info.rkluszczynski.image.engine.tasks.strategy;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.info.rkluszczynski.image.engine.model.ImageStatisticNames;
import pl.info.rkluszczynski.image.engine.tasks.AbstractTask;
import pl.info.rkluszczynski.image.engine.tasks.metrics.Metric;
import pl.info.rkluszczynski.image.engine.utils.BufferedImageWrapper;
import pl.info.rkluszczynski.image.engine.utils.DrawHelper;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * Created by Rafal on 2014-05-27.
 */
public class LocalizedLowestStrategy implements BestMatchStrategy {
    protected static Logger logger = LoggerFactory.getLogger(LocalizedLowestStrategy.class);

    public static int BEST_RESULTS_COUNT = 5;

    private final BufferedImage inputImage;
    private final BufferedImageWrapper templateImageWrapper;

    private final double offset;
    private final int bestResultsWidth;
    private final int bestResultsHeight;
    private final MatchItem[][] bestResultsTable;

    public LocalizedLowestStrategy(BufferedImage resultImage, BufferedImageWrapper templateImageWrapper) {
        this.inputImage = resultImage;
        this.templateImageWrapper = templateImageWrapper;
        this.offset = 0.5 * Math.min(templateImageWrapper.getWidth(), templateImageWrapper.getHeight());
        this.bestResultsWidth = (int) (inputImage.getWidth() / offset) + 1;
        this.bestResultsHeight = (int) (inputImage.getHeight() / offset) + 1;
        this.bestResultsTable = new MatchItem[bestResultsWidth][bestResultsHeight];
    }

    @Override
    public void put(double result, int iw, int ih, double scaleFactor) {
        int tableWidth = (int) (iw / offset);
        int tableHeight = (int) (ih / offset);

        MatchItem item = bestResultsTable[tableWidth][tableHeight];
        if (item == null || result < item.getResult()) {
            bestResultsTable[tableWidth][tableHeight] =
                    new MatchItem(result, iw, ih, scaleFactor);
        }
    }

    @Override
    public void applyBestResults(AbstractTask processingTask, Metric metric) {
        List<MatchItem> results = Lists.newArrayList();
        for (int iw = 0; iw < bestResultsWidth; ++iw) {
            for (int ih = 0; ih < bestResultsHeight; ++ih) {
                MatchItem item = bestResultsTable[iw][ih];
                results.add(item);
            }
        }
        Collections.sort(results);

        for (int i = 0; i < BEST_RESULTS_COUNT; ++i) {
            MatchItem item = results.get(i);

            double matchDivisor = 256. * templateImageWrapper.countNonAlphaPixels();

            ImageStatisticNames statisticName = ImageStatisticNames.valueOf(String.format("METRIC_VALUE_%s", metric.getName()));
            processingTask.saveStatisticData(statisticName, BigDecimal.valueOf(item.getResult() / matchDivisor));

            DrawHelper.drawRectangleOnImage(inputImage,
                    item.getLeftPosition(),
                    item.getTopPosition(),
                    templateImageWrapper.getWidth(),
                    templateImageWrapper.getHeight(),
                    item.getScaleFactor());
        }
    }
}
