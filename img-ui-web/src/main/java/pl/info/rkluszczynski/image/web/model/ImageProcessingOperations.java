package pl.info.rkluszczynski.image.web.model;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import pl.info.rkluszczynski.image.engine.model.SessionData;
import pl.info.rkluszczynski.image.engine.tasks.CopyInputImageTask;

import java.util.List;

@Component
@Qualifier("imageProcessingOperations")
public class ImageProcessingOperations {

    private List<ImageOperationItem> operationDescriptions;

    public ImageProcessingOperations() {
        operationDescriptions = Lists.newArrayList(
                new ImageOperationItem("copyInputImage", "Copy input image after delay")
        );
    }

    public List<ImageOperationItem> getOperationDescriptions() {
        return operationDescriptions;
    }

    public Runnable getProcessingTask(String operation, SessionData sessionData) {
        if (operation.equalsIgnoreCase("copyInputImage")) {
            return new CopyInputImageTask(sessionData);
        }
        return null;
    }
}
