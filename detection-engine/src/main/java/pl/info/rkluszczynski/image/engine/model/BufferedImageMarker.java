package pl.info.rkluszczynski.image.engine.model;

import java.awt.image.BufferedImage;

public class BufferedImageMarker {
    private final BufferedImage imageMarker;
    private final int occurrencesCount;

    public BufferedImageMarker(BufferedImage imageMarker, int occurrencesCount) {
        this.imageMarker = imageMarker;
        this.occurrencesCount = occurrencesCount;
    }

    public BufferedImage getImageMarker() {
        return imageMarker;
    }

    public int getOccurrencesCount() {
        return occurrencesCount;
    }
}
