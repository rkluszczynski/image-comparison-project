package pl.info.rkluszczynski.image.engine.tasks;

import pl.info.rkluszczynski.image.engine.model.SessionData;
import pl.info.rkluszczynski.image.engine.model.strategies.PatternMatchStrategy;
import pl.info.rkluszczynski.image.engine.tasks.multiscale.SingleStageProcessor;
import pl.info.rkluszczynski.image.engine.tasks.multiscale.SizeSupplier;
import pl.info.rkluszczynski.image.engine.utils.BufferedImageWrapper;
import pl.info.rkluszczynski.image.engine.utils.ImageSizeScaleProcessor;
import pl.info.rkluszczynski.image.utils.ImageHelper;

import java.awt.image.BufferedImage;

/**
 * Created by Rafal on 2014-06-01.
 */
public class MultiScaleStageTask extends AbstractDetectorTask {
    private final int SCALE_PYRAMID_DEPTH = 1;
    private final double SCALE_PYRAMID_RATIO = .1;

    private final double FULL_SCALE_STEP_PROGRESS = 1. / (2. * SCALE_PYRAMID_DEPTH + 1.);


    public MultiScaleStageTask(SessionData sessionData, DetectorTaskInput taskInput) {
        super(sessionData, taskInput);
    }

    @Override
    public void processImageData(BufferedImage inputImage, BufferedImage patternImage) {
        int[] suggestedProcessingSizes = SizeSupplier.getSuggestedProcessingSizes(inputImage);
        int compromiseWidth = suggestedProcessingSizes[0];
        int compromiseHeight = suggestedProcessingSizes[1];
        logger.info("Determined image scaling to width={} and height={}", compromiseWidth, compromiseHeight);

        BufferedImage resultImage = ImageHelper.scaleImagePixelsValue(
                ImageSizeScaleProcessor.getExactScaledImage(inputImage, compromiseWidth, compromiseHeight), 0.7);
        BufferedImageWrapper patternImageWrapper = new BufferedImageWrapper(patternImage);

        getTaskInput().setPatternWrapper(patternImageWrapper);
        getTaskInput().setResultImage(resultImage);
        // TODO: move initialization in some automatically executed place
        getTaskInput().getStrategy().initialize(taskInput);
        getTaskInput().getComparator().initialize(taskInput);


        logger.info("Number of non alpha pixels: {} (out of {})", patternImageWrapper.countNonAlphaPixels(),
                patternImage.getWidth() * patternImage.getHeight());
        for (int scaleStep = -SCALE_PYRAMID_DEPTH; scaleStep <= SCALE_PYRAMID_DEPTH; ++scaleStep) {
            double scaleFactor = 1. + scaleStep * SCALE_PYRAMID_RATIO;

            int pyramidStepDesiredWidth = (int) (scaleFactor * compromiseWidth);
            int pyramidStepDesiredHeight = (int) (scaleFactor * compromiseHeight);

            logger.info("Pyramid scale step {} with factor {} to width={} and height={}",
                    scaleStep, scaleFactor, pyramidStepDesiredWidth, pyramidStepDesiredHeight);
            BufferedImage scaledInputImage =
                    ImageSizeScaleProcessor.getExactScaledImage(inputImage, pyramidStepDesiredWidth, pyramidStepDesiredHeight);

            SingleStageProcessor singleStageProcessor = SingleStageProcessor.create(
                    scaledInputImage, getTaskInput(), scaleFactor
            );
            singleStageProcessor.process(
                    this,
                    FULL_SCALE_STEP_PROGRESS);
        }
    }

    @Override
    public void storeResults() {
        PatternMatchStrategy matchStrategy = getTaskInput().getStrategy();
        matchStrategy.applyBestScores(this, getTaskInput());

        saveResultImage(getTaskInput().getResultImage());
    }
}
