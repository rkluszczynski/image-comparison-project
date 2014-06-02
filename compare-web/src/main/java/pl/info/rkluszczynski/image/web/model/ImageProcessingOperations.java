package pl.info.rkluszczynski.image.web.model;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import pl.info.rkluszczynski.image.engine.model.SessionData;
import pl.info.rkluszczynski.image.engine.model.comparators.PatternMatchComparator;
import pl.info.rkluszczynski.image.engine.model.comparators.PixelDifferenceComparator;
import pl.info.rkluszczynski.image.engine.model.comparators.SequenceComparator;
import pl.info.rkluszczynski.image.engine.model.metrics.*;
import pl.info.rkluszczynski.image.engine.model.strategies.BestLocalizedMatchStrategy;
import pl.info.rkluszczynski.image.engine.model.strategies.PatternMatchStrategy;
import pl.info.rkluszczynski.image.engine.tasks.Im4JavaCompareTask;
import pl.info.rkluszczynski.image.engine.tasks.ImageDifferenceTask;
import pl.info.rkluszczynski.image.engine.tasks.MultiScaleStageTask;
import pl.info.rkluszczynski.image.engine.tasks.input.DetectorTaskInput;
import pl.info.rkluszczynski.image.web.model.view.ImageOperationItem;

import java.util.List;

@Component
@Qualifier("imageProcessingOperations")
public class ImageProcessingOperations {

    private final List<ImageOperationItem> operationDescriptions;

    public ImageProcessingOperations() {
        operationDescriptions = Lists.newArrayList(
                new ImageOperationItem("imagePatternMatchingColorABS", "Image pattern matching (color metric: ABS)"),
                new ImageOperationItem("imagePatternMatchingColorRMSE", "Image pattern matching (color metric: RMSE)"),
//                new ImageOperationItem("imagePatternMatchingColorNRMSE", "Image pattern matching (color metric: NRMSE)"),
                new ImageOperationItem("imagePatternMatchingColorPSNR", "Image pattern matching (color metric: PSNR)"),

                new ImageOperationItem("imagePatternMatchingGrayScaleABS", "Image pattern matching (grayscale metric: ABS)"),
                new ImageOperationItem("imagePatternMatchingGrayScaleRMSE", "Image pattern matching (grayscale metric: RMSE)"),
                new ImageOperationItem("imagePatternMatchingGrayScalePSNR", "Image pattern matching (grayscale metric: PSNR)"),

                new ImageOperationItem("testingIm4JavaCompare", "Testing im4java compare operation"),
                new ImageOperationItem("imageDifference", "Calculate image difference")
        );
    }

    public List<ImageOperationItem> getOperationDescriptions() {
        return operationDescriptions;
    }

    public Runnable getProcessingTask(String operation, SessionData sessionData) {
        switch (operation) {
            case "imagePatternMatchingColorABS":
                return createMultiScaleTask(sessionData, new AbsColorMetric());
            case "imagePatternMatchingColorRMSE":
                return createMultiScaleTask(sessionData, new RMSEColorMetric());
            case "imagePatternMatchingColorNRMSE":
                return createMultiScaleTask(sessionData, new NRMSEColorMetric());
            case "imagePatternMatchingColorPSNR":
                return createMultiScaleTask(sessionData, new PSNRColorMetric());
            case "imagePatternMatchingGrayScaleABS":
                return createMultiScaleTask(sessionData, new AbsAveGrayScaleMetric());
            case "imagePatternMatchingGrayScaleRMSE":
                return createMultiScaleTask(sessionData, new RMSEAveGrayScaleMetric());
            case "imagePatternMatchingGrayScalePSNR":
                return createMultiScaleTask(sessionData, new PSNRAveGrayScaleMetric());
            case "testingIm4JavaCompare":
                return new Im4JavaCompareTask(sessionData);
            case "imageDifference":
                return new ImageDifferenceTask(sessionData);
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

    private Runnable createMultiScaleTask(SessionData sessionData, Metric metric) {
        PatternMatchStrategy matchStrategy = new BestLocalizedMatchStrategy();
        PatternMatchComparator matchComparator = new SequenceComparator(
                new PixelDifferenceComparator(metric)
        );
        DetectorTaskInput detectorTaskInput = new DetectorTaskInput(matchComparator, matchStrategy);
        return new MultiScaleStageTask(sessionData, detectorTaskInput);
    }
}
