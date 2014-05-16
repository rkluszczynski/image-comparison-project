package pl.info.rkluszczynski.test.opencv;

/**
 * @author Rafal
 */
public interface AppConstants {

    String IMAGE_RELATIVE_DIR_PATH = "testing-opencv/src/test/resources/";

    String OPENCV_DLL_PATH =
//        "D:/FRAMEWORKS/opencv/build/java/opencv_java248.dll";
            System.getProperty("user.dir") + "/testing-opencv/lib/" + System.getProperty("os.arch") + "/opencv_java248.dll";

}
