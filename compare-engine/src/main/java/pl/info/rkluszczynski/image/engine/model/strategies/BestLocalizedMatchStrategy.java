package pl.info.rkluszczynski.image.engine.model.strategies;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.info.rkluszczynski.image.core.compare.metric.CompareMetric;
import pl.info.rkluszczynski.image.engine.model.ImageStatisticNames;
import pl.info.rkluszczynski.image.engine.model.validators.ColorMeansValidator;
import pl.info.rkluszczynski.image.engine.model.validators.MatchValidator;
import pl.info.rkluszczynski.image.engine.model.validators.RMSEValidator;
import pl.info.rkluszczynski.image.engine.model.validators.ValidationDecision;
import pl.info.rkluszczynski.image.engine.tasks.PatternDetectorTask;
import pl.info.rkluszczynski.image.engine.tasks.input.DetectorTaskInput;
import pl.info.rkluszczynski.image.engine.tasks.multiscale.QueryImageWrapper;
import pl.info.rkluszczynski.image.engine.utils.BufferedImageConverter;
import pl.info.rkluszczynski.image.engine.utils.BufferedImageWrapper;
import pl.info.rkluszczynski.image.engine.utils.DrawHelper;

import javax.imageio.ImageIO;
import java.awt.*;
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

    private static final int maxNumberOfPresentedValidResults = 5;
    private static final int maxNumberOfPresentedPossibleResults = 3;

    private double offset;
    private int bestResultsWidth;
    private int bestResultsHeight;
    private MatchScore[][] bestResultsTable;

    private MatchValidator[] matchValidators = {
            new RMSEValidator(),
            new ColorMeansValidator()//, new ColorStddevsValidator()
    };

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
//        saveBestMatchImages(results, bestScoresAmount, taskInput);


        List<MatchScore> validResults = Lists.newArrayList();
        List<MatchScore> possibleResults = Lists.newArrayList();
        for (int i = 0; i < bestScoresAmount; ++i) {
            MatchScore score = results.get(i);
            logger.info("Validating matching {}", i);

            ValidationDecision validationDecision = isPatternMatchValid(score, taskInput);
            switch (validationDecision.getMatchDecision()) {
                case VALID_MATCH:
                    score.setDescription(String.valueOf(i));
                    validResults.add(score);
                    break;
                case PROBABLY_MATCH:
                    MatchScore newMatchScore = new MatchScore(
                            validationDecision.getScoreValue(),
                            score.getWidthPosition(), score.getHeightPosition(), score.getScaleFactor()
                    );
                    newMatchScore.setDescription(String.format("[%d]", i));
                    possibleResults.add(newMatchScore);
                    break;
                default:
                    logger.info("Rejected score number {} with value {}", i, validationDecision.getScoreValue());
            }
        }

        if (!validResults.isEmpty()) {
            Collections.sort(validResults);
            int validMatchAmount = Math.min(validResults.size(), maxNumberOfPresentedValidResults);

            for (int i = 0; i < validMatchAmount; ++i) {
                MatchScore score = validResults.get(i);

                ImageStatisticNames statisticName =
                        ImageStatisticNames.valueOf(String.format("METRIC_VALUE_%s",
                                metric == null ? "SUM" : metric.getName()));

                detectorTask.saveStatisticData(statisticName, BigDecimal.valueOf(score.getScore()));

                DrawHelper.drawRectangleOnImage(resultImage,
                        score.getWidthPosition(), score.getHeightPosition(),
                        patternWrapper.getWidth(), patternWrapper.getHeight(),
                        score.getScaleFactor());
                DrawHelper.makeBrighterRectangleOnImage(resultImage,
                        score.getWidthPosition(), score.getHeightPosition(),
                        patternWrapper.getWidth(), patternWrapper.getHeight(),
                        score.getScaleFactor());
            }
        } else if (!possibleResults.isEmpty()) {
            Collections.sort(possibleResults);
            int possibleMatchAmount = Math.min(possibleResults.size(), maxNumberOfPresentedPossibleResults);

            for (int i = 0; i < possibleMatchAmount; ++i) {
                MatchScore score = possibleResults.get(i);

                ImageStatisticNames statisticName =
                        ImageStatisticNames.valueOf(String.format("METRIC_VALUE_%s",
                                metric == null ? "SUM" : metric.getName()));

                detectorTask.saveStatisticData(statisticName, BigDecimal.valueOf(score.getScore()));

                DrawHelper.drawRectangleOnImage(resultImage,
                        score.getWidthPosition(), score.getHeightPosition(),
                        patternWrapper.getWidth(), patternWrapper.getHeight(),
                        score.getScaleFactor(), score.getDescription());
            }
        }
    }

    private ValidationDecision isPatternMatchValid(MatchScore matchScore, DetectorTaskInput taskInput) {
        QueryImageWrapper queryImageWrapper = taskInput.getQueryImageWrapper();
        BufferedImageWrapper patternWrapper = taskInput.getPatternWrapper();

        BufferedImage matchSubImage = getMatchSubImage(matchScore, queryImageWrapper, patternWrapper);

        Color[][] patternArray = BufferedImageConverter.convertBufferedImageToColorArray(patternWrapper);
        Color[][] subImageArray = BufferedImageConverter.convertBufferedImageToColorArray(matchSubImage);

        ValidationDecision.MatchDecision matchDecision = ValidationDecision.MatchDecision.VALID_MATCH;
        double originalScoreWeight = 2.;
        double newScore = originalScoreWeight * matchScore.getScore();
        for (MatchValidator matchValidator : matchValidators) {
            ValidationDecision validationDecision = matchValidator.validate(patternArray, subImageArray);
            String validatorName = matchValidator.getName();

            newScore += validationDecision.getScoreValue();
            ValidationDecision.MatchDecision decision = validationDecision.getMatchDecision();
            if (decision == ValidationDecision.MatchDecision.NOT_A_CHANCE) {
                logger.info("Matching rejected by {} validator", validatorName);
                return new ValidationDecision(ValidationDecision.MatchDecision.NOT_A_CHANCE, matchScore.getScore());
            } else if (decision == ValidationDecision.MatchDecision.PROBABLY_MATCH) {
                logger.info("Matching set as POSSIBLE by {} validator", validatorName);
                matchDecision = ValidationDecision.MatchDecision.PROBABLY_MATCH;
            }
        }
        newScore /= (matchValidators.length + originalScoreWeight);
        return new ValidationDecision(matchDecision, newScore);
    }

    private void saveBestMatchImages(List<MatchScore> results, int bestScoresAmount, DetectorTaskInput taskInput) {
        QueryImageWrapper queryImageWrapper = taskInput.getQueryImageWrapper();
        BufferedImageWrapper patternWrapper = taskInput.getPatternWrapper();

        for (int suffixNum = 0; suffixNum < bestScoresAmount; ++suffixNum) {
            MatchScore matchScore = results.get(suffixNum);
            logger.info("Matching {}:", suffixNum);

            BufferedImage matchSubImage = getMatchSubImage(matchScore, queryImageWrapper, patternWrapper);

            String prefixName = "templateMatchImage";
//            prefixName = "bon-pattern1-shelfstoper_col-abs";
//            prefixName = "zubr-pattern1-poster_col-abs";
            try {
                String filename = String.format("%s-%03d.png", prefixName, suffixNum);
                logger.info("Saving file: {}", filename);
                ImageIO.write(matchSubImage, "PNG", new File(filename));
            } catch (IOException e) {
                logger.error("Problem during saving subImage!", e);
            }
        }
    }

    private BufferedImage getMatchSubImage(MatchScore matchScore, QueryImageWrapper queryImageWrapper, BufferedImageWrapper patternWrapper) {
        double scaleFactor = matchScore.getScaleFactor();
        BufferedImage queryImage = queryImageWrapper.extract(scaleFactor);
        return queryImage.getSubimage(
                matchScore.getWidthPosition(), matchScore.getHeightPosition(),
                patternWrapper.getWidth(), patternWrapper.getHeight());
    }
}
