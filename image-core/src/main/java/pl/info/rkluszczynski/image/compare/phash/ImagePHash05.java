package pl.info.rkluszczynski.image.compare.phash;

import com.google.common.collect.Lists;
import org.imgscalr.Scalr;
import pl.info.rkluszczynski.image.dsp.DCT;
import pl.info.rkluszczynski.image.utils.ImageHelper;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.log10;

public class ImagePHash05 {

    private int smallImageSize = 32;
    private int reducedDctSmallerSize = 8;

    protected static void processImagePlane(double[][] colorPlane) {
        int imageWidth = colorPlane.length;
        int imageHeight = colorPlane[0].length;

        for (int row = 0; row < imageWidth; ++row) {
            double[] theRow = extractPlaneRow(colorPlane, row);
            insertPlaneRow(colorPlane, DCT.forward(theRow), row);
        }

        for (int col = 0; col < imageHeight; ++col) {
            double[] theColumn = extractPlaneColumn(colorPlane, col);
            insertPlaneColumn(colorPlane, DCT.forward(theColumn), col);
        }

        //Normalize the spectral values to the range 0 - 255.
        normalize(colorPlane);
    }

    private static void insertPlaneColumn(double[][] colorPlane, double[] dctForm, int col) {
        int numRows = colorPlane.length;
        for (int row = 0; row < numRows; row++) {
            colorPlane[row][col] = dctForm[row];
        }
    }

    private static double[] extractPlaneColumn(double[][] colorPlane, int col) {
        int numRows = colorPlane.length;
        double[] output = new double[numRows];
        for (int row = 0; row < numRows; ++row) {
            output[row] = colorPlane[row][col];
        }
        return output;
    }

    private static void insertPlaneRow(double[][] colorPlane, double[] dctForm, int row) {
        int numCols = colorPlane[0].length;
        for (int col = 0; col < numCols; ++col) {
            colorPlane[row][col] = dctForm[col];
        }
    }

    private static double[] extractPlaneRow(double[][] colorPlane, int row) {
        int numCols = colorPlane[0].length;
        double[] output = new double[numCols];
        for (int col = 0; col < numCols; ++col) {
            output[col] = colorPlane[row][col];
        }
        return output;
    }

    //-----------------------------------------------------//
    //Normalizes the data in a 2D double plane to make it
    // compatible with being displayed as an image plane.
    //First all negative values are converted to positive
    // values.
    //Then all values are converted to log base 10 to
    // preserve the dynamic range of the plotting system.
    // All negative values are set to 0 at this point.
    //Then all values that are below X-percent of the maximum
    // value are set to X-percent of the maximum value
    // producing a floor for the values.
    //Then all values are biased so that the minimum value
    // (the floor) becomes 0.
    //Then all values are scaled so that the maximum value
    // becomes 255.
    static void normalize(double[][] plane) {
        int rows = plane.length;
        int cols = plane[0].length;

        //Begin by converting all negative values to positive
        // values.  This is equivalent to the computation of
        // the magnitude for purely real data.
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (plane[row][col] < 0) {
                    plane[row][col] = -plane[row][col];
                }//end if
            }//end inner loop
        }//end outer loop

        //Convert the values to log base 10 to preserve the
        // dynamic range of the plotting system.  Set negative
        // values to 0.

        //First eliminate or change any values that are
        // incompatible with log10 method.
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (plane[row][col] == 0.0) {
                    plane[row][col] = 0.0000001;
                } else if (plane[row][col] == Double.NaN) {
                    plane[row][col] = 0.0000001;
                } else if (plane[row][col] ==
                        Double.POSITIVE_INFINITY) {
                    plane[row][col] = 9999999999.0;
                }//end else
            }//end inner loop
        }//end outer loop

        //Now convert the data to log base 10 setting all
        // negative results to 0.
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                plane[row][col] = log10(plane[row][col]);
                if (plane[row][col] < 0) {
                    plane[row][col] = 0;
                }//end if
            }//end inner loop
        }//end outer loop


        //Now set everything below X-percent of the maximum
        // value to X-percent of the maximum value where X is
        // determined by the value of scale.
        double scale = 1.0 / 7.0;
        //First find the maximum value.
        double max = Double.MIN_VALUE;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (plane[row][col] > max) {
                    max = plane[row][col];
                }//end if
            }//end inner loop
        }//end outer loop

        //Now set everything below X-percent of the maximum to
        // X-percent of the maximum value and slide
        // everything down to cause the new minimum to be
        // at 0.0
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (plane[row][col] < scale * max) {
                    plane[row][col] = scale * max;
                }//end if
                plane[row][col] -= scale * max;
            }//end inner loop
        }//end outer loop

        //Now scale the data so that the maximum value is 255.

        //First find the maximum value
        max = Double.MIN_VALUE;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (plane[row][col] > max) {
                    max = plane[row][col];
                }//end if
            }//end inner loop
        }//end outer loop
        //Now scale the data.
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                plane[row][col] = plane[row][col] * 255.0 / max;
            }//end inner loop
        }//end outer loop

    }//end normalize

    // Returns a 'binary string' (like. 001010111011100010) which is easy to do a hamming distance on.
    public String getGrayScaleHash(BufferedImage bufferedImage) {
        /* 1. Reduce smallImageSize to 32x32 */
        BufferedImage smallImage = Scalr.resize(bufferedImage,
                Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_EXACT, smallImageSize, smallImageSize);

		/* 2. Reduce color. */
        BufferedImage reducedImage = ImageHelper.convertImageToGrayScale(smallImage);

		/* 3. Compute the DCT. */
        double[][] imageColorPlane = new double[smallImageSize][smallImageSize];

        for (int iw = 0; iw < reducedImage.getWidth(); ++iw) {
            for (int ih = 0; ih < reducedImage.getHeight(); ++ih) {
                Color pixelColor = new Color(reducedImage.getRGB(iw, ih));
                imageColorPlane[iw][ih] = pixelColor.getGreen();
            }
        }
        return getColorPlaneHash(imageColorPlane);
    }

    public String[] getColorHashes(BufferedImage bufferedImage) {
        String[] result = new String[3];

        /* 1. Reduce smallImageSize to 32x32 */
        BufferedImage smallImage = Scalr.resize(bufferedImage,
                Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_EXACT, smallImageSize, smallImageSize);

		/* 2. Iterate over colors. */
        for (int rgb = 0; rgb < 3; ++rgb) {
            /* 3. Compute the DCT. */
            double[][] imageColorPlane = new double[smallImageSize][smallImageSize];

            for (int iw = 0; iw < smallImageSize; ++iw) {
                for (int ih = 0; ih < smallImageSize; ++ih) {
                    Color pixelColor = new Color(smallImage.getRGB(iw, ih));
                    int colorValue = -1;
                    switch (rgb) {
                        case 0:
                            colorValue = pixelColor.getRed();
                            break;
                        case 1:
                            colorValue = pixelColor.getGreen();
                            break;
                        case 2:
                            colorValue = pixelColor.getBlue();
                            break;
                        default:
                            throw new RuntimeException();
                    }
                    imageColorPlane[iw][ih] = colorValue;
                }
            }
            result[rgb] = getColorPlaneHash(imageColorPlane);
        }
        return result;
    }

    private String getColorPlaneHash(double[][] imageColorPlane) {
        processImagePlane(imageColorPlane);

		/* 4. Reduce the DCT. */
        double[][] reducedPlane = new double[reducedDctSmallerSize][reducedDctSmallerSize];
        for (int x = 0; x < reducedDctSmallerSize; x++) {
            for (int y = 0; y < reducedDctSmallerSize; y++) {
                reducedPlane[x][y] = imageColorPlane[x][y];
            }
        }

		/* 5. Compute the median/average  value. */
        ArrayList<Double> list = Lists.newArrayList();
        double sum = 0.;
        for (int x = 0; x < reducedDctSmallerSize; x++) {
            for (int y = 0; y < reducedDctSmallerSize; y++) {
                list.add(reducedPlane[x][y]);
                sum += reducedPlane[x][y];
            }
        }
        Collections.sort(list);
        double median = list.get(reducedDctSmallerSize * reducedDctSmallerSize / 2);
        median = sum / (reducedDctSmallerSize * reducedDctSmallerSize);

		/* 6. Further reduce the DCT. */
        String imagePHash = "";
        for (int x = 0; x < reducedDctSmallerSize; x++) {
            for (int y = 0; y < reducedDctSmallerSize; y++) {
//                if (x != 0 && y != 0)
                imagePHash += (reducedPlane[x][y] > median ? "1" : "0");
            }
        }
        return imagePHash;
    }
}

