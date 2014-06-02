package pl.info.rkluszczynski.image.engine.tasks;

import pl.info.rkluszczynski.image.engine.model.ImageStatisticNames;
import pl.info.rkluszczynski.image.engine.tasks.input.TasksProperties;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;

/**
 * Created by Rafal on 2014-06-01.
 */
public interface PatternDetectorTask {

    void prepareImageData(BufferedImage inputImage, BufferedImage patternImage);

    void initialize(TasksProperties tasksProperties);

    void processImageData(BufferedImage inputImage, BufferedImage patternImage);

    void storeResults();


    void addProgress(double progress);

    void saveStatisticData(ImageStatisticNames statisticName, BigDecimal statisticValue);

}
