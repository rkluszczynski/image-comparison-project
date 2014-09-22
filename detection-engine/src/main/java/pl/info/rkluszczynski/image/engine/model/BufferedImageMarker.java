package pl.info.rkluszczynski.image.engine.model;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BufferedImageMarker {
    private final BufferedImage imageMarker;
    private final int occurrencesCount;
    private Color markerColor = Color.BLACK;
    private String description = "";

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

    public Color getMarkerColor() {
        return markerColor;
    }

    public void setMarkerColor(Color markerColor) {
        this.markerColor = markerColor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
