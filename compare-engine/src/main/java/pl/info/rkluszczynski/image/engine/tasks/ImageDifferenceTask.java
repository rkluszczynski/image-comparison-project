package pl.info.rkluszczynski.image.engine.tasks;

import pl.info.rkluszczynski.image.engine.model.SessionData;
import pl.info.rkluszczynski.image.engine.utils.ImageSizeScaleProcessor;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static pl.info.rkluszczynski.image.engine.model.ImageStatisticNames.DIFFERENCE_COEFFICIENT;


public class ImageDifferenceTask extends AbstractDetectorTask {
    private BufferedImage resultImage;

    public ImageDifferenceTask(SessionData sessionData) {
        super(sessionData, null);
    }

    @Override
    public void processImageData(BufferedImage inputImage, BufferedImage templateImage) {
        int compromiseWidth = Math.max(inputImage.getWidth(), templateImage.getWidth());
        int compromiseHeight = Math.max(inputImage.getHeight(), templateImage.getHeight());
        logger.debug("Scaling images to width={} and height={}", compromiseWidth, compromiseHeight);

        resultImage = new BufferedImage(compromiseWidth, compromiseHeight, TYPE_INT_RGB);
        BufferedImage scaledInputImage =
                ImageSizeScaleProcessor.getExactScaledImage(inputImage, compromiseWidth, compromiseHeight);
        BufferedImage scaledTemplateImage =
                ImageSizeScaleProcessor.getExactScaledImage(templateImage, compromiseWidth, compromiseHeight);
        logger.trace("scaledInputImage: width={}, height={}", scaledInputImage.getWidth(), scaledInputImage.getHeight());
        logger.trace("scaledTemplateImage: width={}, height={}", scaledTemplateImage.getWidth(), scaledTemplateImage.getHeight());

        for (int iw = 0; iw < compromiseWidth; ++iw) {
            for (int ih = 0; ih < compromiseHeight; ++ih) {
                Color scaledInputImagePixelValue = new Color(scaledInputImage.getRGB(iw, ih));
                Color scaledTemplateImagePixelValue = new Color(scaledTemplateImage.getRGB(iw, ih));

                Color resultPixelValue = new Color(
                        Math.abs(scaledInputImagePixelValue.getRed() - scaledTemplateImagePixelValue.getRed()),
                        Math.abs(scaledInputImagePixelValue.getGreen() - scaledTemplateImagePixelValue.getGreen()),
                        Math.abs(scaledInputImagePixelValue.getBlue() - scaledTemplateImagePixelValue.getBlue())
                );
                resultImage.setRGB(iw, ih, resultPixelValue.getRGB());
            }
        }
        resultImage = ImageSizeScaleProcessor.getExactScaledImage(resultImage,
                inputImage.getWidth(), inputImage.getHeight());
    }

    @Override
    public void storeResults() {
        saveResultImage(resultImage);
        saveStatisticData(DIFFERENCE_COEFFICIENT, calculateDifferenceCoefficient(resultImage));
    }

    private BigDecimal calculateDifferenceCoefficient(BufferedImage resultImage) {
        double resultDoubleValue = 0.;
        for (int iw = 0; iw < resultImage.getWidth(); ++iw) {
            for (int ih = 0; ih < resultImage.getHeight(); ++ih) {
                Color pixelValue = new Color(resultImage.getRGB(iw, ih));
                resultDoubleValue += (pixelValue.getRed() + pixelValue.getGreen() + pixelValue.getBlue());
            }
        }
        resultDoubleValue = resultDoubleValue / 3.;
        double resultDenominator = 256. * resultImage.getWidth() * resultImage.getHeight();
        resultDoubleValue = resultDoubleValue / resultDenominator;
        return BigDecimal.valueOf(resultDoubleValue);
    }
}
