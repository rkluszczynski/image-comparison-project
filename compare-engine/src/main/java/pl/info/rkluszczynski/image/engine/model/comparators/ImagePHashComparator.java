package pl.info.rkluszczynski.image.engine.model.comparators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.info.rkluszczynski.image.compare.phash.HammingDistance;
import pl.info.rkluszczynski.image.compare.phash.ImagePHash03;
import pl.info.rkluszczynski.image.engine.model.metrics.CompareMetric;
import pl.info.rkluszczynski.image.engine.tasks.input.DetectorTaskInput;
import pl.info.rkluszczynski.image.engine.utils.BufferedImageWrapper;

import java.awt.image.BufferedImage;

public class ImagePHashComparator extends AbstractMatchComparator {
    protected static final Logger logger = LoggerFactory.getLogger(ImagePHashComparator.class);

    ImagePHash03 imagePHash03 = new ImagePHash03();
    String patternPHash;

    public ImagePHashComparator(CompareMetric metric) {
        super(metric);
    }

    @Override
    public void initialize(DetectorTaskInput taskInput) {
        super.initialize(taskInput);
        BufferedImageWrapper patternWrapper = taskInput.getPatternWrapper();
        patternPHash = imagePHash03.getHash(patternWrapper.getBufferedImage());
    }

    @Override
    public double calculatePatternMatchScore(BufferedImage image, int widthPosition, int heightPosition) {
        DetectorTaskInput taskInput = getTaskInput();
        BufferedImageWrapper patternWrapper = taskInput.getPatternWrapper();

        BufferedImage subImage = image.getSubimage(widthPosition, heightPosition, patternWrapper.getWidth(), patternWrapper.getHeight());
        String subImagePHash = imagePHash03.getHash(subImage);

        int distance = HammingDistance.calculate(patternPHash, subImagePHash);
        logger.info("Distance {} with pHashes {}, {} at position ({}, {})", distance,
                patternPHash, subImagePHash, widthPosition, heightPosition);
        return (distance > 10) ? -1 : 0;
    }
}
