package pl.info.rkluszczynski.image.engine.model.strategies;

import com.google.common.collect.Lists;
import pl.info.rkluszczynski.image.engine.model.ImageStatisticNames;
import pl.info.rkluszczynski.image.engine.model.metrics.Metric;
import pl.info.rkluszczynski.image.engine.tasks.PatternDetectorTask;
import pl.info.rkluszczynski.image.engine.tasks.input.DetectorTaskInput;
import pl.info.rkluszczynski.image.engine.utils.BufferedImageWrapper;
import pl.info.rkluszczynski.image.engine.utils.DrawHelper;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static pl.info.rkluszczynski.image.engine.config.EngineConstants.*;

/**
 * Created by Rafal on 2014-05-27.
 */
public class BestLocalizedMatchStrategy implements PatternMatchStrategy {

    private double offset;
    private int bestResultsWidth;
    private int bestResultsHeight;
    private MatchScore[][] bestResultsTable;

    @Override
    public void initialize(DetectorTaskInput taskInput) {
        BufferedImageWrapper patternWrapper = taskInput.getPatternWrapper();
        BufferedImage resultImage = taskInput.getResultImage();

        offset = BEST_LOCALIZED_SCORES_OFFSET > 0 ? BEST_LOCALIZED_SCORES_OFFSET
                : BEST_LOCALIZED_SCORES_MIN_SIZE_RATIO * Math.min(patternWrapper.getWidth(), patternWrapper.getHeight());
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
        Metric metric = taskInput.getComparator().getMetric();

        for (int i = 0; i < BEST_LOCALIZED_SCORES_STRATEGY_AMOUNT; ++i) {
            MatchScore item = results.get(i);

            double matchDivisor = MAX_PIXEL_VALUE * patternWrapper.countNonAlphaPixels();

            ImageStatisticNames statisticName = ImageStatisticNames.valueOf(String.format("METRIC_VALUE_%s",
                    metric == null ? "SUM" : metric.getName()));
            detectorTask.saveStatisticData(statisticName, BigDecimal.valueOf(item.getScore() / matchDivisor));

            DrawHelper.drawRectangleOnImage(resultImage,
                    item.getWidthPosition(), item.getHeightPosition(),
                    patternWrapper.getWidth(), patternWrapper.getHeight(),
                    item.getScaleFactor(),
                    String.valueOf(i));
        }
    }
}
