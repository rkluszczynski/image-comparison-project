package pl.info.rkluszczynski.image.engine.model.strategies;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.info.rkluszczynski.image.engine.model.ImageStatisticNames;
import pl.info.rkluszczynski.image.engine.model.metrics.Metric;
import pl.info.rkluszczynski.image.engine.tasks.DetectorTaskInput;
import pl.info.rkluszczynski.image.engine.tasks.PatternDetectorTask;
import pl.info.rkluszczynski.image.engine.utils.BufferedImageWrapper;
import pl.info.rkluszczynski.image.engine.utils.DrawHelper;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * Created by Rafal on 2014-05-27.
 */
public class BestLocalizedMatchStrategy implements PatternMatchStrategy {
    protected static Logger logger = LoggerFactory.getLogger(BestLocalizedMatchStrategy.class);

    private static final int BEST_RESULTS_COUNT = 7;

    private double offset;
    private int bestResultsWidth;
    private int bestResultsHeight;
    private MatchScore[][] bestResultsTable;


    @Override
    public void initialize(DetectorTaskInput taskInput) {
        BufferedImageWrapper patternWrapper = taskInput.getPatternWrapper();
        BufferedImage resultImage = taskInput.getResultImage();

        offset = 0.5 * Math.min(patternWrapper.getWidth(), patternWrapper.getHeight());
        bestResultsWidth = (int) (resultImage.getWidth() / offset) + 1;
        bestResultsHeight = (int) (resultImage.getHeight() / offset) + 1;
        bestResultsTable = new MatchScore[bestResultsWidth][bestResultsHeight];
    }

    @Override
    public void putScore(MatchScore matchScore) {
        if (matchScore.getScore() < 0) {
            return;
        }
        double scaleFactor = matchScore.getScaleFactor();
        int tableWidth = (int) ((matchScore.getWidthPosition() / scaleFactor) / offset);
        int tableHeight = (int) ((matchScore.getHeightPosition() / scaleFactor) / offset);

        MatchScore item = bestResultsTable[tableWidth][tableHeight];
        if (item == null || matchScore.getScore() < item.getScore()) {
            bestResultsTable[tableWidth][tableHeight] = matchScore;
        }
    }

    @Override
    public void applyBestScores(PatternDetectorTask detectorTask, DetectorTaskInput taskInput) {
        List<MatchScore> results = Lists.newArrayList();
        for (int iw = 0; iw < bestResultsWidth; ++iw) {
            for (int ih = 0; ih < bestResultsHeight; ++ih) {
                MatchScore item = bestResultsTable[iw][ih];
                if (item != null) {
                    results.add(item);
                }
            }
        }
        Collections.sort(results);

        BufferedImageWrapper patternWrapper = taskInput.getPatternWrapper();
        BufferedImage resultImage = taskInput.getResultImage();
        Metric metric = taskInput.getMetric();

        for (int i = 0; i < BEST_RESULTS_COUNT; ++i) {
            MatchScore item = results.get(i);

            double matchDivisor = 256. * patternWrapper.countNonAlphaPixels();

            ImageStatisticNames statisticName = ImageStatisticNames.valueOf(String.format("METRIC_VALUE_%s", metric.getName()));
            detectorTask.saveStatisticData(statisticName, BigDecimal.valueOf(item.getScore() / matchDivisor));

            DrawHelper.drawRectangleOnImage(resultImage,
                    item.getWidthPosition(), item.getHeightPosition(),
                    patternWrapper.getWidth(), patternWrapper.getHeight(),
                    item.getScaleFactor(),
                    String.valueOf(i));
        }
    }
}
