package pl.info.rkluszczynski.image.engine.model.strategies;

import com.google.common.collect.Lists;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.info.rkluszczynski.image.core.compare.metric.CompareMetric;
import pl.info.rkluszczynski.image.core.compare.phash.ImagePHash03;
import pl.info.rkluszczynski.image.engine.ImageDiffer;
import pl.info.rkluszczynski.image.engine.model.ImageStatisticNames;
import pl.info.rkluszczynski.image.engine.tasks.PatternDetectorTask;
import pl.info.rkluszczynski.image.engine.tasks.input.DetectorTaskInput;
import pl.info.rkluszczynski.image.engine.utils.BufferedImageWrapper;
import pl.info.rkluszczynski.image.engine.utils.DrawHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static pl.info.rkluszczynski.image.engine.config.EngineConstants.*;

/**
 * Created by Rafal on 2014-05-27.
 */
public class BestLocalizedMatchStrategy implements PatternMatchStrategy {
    protected static final Logger logger = LoggerFactory.getLogger(BestLocalizedMatchStrategy.class);
    ImagePHash03 imagePHash = new ImagePHash03();
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
        CompareMetric metric = taskInput.getComparator().getMetric();

        int bestScoresAmount = Math.min(BEST_LOCALIZED_SCORES_STRATEGY_AMOUNT, results.size());
        String patternPHash = imagePHash.getHash(patternWrapper.getBufferedImage());
        for (int i = 0; i < bestScoresAmount; ++i) {
            MatchScore item = results.get(i);

            logger.info("Matching {}:", i);
            BufferedImage subImage = getMatchSubImage(resultImage,
                    item.getWidthPosition(), item.getHeightPosition(),
                    patternWrapper.getWidth(), patternWrapper.getHeight(), item.getScaleFactor());
            BufferedImage exactSubImage = Scalr.resize(subImage,
                    Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_EXACT, patternWrapper.getWidth(), patternWrapper.getHeight());
            saveImageMatch(exactSubImage, i);
            ImageDiffer.calculateDifferStatistics(patternWrapper.getBufferedImage(), exactSubImage);

//            String subImagePHash = determineSubImagePHash(resultImage,
//                    item.getWidthPosition(), item.getHeightPosition(),
//                    patternWrapper.getWidth(), patternWrapper.getHeight(), item.getScaleFactor());
//            int distance = HammingDistance.calculate(patternPHash, subImagePHash);
//            logger.info("Distance {} with pHashes {}, {} at position ({}, {})", distance,
//                    patternPHash, subImagePHash, item.getWidthPosition(), item.getHeightPosition());
//            if (distance > 23) {
//                continue;
//            }

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

    private void saveImageMatch(BufferedImage image, int suffixNum) {
        try {
            ImageIO.write(image, "PNG", new File(String.format("templateResult-%d.png", suffixNum)));
        } catch (IOException e) {
            logger.error("Problem during saving subImage!", e);
        }
    }

    private String determineSubImagePHash(BufferedImage image, int widthPosition, int heightPosition, int width, int height, double scaleFactor) {
        BufferedImage subImage = getMatchSubImage(image, widthPosition, heightPosition, width, height, scaleFactor);
        String hash = imagePHash.getHash(subImage);
        return hash;
    }

    private BufferedImage getMatchSubImage(BufferedImage image, int widthPosition, int heightPosition, int width, int height, double scaleFactor) {
        double invertedScaleFactor = 1. / scaleFactor;
        int scaledLeftPosition = (int) (invertedScaleFactor * widthPosition);
        int scaledTopPosition = (int) (invertedScaleFactor * heightPosition);
        int scaledWidth = (int) (invertedScaleFactor * width);
        int scaledHeight = (int) (invertedScaleFactor * height);

        return image.getSubimage(scaledLeftPosition, scaledTopPosition, scaledWidth, scaledHeight);
    }
}
