package pl.info.rkluszczynski.image.standalone.exception;

public class ImageDetectionException extends Exception {

    public ImageDetectionException(String message) {
        super(message);
    }

    public ImageDetectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
