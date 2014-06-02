package pl.info.rkluszczynski.image.web.model;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import pl.info.rkluszczynski.image.engine.model.SessionData;
import pl.info.rkluszczynski.image.engine.model.comparators.PatternMatchComparator;
import pl.info.rkluszczynski.image.engine.model.comparators.PixelDifferenceComparator;
import pl.info.rkluszczynski.image.engine.model.metrics.*;
import pl.info.rkluszczynski.image.engine.model.strategies.BestLocalizedMatchStrategy;
import pl.info.rkluszczynski.image.engine.model.strategies.PatternMatchStrategy;
import pl.info.rkluszczynski.image.engine.tasks.DetectorTaskInput;
import pl.info.rkluszczynski.image.engine.tasks.Im4JavaCompareTask;
import pl.info.rkluszczynski.image.engine.tasks.ImageDifferenceTask;
import pl.info.rkluszczynski.image.engine.tasks.MultiScaleStageTask;
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
        PatternMatchStrategy matchStrategy = new BestLocalizedMatchStrategy();
        PatternMatchComparator matchComparator = new PixelDifferenceComparator();

        switch (operation) {
            case "imagePatternMatchingColorABS":
                return new MultiScaleStageTask(sessionData,
                        new DetectorTaskInput(new AbsColorMetric(), matchComparator, matchStrategy));
            case "imagePatternMatchingColorRMSE":
                return new MultiScaleStageTask(sessionData,
                        new DetectorTaskInput(new RMSEColorMetric(), matchComparator, matchStrategy));
            case "imagePatternMatchingColorNRMSE":
                return new MultiScaleStageTask(sessionData,
                        new DetectorTaskInput(new NRMSEColorMetric(), matchComparator, matchStrategy));
            case "imagePatternMatchingColorPSNR":
                return new MultiScaleStageTask(sessionData,
                        new DetectorTaskInput(new PSNRColorMetric(), matchComparator, matchStrategy));
            case "imagePatternMatchingGrayScaleABS":
                return new MultiScaleStageTask(sessionData,
                        new DetectorTaskInput(new AbsAveGrayScaleMetric(), matchComparator, matchStrategy));
            case "imagePatternMatchingGrayScaleRMSE":
                return new MultiScaleStageTask(sessionData,
                        new DetectorTaskInput(new RMSEAveGrayScaleMetric(), matchComparator, matchStrategy));
            case "imagePatternMatchingGrayScalePSNR":
                return new MultiScaleStageTask(sessionData,
                        new DetectorTaskInput(new PSNRAveGrayScaleMetric(), matchComparator, matchStrategy));
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
}
