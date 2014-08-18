package pl.info.rkluszczynski.image.standalone.config;

/**
 * Created by Rafal on 2014-05-29.
 */
public interface ApplicationConstants {

    String COMPARE_APPLICATION_BASE_PACKAGE = "pl.info.rkluszczynski.image.standalone";

    //    String RESOURCE_DIRECTORY = "detection-application/src/main/resources/";
    String RESOURCE_DIRECTORY = "src/main/resources/";

    String BASE_SET_FILES = RESOURCE_DIRECTORY + "image-data/base-set";
    String COMPARE_SET_FILES = RESOURCE_DIRECTORY + "image-data/detect-set";
    String STATISTICS_DIR = RESOURCE_DIRECTORY + "statistics";

}
