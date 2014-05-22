package pl.info.rkluszczynski.image.engine.tasks.pyramid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.info.rkluszczynski.image.engine.model.ImageStatisticNames;
import pl.info.rkluszczynski.image.engine.tasks.AbstractTask;
import pl.info.rkluszczynski.image.engine.tasks.metrics.Metric;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;

final
public class SingleScaleStepProcessor {
    protected static Logger logger = LoggerFactory.getLogger(SingleScaleStepProcessor.class);

    private final BufferedImage inputImage;
    private final BufferedImage templateImage;
    private final Metric metric;
    private final double scaleFactor;

    public void process(BufferedImage resultImage, AbstractTask processingTask, double fullScaleStepProgress) {
        double bestResult = Double.MAX_VALUE;
        int bestLeftPosition = -1;
        int bestTopPosition = -1;

        double oneRowProgress = fullScaleStepProgress / (inputImage.getWidth() - templateImage.getWidth());
        double matchDivisor = 256. * templateImage.getHeight() * templateImage.getWidth();

        for (int iw = 0; iw < inputImage.getWidth() - templateImage.getWidth(); ++iw) {
            for (int ih = 0; ih < inputImage.getHeight() - templateImage.getHeight(); ++ih) {
                double result = checkPatternAtImagePosition(iw, ih, inputImage, templateImage);
                if (result < bestResult) {
                    bestResult = result;
                    bestLeftPosition = iw;
                    bestTopPosition = ih;
                }
            }
            processingTask.addProgress(oneRowProgress);
        }

        if (bestResult < Double.MAX_VALUE) {
            logger.info("Best result at level: " + bestResult + " at (" + bestLeftPosition + ", " + bestTopPosition + ")");

            ImageStatisticNames statisticName = ImageStatisticNames.valueOf(String.format("METRIC_VALUE_%s", metric.getName()));
            processingTask.saveStatisticData(statisticName, BigDecimal.valueOf(bestResult / matchDivisor));

            drawRectangleOnImage(resultImage, bestLeftPosition, bestTopPosition, templateImage.getWidth(), templateImage.getHeight(), scaleFactor);
        }
    }

    private void drawRectangleOnImage(BufferedImage image, int leftPosition, int topPosition, int width, int height, double scaleFactor) {
        double invertedScaleFactor = 1. / scaleFactor;
        int scaledLeftPosition = (int) (invertedScaleFactor * leftPosition);
        int scaledTopPosition = (int) (invertedScaleFactor * topPosition);
        int scaledWidth = (int) (invertedScaleFactor * width);
        int scaledHeight = (int) (invertedScaleFactor * height);

        Graphics2D graph = image.createGraphics();
        graph.setColor(Color.WHITE);
//        graph.fill(new Rectangle(scaledLeftPosition, scaledTopPosition, scaledWidth, scaledHeight));
        graph.draw(new Rectangle(scaledLeftPosition, scaledTopPosition, scaledWidth, scaledHeight));
        graph.dispose();
    }

    private double checkPatternAtImagePosition(int w, int h, BufferedImage scaledInputImage, BufferedImage templateImage) {
        assert metric != null;
        metric.resetValue();
        for (int piw = 0; piw < templateImage.getWidth(); ++piw) {
            for (int pih = 0; pih < templateImage.getHeight(); ++pih) {
                Color scaledInputImagePixelValue = new Color(scaledInputImage.getRGB(w + piw, h + pih));
                Color templateImagePixelValue = new Color(templateImage.getRGB(piw, pih));

                metric.addPixelsDifference(scaledInputImagePixelValue, templateImagePixelValue);
            }
        }
        return metric.calculateValue();
    }


    public static SingleScaleStepProcessor create(BufferedImage inputImage, BufferedImage templateImage, Metric metric, double scaleFactor) {
        return new SingleScaleStepProcessor(inputImage, templateImage, metric, scaleFactor);
    }

    private SingleScaleStepProcessor(BufferedImage inputImage, BufferedImage templateImage, Metric metric, double scaleFactor) {
        this.inputImage = inputImage;
        this.templateImage = templateImage;
        this.metric = metric;
        this.scaleFactor = scaleFactor;
    }
}
