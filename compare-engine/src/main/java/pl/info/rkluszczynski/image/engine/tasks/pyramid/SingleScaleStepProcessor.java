package pl.info.rkluszczynski.image.engine.tasks.pyramid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.info.rkluszczynski.image.engine.tasks.AbstractTask;
import pl.info.rkluszczynski.image.engine.tasks.metrics.Metric;
import pl.info.rkluszczynski.image.engine.tasks.strategy.BestMatchStrategy;
import pl.info.rkluszczynski.image.engine.utils.BufferedImageWrapper;

import java.awt.*;
import java.awt.image.BufferedImage;

final
public class SingleScaleStepProcessor {
    protected static Logger logger = LoggerFactory.getLogger(SingleScaleStepProcessor.class);

    private final BufferedImage inputImage;
    private final BufferedImageWrapper templateImageWrapper;
    private final Metric metric;
    private final double scaleFactor;

    public void process(BufferedImage resultImage, AbstractTask processingTask, double fullScaleStepProgress) {
        double oneRowProgress = fullScaleStepProgress / (inputImage.getWidth() - templateImageWrapper.getWidth());

        BestMatchStrategy matchStrategy = processingTask.getMatchStrategy();
        for (int iw = 0; iw < inputImage.getWidth() - templateImageWrapper.getWidth(); ++iw) {
            for (int ih = 0; ih < inputImage.getHeight() - templateImageWrapper.getHeight(); ++ih) {
                double result = checkPatternAtImagePosition(iw, ih, inputImage, templateImageWrapper);
                matchStrategy.put(result, iw, ih, scaleFactor);
            }
            processingTask.addProgress(oneRowProgress);
        }
    }

    private double checkPatternAtImagePosition(int w, int h, BufferedImage scaledInputImage, BufferedImageWrapper templateImage) {
        assert metric != null;
        metric.resetValue();
        for (int piw = 0; piw < templateImage.getWidth(); ++piw) {
            for (int pih = 0; pih < templateImage.getHeight(); ++pih) {
                if (!templateImage.treatPixelAsAlpha(piw, pih)) {
                    Color scaledInputImagePixelValue = new Color(scaledInputImage.getRGB(w + piw, h + pih));

                    metric.addPixelsDifference(
                            scaledInputImagePixelValue,
                            templateImageWrapper.getPixelColor(piw, pih)
                    );
                }
            }
        }
        return metric.calculateValue();
    }


    public static SingleScaleStepProcessor create(BufferedImage inputImage, BufferedImageWrapper templateImageWrapper, Metric metric, double scaleFactor) {
        return new SingleScaleStepProcessor(inputImage, templateImageWrapper, metric, scaleFactor);
    }

    private SingleScaleStepProcessor(BufferedImage inputImage, BufferedImageWrapper templateImageWrapper, Metric metric, double scaleFactor) {
        this.inputImage = inputImage;
        this.templateImageWrapper = templateImageWrapper;
        this.metric = metric;
        this.scaleFactor = scaleFactor;
    }
}
