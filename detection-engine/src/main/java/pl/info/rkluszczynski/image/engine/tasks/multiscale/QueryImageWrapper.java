package pl.info.rkluszczynski.image.engine.tasks.multiscale;

import com.google.common.collect.Maps;

import java.awt.image.BufferedImage;
import java.util.Map;

/**
 * Created by Rafal on 2014-06-21.
 */
public class QueryImageWrapper {
    private Map<Double, BufferedImage> stagesQueryImages = Maps.newHashMap();

    public QueryImageWrapper() {
    }

    public void store(double scaleFactorKey, BufferedImage bufferedImage) {
        stagesQueryImages.put(scaleFactorKey, bufferedImage);
    }

    public BufferedImage extract(double scaleFactorKey) {
        BufferedImage image = stagesQueryImages.get(scaleFactorKey);
        if (image == null)
            throw new NullPointerException("No image for scaleFactor: " + scaleFactorKey);
        return image;
    }
}
