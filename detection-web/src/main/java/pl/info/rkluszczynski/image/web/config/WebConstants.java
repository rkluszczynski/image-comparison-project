package pl.info.rkluszczynski.image.web.config;

public interface WebConstants {

    String WEB_APPLICATION_BASE_PACKAGE = "pl.info.rkluszczynski.image.web";

    String USER_SESSION_ATTRIBUTE_NAME__IMAGE_DATA = "userSessionImageData";

    String STATUS_CONTEXT_PATH__ROOT = "/status";
    String DETECT_CONTEXT_PATH__ROOT = "/detect";

    String DETECT_CONTEXT_PATH__USER_SESSION_INPUT_IMAGE = "userSessionInputImage";
    String DETECT_CONTEXT_PATH__USER_SESSION_TEMPLATE_IMAGE = "userSessionTemplateImage";
    String DETECT_CONTEXT_PATH__USER_SESSION_RESULT_IMAGE = "userSessionResultImage";

}
