package pl.info.rkluszczynski.image.engine.model;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BufferedImageMarker {
    private final BufferedImage imageMarker;
    private final long occurrencesCount;
    private Color markerColor = Color.BLACK;

    public BufferedImageMarker(BufferedImage imageMarker, long occurrencesCount) {
        this.imageMarker = imageMarker;
        this.occurrencesCount = occurrencesCount;
    }

    public BufferedImage getImageMarker() {
        return imageMarker;
    }

    public long getOccurrencesCount() {
        return occurrencesCount;
    }

    public Color getMarkerColor() {
        return markerColor;
    }

    public void setMarkerColor(Color markerColor) {
        this.markerColor = markerColor;
    }
}
