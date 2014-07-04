package pl.info.rkluszczynski.image.engine.utils;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
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
        float borderThickness = 2;

        Stroke oldStroke = graph.getStroke();
        graph.setStroke(new BasicStroke(borderThickness));

//        graph.setColor(Color.WHITE);
        graph.setColor(new Color(240, 240, 0));

//        graph.fill(new Rectangle(scaledLeftPosition, scaledTopPosition, scaledWidth, scaledHeight));
        graph.draw(new Rectangle(scaledLeftPosition, scaledTopPosition, scaledWidth, scaledHeight));
        graph.setStroke(oldStroke);

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

    static
    public void makeBrighterRectangleOnImage(BufferedImage image, int leftPosition, int topPosition, int width, int height, double scaleFactor) {
        double invertedScaleFactor = 1. / scaleFactor;
        int scaledLeftPosition = (int) (invertedScaleFactor * leftPosition);
        int scaledTopPosition = (int) (invertedScaleFactor * topPosition);
        int scaledWidth = (int) (invertedScaleFactor * width);
        int scaledHeight = (int) (invertedScaleFactor * height);

        for (int iw = scaledLeftPosition; iw < scaledLeftPosition + scaledWidth; ++iw) {
            for (int ih = scaledTopPosition; ih < scaledTopPosition + scaledHeight; ++ih) {
                Color color = new Color(image.getRGB(iw, ih));
                image.setRGB(iw, ih,
                        new Color(
                                Math.min(color.getRed() + 64, 255),
                                Math.min(color.getGreen() + 64, 255),
                                Math.min(color.getBlue() + 64, 255)
                        ).getRGB()
                );
            }
        }

        Graphics2D graph = image.createGraphics();
        float borderThickness = 3;

        Stroke oldStroke = graph.getStroke();
        graph.setStroke(new BasicStroke(borderThickness));

//        graph.setColor(Color.BLACK);
        graph.setColor(new Color(0, 240, 0));
        Shape shape = new RoundRectangle2D.Double(
                scaledLeftPosition, scaledTopPosition,
                scaledWidth, scaledHeight,
                Math.min(scaledWidth / 3., 5.), Math.min(scaledHeight / 3., 5.));
        graph.draw(shape);

        graph.setStroke(oldStroke);
        graph.dispose();
    }

    static
    public BufferedImage createEmptyImage(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setColor(new Color(0, 0, 0, 0));
        g.fillRect(0, 0, width, height);
        g.dispose();
        return image;
    }
}
