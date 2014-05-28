package pl.info.rkluszczynski.image.engine.utils;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Rafal on 2014-05-29.
 */
final
public class DrawHelper {

    static
    public void drawRectangleOnImage(BufferedImage image, int leftPosition, int topPosition, int width, int height, double scaleFactor) {
        double invertedScaleFactor = 1. / scaleFactor;
        int scaledLeftPosition = (int) (invertedScaleFactor * leftPosition);
        int scaledTopPosition = (int) (invertedScaleFactor * topPosition);
        int scaledWidth = (int) (invertedScaleFactor * width);
        int scaledHeight = (int) (invertedScaleFactor * height);

        Graphics2D graph = image.createGraphics();
        graph.setColor(Color.WHITE);
//        graph.fill(new Rectangle(scaledLeftPosition, scaledTopPosition, scaledWidth, scaledHeight));
        graph.draw(new Rectangle(scaledLeftPosition, scaledTopPosition, scaledWidth, scaledHeight));
        graph.dispose();
    }

    private DrawHelper() {
    }
}
