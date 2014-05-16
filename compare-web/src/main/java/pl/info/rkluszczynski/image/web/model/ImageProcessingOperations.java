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
import pl.info.rkluszczynski.image.engine.tasks.metric.AbsMetric;
import pl.info.rkluszczynski.image.engine.tasks.metric.NRMSEMetric;
import pl.info.rkluszczynski.image.engine.tasks.metric.PSNRMetric;
import pl.info.rkluszczynski.image.engine.tasks.metric.RMSEMetric;
import pl.info.rkluszczynski.image.web.model.view.ImageOperationItem;

import java.util.List;

@Component
@Qualifier("imageProcessingOperations")
public class ImageProcessingOperations {

    private List<ImageOperationItem> operationDescriptions;

    public ImageProcessingOperations() {
        operationDescriptions = Lists.newArrayList(
                new ImageOperationItem("imagePatternMatchingABS", "Image pattern matching (metric: ABS)"),
                new ImageOperationItem("imagePatternMatchingRMSE", "Image pattern matching (metric: RMSE)"),
                new ImageOperationItem("imagePatternMatchingNRMSE", "Image pattern matching (metric: NRMSE)"),
                new ImageOperationItem("imagePatternMatchingPSNR", "Image pattern matching (metric: PSNR)"),
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
            case "imagePatternMatchingABS":
                return new PyramidCompareTask(sessionData, new AbsMetric());
            case "imagePatternMatchingRMSE":
                return new PyramidCompareTask(sessionData, new RMSEMetric());
            case "imagePatternMatchingNRMSE":
                return new PyramidCompareTask(sessionData, new NRMSEMetric());
            case "imagePatternMatchingPSNR":
                return new PyramidCompareTask(sessionData, new PSNRMetric());
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
