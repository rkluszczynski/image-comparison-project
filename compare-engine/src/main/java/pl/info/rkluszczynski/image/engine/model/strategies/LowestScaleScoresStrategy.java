package pl.info.rkluszczynski.image.engine.model.strategies;

import com.google.common.collect.Maps;
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
import java.util.Map;

/**
 * Created by Rafal on 2014-05-27.
 */
public class LowestScaleScoresStrategy implements PatternMatchStrategy {
    private static final Logger logger = LoggerFactory.getLogger(LowestScaleScoresStrategy.class);

    private final Map<Double, MatchScore> scaleResults = Maps.newHashMap();


    @Override
    public void initialize(DetectorTaskInput taskInput) {
    }

    @Override
    public void putScore(MatchScore matchScore) {
        double scaleFactor = matchScore.getScaleFactor();
        MatchScore bestMatch = scaleResults.get(scaleFactor);
        if (bestMatch == null || matchScore.getScore() < bestMatch.getScore()) {
            scaleResults.put(scaleFactor, matchScore);
        }
    }

    @Override
    public void applyBestScores(PatternDetectorTask detectorTask, DetectorTaskInput taskInput) {
        BufferedImageWrapper patternWrapper = taskInput.getPatternWrapper();
        BufferedImage resultImage = taskInput.getResultImage();

        for (MatchScore bestScaleMatch : scaleResults.values()) {
            logger.info("Best result {} at ({}, {}) with scale {}",
                    bestScaleMatch.getScore(),
                    bestScaleMatch.getWidthPosition(),
                    bestScaleMatch.getHeightPosition(),
                    bestScaleMatch.getScaleFactor());

            double matchDivisor = 256. * patternWrapper.countNonAlphaPixels();
            Metric metric = taskInput.getMetric();

            ImageStatisticNames statisticName = ImageStatisticNames.valueOf(String.format("METRIC_VALUE_%s", metric.getName()));
            detectorTask.saveStatisticData(statisticName, BigDecimal.valueOf(bestScaleMatch.getScore() / matchDivisor));

            DrawHelper.drawRectangleOnImage(resultImage,
                    bestScaleMatch.getWidthPosition(), bestScaleMatch.getHeightPosition(),
                    patternWrapper.getWidth(), patternWrapper.getHeight(),
                    bestScaleMatch.getScaleFactor());
        }
    }
}
