package pl.info.rkluszczynski.image.engine.tasks;

import pl.info.rkluszczynski.image.engine.model.SessionData;
import pl.info.rkluszczynski.image.engine.model.strategies.PatternMatchStrategy;
import pl.info.rkluszczynski.image.engine.tasks.input.DetectorTaskInput;
import pl.info.rkluszczynski.image.engine.tasks.input.TasksProperties;
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

    private Integer multistageScaleDepth;
    private Double multistageScaleRatio;
    private double fullScaleStepProgress;

    private BufferedImage resultImage;
    private BufferedImageWrapper patternWrapper;

    public MultiScaleStageTask(SessionData sessionData, DetectorTaskInput taskInput) {
        super(sessionData, taskInput);
    }

    @Override
    public void prepareImageData(BufferedImage inputImage, BufferedImage patternImage) {
        int[] suggestedProcessingSizes = SizeSupplier.getSuggestedProcessingSizes(inputImage);
        int compromiseWidth = suggestedProcessingSizes[0];
        int compromiseHeight = suggestedProcessingSizes[1];
        logger.info("Determined image scaling to width={} and height={}", compromiseWidth, compromiseHeight);

        resultImage = ImageHelper.scaleImagePixelsValue(
                ImageSizeScaleProcessor.getExactScaledImage(inputImage, compromiseWidth, compromiseHeight), 0.7);
        patternWrapper = new BufferedImageWrapper(patternImage);

        DetectorTaskInput taskInput = getTaskInput();
        taskInput.setPatternWrapper(patternWrapper);
        taskInput.setResultImage(resultImage);
    }

    @Override
    public void initialize(TasksProperties tasksProperties) {
        multistageScaleDepth = tasksProperties.getMultistageScaleDepth();
        multistageScaleRatio = tasksProperties.getMultistageScaleRatio();
        fullScaleStepProgress = 1. / (2. * multistageScaleDepth + 1.);
    }

    @Override
    public void processImageData(BufferedImage inputImage, BufferedImage patternImage) {
        int compromiseWidth = resultImage.getWidth();
        int compromiseHeight = resultImage.getHeight();

        logger.info("Number of non alpha pixels: {} (out of {})", patternWrapper.countNonAlphaPixels(),
                patternImage.getWidth() * patternImage.getHeight());
        for (int scaleStep = -multistageScaleDepth; scaleStep <= multistageScaleDepth; ++scaleStep) {
            double scaleFactor = 1. + scaleStep * multistageScaleRatio;

            int pyramidStepDesiredWidth = (int) (scaleFactor * compromiseWidth);
            int pyramidStepDesiredHeight = (int) (scaleFactor * compromiseHeight);

            logger.info("Pyramid scale step {} with factor {} to width={} and height={}",
                    scaleStep, scaleFactor, pyramidStepDesiredWidth, pyramidStepDesiredHeight);
            BufferedImage scaledInputImage =
                    ImageSizeScaleProcessor.getExactScaledImage(inputImage, pyramidStepDesiredWidth, pyramidStepDesiredHeight);

            SingleStageProcessor singleStageProcessor = SingleStageProcessor.create(
                    scaledInputImage, getTaskInput(), scaleFactor
            );
            singleStageProcessor.process(this, fullScaleStepProgress);
        }
    }

    @Override
    public void storeResults() {
        DetectorTaskInput taskInput = getTaskInput();

        PatternMatchStrategy matchStrategy = taskInput.getStrategy();
        matchStrategy.applyBestScores(this, taskInput);

        saveResultImage(taskInput.getResultImage());
    }
}
