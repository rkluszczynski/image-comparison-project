package pl.info.rkluszczynski.test.opencv;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

import java.io.IOException;

import static pl.info.rkluszczynski.test.opencv.AppConstants.OPENCV_DLL_PATH;

public class TestOpenCvApp {

    public static void main(String[] args) throws IOException {
        System.out.println(System.getProperty("user.dir"));
        System.out.println(System.getProperty("os.arch"));
        System.out.println(Core.NATIVE_LIBRARY_NAME);

//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.load(OPENCV_DLL_PATH);

        VideoCapture camera = new VideoCapture(0);
        if (!camera.isOpened()) {
            System.out.println("Error");
        } else {
            Mat frame = new Mat();
            while (true) {
                if (camera.read(frame)) {
                    System.out.println("Frame Obtained");
                    System.out.println("Captured Frame Width " +
                            frame.width() + " Height " + frame.height());
                    Highgui.imwrite("testing-opencv/tmp-result-camera.jpg", frame);
                    System.out.println("OK");
                    break;
                }
            }
            camera.release();
        }
    }
}
