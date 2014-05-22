package pl.info.rkluszczynski.image.engine.tasks;

import pl.info.rkluszczynski.image.engine.model.SessionData;
import pl.info.rkluszczynski.image.engine.tasks.metrics.Metric;
import pl.info.rkluszczynski.image.engine.tasks.pyramid.SingleScaleStepProcessor;
import pl.info.rkluszczynski.image.engine.tasks.pyramid.SizeSupplier;
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
        int compromiseWidth = SizeSupplier.getSuggestedProcessingWidth(inputImage);
        int compromiseHeight = SizeSupplier.getSuggestedProcessingHeight(inputImage);

        BufferedImage resultImage = ImageHelper.scaleImagePixelsValue(inputImage, 0.8);

        for (int scaleStep = -SCALE_PYRAMID_DEPTH; scaleStep <= SCALE_PYRAMID_DEPTH; ++scaleStep) {
            double scaleFactor = 1. + scaleStep * SCALE_PYRAMID_RATIO;

            int pyramidStepDesiredWidth = (int) (scaleFactor * compromiseWidth);
            int pyramidStepDesiredHeight = (int) (scaleFactor * compromiseHeight);

            logger.info("Pyramid scale step {} with factor {} to width={} and height={}",
                    scaleStep, scaleFactor, pyramidStepDesiredWidth, pyramidStepDesiredHeight);
            BufferedImage scaledInputImage =
                    ImageSizeScaleProcessor.getExactScaledImage(inputImage, pyramidStepDesiredWidth, pyramidStepDesiredHeight);

            SingleScaleStepProcessor singleScaleStepProcessor = SingleScaleStepProcessor.create(
                    scaledInputImage, templateImage, metric, scaleFactor
            );
            singleScaleStepProcessor.process(resultImage, this, FULL_SCALE_STEP_PROGRESS);
        }
        saveResultImage(resultImage);
    }
}
