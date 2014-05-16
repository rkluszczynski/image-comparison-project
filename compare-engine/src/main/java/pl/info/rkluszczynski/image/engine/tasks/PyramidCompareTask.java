package pl.info.rkluszczynski.image.engine.tasks;

import pl.info.rkluszczynski.image.engine.model.ImageStatisticNames;
import pl.info.rkluszczynski.image.engine.model.SessionData;
import pl.info.rkluszczynski.image.engine.tasks.metric.AbsMetric;
import pl.info.rkluszczynski.image.engine.tasks.metric.Metric;
import pl.info.rkluszczynski.image.engine.utils.ImageSizeScaleProcessor;
import pl.info.rkluszczynski.image.utils.ImageHelper;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;


public class PyramidCompareTask extends AbstractTask {
    private static final int SCALE_PYRAMID_DEPTH = 1;
    private static final double SCALE_PYRAMID_RATIO = .1;

    private final double FULL_SCALE_STEP_PROGRESS = 1. / (2. * SCALE_PYRAMID_DEPTH + 1.);

    public PyramidCompareTask(SessionData sessionData) {
        super(sessionData);
    }

    @Override
    public void processImageData(BufferedImage inputImage, BufferedImage templateImage) {
        int compromiseWidth = 300;
        int compromiseHeight = 400;
        BufferedImage resultImage = ImageHelper.scaleImagePixelsValue(inputImage, 0.8);

        for (int scaleStep = -SCALE_PYRAMID_DEPTH; scaleStep <= SCALE_PYRAMID_DEPTH; ++scaleStep) {
            double scaleFactor = 1. + scaleStep * SCALE_PYRAMID_RATIO;

            int pyramidStepDesiredWidth = (int) (scaleFactor * compromiseWidth);
            int pyramidStepDesiredHeight = (int) (scaleFactor * compromiseHeight);

            logger.info("Pyramid scale step {} to width={} and height={}", scaleStep, pyramidStepDesiredWidth, pyramidStepDesiredHeight);
            BufferedImage scaledInputImage =
                    ImageSizeScaleProcessor.getExactScaledImage(inputImage, pyramidStepDesiredWidth, pyramidStepDesiredHeight);

            processPyramidStepImageData(scaledInputImage, templateImage, resultImage);
        }
        saveResultImage(resultImage);
    }

    private void processPyramidStepImageData(BufferedImage scaledInputImage, BufferedImage templateImage, BufferedImage resultImage) {
        double bestResult = Double.MAX_VALUE;
        int bestLeftPosition = -1;
        int bestTopPosition = -1;

        double oneRowProgress = FULL_SCALE_STEP_PROGRESS / (scaledInputImage.getWidth() - templateImage.getWidth());
        double matchDivisor = 256. * templateImage.getHeight() * templateImage.getWidth();

        for (int iw = 0; iw < scaledInputImage.getWidth() - templateImage.getWidth(); ++iw) {
            for (int ih = 0; ih < scaledInputImage.getHeight() - templateImage.getHeight(); ++ih) {
                double result = checkPatternAtImagePosition(iw, ih, scaledInputImage, templateImage);
                if (result < bestResult) {
                    bestResult = result;
                    bestLeftPosition = iw;
                    bestTopPosition = ih;
                }
            }
            addProgress(oneRowProgress);
        }

        if (bestResult < Double.MAX_VALUE) {
            logger.info("Best result at level: " + bestResult + " at (" + bestLeftPosition + ", " + bestTopPosition + ")");
            saveStatisticData(ImageStatisticNames.DUMMY_RESULT, BigDecimal.valueOf(bestResult / matchDivisor));
            drawRectangleOnImage(resultImage, bestLeftPosition, bestTopPosition, templateImage.getWidth(), templateImage.getHeight());
        }
    }

    private void drawRectangleOnImage(BufferedImage image, int leftPosition, int topPosition, int width, int height) {
        Graphics2D graph = image.createGraphics();
        graph.setColor(Color.WHITE);
//        graph.fill(new Rectangle(leftPosition, topPosition, width, height));
        graph.draw(new Rectangle(leftPosition, topPosition, width, height));
        graph.dispose();
    }

    private double checkPatternAtImagePosition(int w, int h, BufferedImage scaledInputImage, BufferedImage templateImage) {
        Metric compareMetric = new AbsMetric();
        for (int piw = 0; piw < templateImage.getWidth(); ++piw) {
            for (int pih = 0; pih < templateImage.getHeight(); ++pih) {
                Color scaledInputImagePixelValue = new Color(scaledInputImage.getRGB(w + piw, h + pih));
                Color templateImagePixelValue = new Color(templateImage.getRGB(piw, pih));

                compareMetric.addPixelsDifference(scaledInputImagePixelValue, templateImagePixelValue);
            }
        }
        assert compareMetric != null;
        return compareMetric.calculateValue();
    }
}
