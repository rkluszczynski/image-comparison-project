package pl.info.rkluszczynski.image.engine.tasks.multiscale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.info.rkluszczynski.image.engine.model.comparators.PatternMatchComparator;
import pl.info.rkluszczynski.image.engine.model.strategies.MatchScore;
import pl.info.rkluszczynski.image.engine.model.strategies.PatternMatchStrategy;
import pl.info.rkluszczynski.image.engine.tasks.PatternDetectorTask;
import pl.info.rkluszczynski.image.engine.tasks.input.DetectorTaskInput;
import pl.info.rkluszczynski.image.engine.utils.BufferedImageWrapper;

import java.awt.image.BufferedImage;

final
public class SingleStageProcessor {
    protected static Logger logger = LoggerFactory.getLogger(SingleStageProcessor.class);

    private final BufferedImage inputImage;
    private final DetectorTaskInput taskInput;
    private final double scaleFactor;

    private SingleStageProcessor(BufferedImage inputImage, DetectorTaskInput taskInput, double scaleFactor) {
        this.inputImage = inputImage;
        this.taskInput = taskInput;
        this.scaleFactor = scaleFactor;
    }

    public static SingleStageProcessor create(DetectorTaskInput taskInput, double scaleFactor) {
        BufferedImage image = taskInput.getQueryImageWrapper().extract(scaleFactor);
        return new SingleStageProcessor(image, taskInput, scaleFactor);
    }

    public void process(PatternDetectorTask detectorTask, double fullScaleStepProgress) {
        BufferedImageWrapper patternWrapper = taskInput.getPatternWrapper();
        PatternMatchStrategy matchStrategy = taskInput.getStrategy();
        PatternMatchComparator matchComparator = taskInput.getComparator();

        double oneRowProgress = fullScaleStepProgress / (inputImage.getWidth() - patternWrapper.getWidth());
        for (int iw = 0; iw < inputImage.getWidth() - patternWrapper.getWidth(); ++iw) {
            for (int ih = 0; ih < inputImage.getHeight() - patternWrapper.getHeight(); ++ih) {
                double score = matchComparator.calculatePatternMatchScore(inputImage, iw, ih);
                MatchScore matchScore = new MatchScore(score, iw, ih, scaleFactor);
                matchStrategy.putScore(matchScore);
            }
            detectorTask.addProgress(oneRowProgress);
        }
    }
}
