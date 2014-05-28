package pl.info.rkluszczynski.image.engine.tasks;

import pl.info.rkluszczynski.image.engine.model.SessionData;
import pl.info.rkluszczynski.image.engine.tasks.metrics.Metric;
import pl.info.rkluszczynski.image.engine.tasks.pyramid.SingleScaleStepProcessor;
import pl.info.rkluszczynski.image.engine.tasks.pyramid.SizeSupplier;
import pl.info.rkluszczynski.image.engine.tasks.strategy.LowestMetricStrategy;
import pl.info.rkluszczynski.image.engine.utils.BufferedImageWrapper;
import pl.info.rkluszczynski.image.engine.utils.ImageSizeScaleProcessor;
import pl.info.rkluszczynski.image.utils.ImageHelper;

import java.awt.image.BufferedImage;


public class PyramidCompareTask extends AbstractTask {
    private final int SCALE_PYRAMID_DEPTH = 1;
    private final double SCALE_PYRAMID_RATIO = .1;

    private final double FULL_SCALE_STEP_PROGRESS = 1. / (2. * SCALE_PYRAMID_DEPTH + 1.);
    private final Metric metric;


    public PyramidCompareTask(SessionData sessionData, Metric metric) {
        super(sessionData);
        this.metric = metric;
    }

    @Override
    public void processImageData(BufferedImage inputImage, BufferedImage templateImage) {
        int[] suggestedProcessingSizes = SizeSupplier.getSuggestedProcessingSizes(inputImage);
        int compromiseWidth = suggestedProcessingSizes[0];
        int compromiseHeight = suggestedProcessingSizes[1];
        logger.info("Determined image scaling to width={} and height={}", compromiseWidth, compromiseHeight);

        BufferedImage resultImage = ImageHelper.scaleImagePixelsValue(
                ImageSizeScaleProcessor.getExactScaledImage(inputImage, compromiseWidth, compromiseHeight), 0.8);
        BufferedImageWrapper templateImageWrapper = new BufferedImageWrapper(templateImage);
        setMatchStrategy(new LowestMetricStrategy(resultImage, templateImageWrapper));

        logger.info("Number of non alpha pixels: {} (out of {})", templateImageWrapper.countNonAlphaPixels(),
                templateImage.getWidth() * templateImage.getHeight());
        for (int scaleStep = -SCALE_PYRAMID_DEPTH; scaleStep <= SCALE_PYRAMID_DEPTH; ++scaleStep) {
            double scaleFactor = 1. + scaleStep * SCALE_PYRAMID_RATIO;

            int pyramidStepDesiredWidth = (int) (scaleFactor * compromiseWidth);
            int pyramidStepDesiredHeight = (int) (scaleFactor * compromiseHeight);

            logger.info("Pyramid scale step {} with factor {} to width={} and height={}",
                    scaleStep, scaleFactor, pyramidStepDesiredWidth, pyramidStepDesiredHeight);
            BufferedImage scaledInputImage =
                    ImageSizeScaleProcessor.getExactScaledImage(inputImage, pyramidStepDesiredWidth, pyramidStepDesiredHeight);

            SingleScaleStepProcessor singleScaleStepProcessor = SingleScaleStepProcessor.create(
                    scaledInputImage, templateImageWrapper, metric, scaleFactor
            );
            singleScaleStepProcessor.process(resultImage, this, FULL_SCALE_STEP_PROGRESS);
        }
        saveResultImage(resultImage);
    }
}
