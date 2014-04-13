package pl.info.rkluszczynski.test.opencv;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static pl.info.rkluszczynski.test.opencv.AppConstants.IMAGE_RELATIVE_DIR_PATH;
import static pl.info.rkluszczynski.test.opencv.AppConstants.OPENCV_DLL_PATH;

public class TestTemplateMatchApp {

    private static String inFile = IMAGE_RELATIVE_DIR_PATH + "image.png";
    private static String templateFile = IMAGE_RELATIVE_DIR_PATH + "template.png";

    private static int match_method = Imgproc.TM_CCOEFF;
    private static String outFile = "testing-opencv/tmp-result-match.png";

    public static void main(String[] args) throws IOException {
        System.load(OPENCV_DLL_PATH);

        Mat img = Highgui.imread(inFile);
        Mat templ = Highgui.imread(templateFile);

        // / Create the result matrix
        int result_cols = img.cols() - templ.cols() + 1;
        int result_rows = img.rows() - templ.rows() + 1;
        Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);

        // / Do the Matching and Normalize
        Imgproc.matchTemplate(img, templ, result, match_method);
        Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new Mat());

        // / Localizing the best match with minMaxLoc
        Core.MinMaxLocResult mmr = Core.minMaxLoc(result);

        Point matchLoc;
        if (match_method == Imgproc.TM_SQDIFF || match_method == Imgproc.TM_SQDIFF_NORMED) {
            matchLoc = mmr.minLoc;
        } else {
            matchLoc = mmr.maxLoc;
        }

        // / Show me what you got
        Core.rectangle(img, matchLoc, new Point(matchLoc.x + templ.cols(),
                matchLoc.y + templ.rows()), new Scalar(0, 255, 0));

        // Save the visualized detection.
        System.out.println("Writing " + outFile);
        Highgui.imwrite(outFile, img);
    }


    BufferedImage convertMatToBufferedImage(Mat image) throws IOException {
        MatOfByte matOfByte = new MatOfByte();
        Highgui.imencode(".jpg", image, matOfByte);
        byte[] bytes = matOfByte.toArray();
        InputStream inputStream = new ByteArrayInputStream(bytes);
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        return bufferedImage;
    }

    Mat convertBufferedImageToMat(BufferedImage image) {
        int rows = image.getWidth();
        int cols = image.getHeight();
        int type = CvType.CV_16UC1;
        Mat newMat = new Mat(rows, cols, type);

        for (int r = 0; r < rows; ++r) {
            for (int c = 0; c < cols; ++c) {
                newMat.put(r, c, image.getRGB(r, c));
            }
        }
        return newMat;
    }
}
