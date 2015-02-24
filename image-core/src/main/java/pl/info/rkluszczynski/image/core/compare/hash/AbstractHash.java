package pl.info.rkluszczynski.image.core.compare.hash;

import java.awt.image.BufferedImage;

/**
 * Created by Rafal on 2014-06-20.
 */
public interface AbstractHash {

    String getGrayScaleHash(BufferedImage bufferedImage);

    String[] getColorHashes(BufferedImage bufferedImage);

    String getHashName();

}
