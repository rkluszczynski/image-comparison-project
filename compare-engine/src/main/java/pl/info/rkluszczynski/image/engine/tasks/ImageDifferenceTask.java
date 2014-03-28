package pl.info.rkluszczynski.image.engine.tasks;

import pl.info.rkluszczynski.image.engine.model.SessionData;
import pl.info.rkluszczynski.image.engine.utils.ImageSizeScaleProcessor;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static pl.info.rkluszczynski.image.engine.model.ImageStatisticNames.DIFFERENCE_COEFFICIENT;


public class ImageDifferenceTask extends AbstractTask {
    public ImageDifferenceTask(SessionData sessionData) {
        super(sessionData);
    }

    @Override
    public void processImageData(BufferedImage inputImage, BufferedImage templateImage) {
        int maxWidth = Math.max(inputImage.getWidth(), templateImage.getWidth());
        int maxHeight = Math.max(inputImage.getHeight(), templateImage.getHeight());

        BufferedImage resultImage = new BufferedImage(maxWidth, maxHeight, TYPE_INT_RGB);
        BufferedImage scaledInputImage =
                ImageSizeScaleProcessor.getScaledImage(inputImage, maxWidth, maxHeight);
        BufferedImage scaledTemplateImage =
                ImageSizeScaleProcessor.getScaledImage(templateImage, maxWidth, maxHeight);

        int scaledWidth = Math.min(scaledInputImage.getWidth(), scaledTemplateImage.getWidth());
        int scaledHeight = Math.min(scaledInputImage.getHeight(), scaledTemplateImage.getHeight());
        for (int iw = 0; iw < scaledWidth; ++iw) {
            for (int ih = 0; ih < scaledHeight; ++ih) {
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
        resultImage = ImageSizeScaleProcessor.getScaledImage(resultImage,
                inputImage.getWidth(), inputImage.getHeight());

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
