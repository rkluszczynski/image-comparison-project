package pl.info.rkluszczynski.image.engine;

import pl.info.rkluszczynski.image.engine.model.metrics.RMSEColorMetric;
import pl.info.rkluszczynski.image.engine.utils.DrawHelper;

import java.awt.*;
import java.awt.image.BufferedImage;

final
public class ImageBlockDiffCalc {
    private static int BLOCK_SIZE = 8;

    private ImageBlockDiffCalc() {
    }

    static
    public double processImageBlockByBlock(BufferedImage image1, BufferedImage image2) {
        int imageWidth = image1.getWidth();
        int imageHeight = image1.getHeight();

        double resultValue = 0.;
        for (int iw = 0; iw < imageWidth; iw += BLOCK_SIZE) {
            for (int ih = 0; ih < imageHeight; ih += BLOCK_SIZE) {
                BufferedImage subImage1 = copyImageBlockToSubImage(image1, iw, ih);
                BufferedImage subImage2 = copyImageBlockToSubImage(image2, iw, ih);

                double value = compareSubImages(subImage1, subImage2);
                resultValue = Math.max(resultValue, value);
            }
        }
        return resultValue;
    }

    private static double compareSubImages(BufferedImage subImage1, BufferedImage subImage2) {
        RMSEColorMetric metric = new RMSEColorMetric();
        Color[][] subImageArr1 = ImageDiffer.convertImageToColorArray(subImage1);
        Color[][] subImageArr2 = ImageDiffer.convertImageToColorArray(subImage2);
        metric.resetValue();
        for (int iw = 0; iw < BLOCK_SIZE; ++iw) {
            for (int ih = 0; ih < BLOCK_SIZE; ++ih) {
                metric.addPixelsDifference(subImageArr1[iw][ih], subImageArr2[iw][ih]);
            }
        }
        return metric.calculateValue();
    }

    private static BufferedImage copyImageBlockToSubImage(BufferedImage image1, int iw, int ih) {
        BufferedImage subImage = DrawHelper.createEmptyImage(BLOCK_SIZE, BLOCK_SIZE);
        for (int siw = 0; siw < BLOCK_SIZE; ++siw) {
            for (int sih = 0; sih < BLOCK_SIZE; ++sih) {
                if (iw + siw < image1.getWidth() && ih + sih < image1.getHeight())
                    subImage.setRGB(siw, sih,
                            image1.getRGB(iw + siw, ih + sih));
            }
        }
        return subImage;
    }
}
