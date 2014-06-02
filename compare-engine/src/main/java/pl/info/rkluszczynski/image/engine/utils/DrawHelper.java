package pl.info.rkluszczynski.image.engine.utils;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Rafal on 2014-05-29.
 */
final
public class DrawHelper {

    private DrawHelper() {
    }

    static
    public void drawRectangleOnImage(BufferedImage image, int leftPosition, int topPosition, int width, int height, double scaleFactor) {
        drawRectangleOnImage(image, leftPosition, topPosition, width, height, scaleFactor, null);
    }

    static
    public void drawRectangleOnImage(BufferedImage image, int leftPosition, int topPosition, int width, int height, double scaleFactor, String text) {
        double invertedScaleFactor = 1. / scaleFactor;
        int scaledLeftPosition = (int) (invertedScaleFactor * leftPosition);
        int scaledTopPosition = (int) (invertedScaleFactor * topPosition);
        int scaledWidth = (int) (invertedScaleFactor * width);
        int scaledHeight = (int) (invertedScaleFactor * height);

        Graphics2D graph = image.createGraphics();
        graph.setColor(Color.WHITE);
//        graph.fill(new Rectangle(scaledLeftPosition, scaledTopPosition, scaledWidth, scaledHeight));
        graph.draw(new Rectangle(scaledLeftPosition, scaledTopPosition, scaledWidth, scaledHeight));

        if (text != null && !"".equals(text)) {
            graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            Font font = new Font("Serif", Font.PLAIN, 12);
            graph.setFont(font);
            graph.drawString(text,
                    scaledLeftPosition + width / 2,
                    scaledTopPosition + height / 2);
        }
        graph.dispose();
    }
}
