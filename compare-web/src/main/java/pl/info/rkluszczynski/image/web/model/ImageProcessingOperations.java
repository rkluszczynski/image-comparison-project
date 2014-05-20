package pl.info.rkluszczynski.image.web.model;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import pl.info.rkluszczynski.image.engine.model.SessionData;
import pl.info.rkluszczynski.image.engine.tasks.CopyInputImageTask;
import pl.info.rkluszczynski.image.engine.tasks.Im4JavaTask;
import pl.info.rkluszczynski.image.engine.tasks.ImageDifferenceTask;
import pl.info.rkluszczynski.image.engine.tasks.PyramidCompareTask;
import pl.info.rkluszczynski.image.engine.tasks.metrics.*;
import pl.info.rkluszczynski.image.web.model.view.ImageOperationItem;

import java.util.List;

@Component
@Qualifier("imageProcessingOperations")
public class ImageProcessingOperations {

    private List<ImageOperationItem> operationDescriptions;

    public ImageProcessingOperations() {
        operationDescriptions = Lists.newArrayList(
                new ImageOperationItem("imagePatternMatchingColorABS", "Image pattern matching (color metric: ABS)"),
                new ImageOperationItem("imagePatternMatchingColorRMSE", "Image pattern matching (color metric: RMSE)"),
//                new ImageOperationItem("imagePatternMatchingColorNRMSE", "Image pattern matching (color metric: NRMSE)"),
                new ImageOperationItem("imagePatternMatchingColorPSNR", "Image pattern matching (color metric: PSNR)"),

                new ImageOperationItem("imagePatternMatchingGrayScaleABS", "Image pattern matching (grayscale metric: ABS)"),
                new ImageOperationItem("imagePatternMatchingGrayScaleRMSE", "Image pattern matching (grayscale metric: RMSE)"),
                new ImageOperationItem("imagePatternMatchingGrayScalePSNR", "Image pattern matching (grayscale metric: PSNR)"),

//                new ImageOperationItem("testingIm4Java", "Testing im4java convert"),
                new ImageOperationItem("imageDifference", "Calculate image difference"),
                new ImageOperationItem("copyInputImage", "Copy input image after delay")
        );
    }

    public List<ImageOperationItem> getOperationDescriptions() {
        return operationDescriptions;
    }

    public Runnable getProcessingTask(String operation, SessionData sessionData) {
        switch (operation) {
            case "copyInputImage":
                return new CopyInputImageTask(sessionData);
            case "imageDifference":
                return new ImageDifferenceTask(sessionData);
            case "testingIm4Java":
                return new Im4JavaTask(sessionData);
            case "imagePatternMatchingColorABS":
                return new PyramidCompareTask(sessionData, new AbsColorMetric());
            case "imagePatternMatchingColorRMSE":
                return new PyramidCompareTask(sessionData, new RMSEColorMetric());
            case "imagePatternMatchingColorNRMSE":
                return new PyramidCompareTask(sessionData, new NRMSEColorMetric());
            case "imagePatternMatchingColorPSNR":
                return new PyramidCompareTask(sessionData, new PSNRColorMetric());
            case "imagePatternMatchingGrayScaleABS":
                return new PyramidCompareTask(sessionData, new AbsAveGrayScaleMetric());
            case "imagePatternMatchingGrayScaleRMSE":
                return new PyramidCompareTask(sessionData, new RMSEAveGrayScaleMetric());
            case "imagePatternMatchingGrayScalePSNR":
                return new PyramidCompareTask(sessionData, new PSNRAveGrayScaleMetric());
            default:
                return null;
        }
    }

    public boolean isOperationNameNotValid(final String operation) {
        return Collections2.filter(operationDescriptions, new Predicate<ImageOperationItem>() {
            @Override
            public boolean apply(ImageOperationItem input) {
                return input.getValue().equalsIgnoreCase(operation);
            }
        }).isEmpty();
    }
}
