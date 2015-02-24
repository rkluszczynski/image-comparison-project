package pl.info.rkluszczynski.image.standalone.exception;

public class StandaloneApplicationException extends Exception {

    public StandaloneApplicationException(String message) {
        super(message);
    }

    public StandaloneApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
