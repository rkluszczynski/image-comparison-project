package pl.info.rkluszczynski.image.core.dsp;

/*File ImgMod34.java
Copyright 2006, R.G.Baldwin

This program performs a forward DCT on an image followed by
an inverse DCT on the spectral planes produced by the
forward DCT.

To partially simulate the behavior of JPEG, the spectral
results produced by the forward transform are requantized
so that the data could be stored in an eleven-bit twos
complement format (-1024 to +1023).  However, the data is
never actually stored in an integer format.  However, this
process should create the same requantization noise that
would be experienced if the data were actually stored in
eleven bits.

Other than the requantization to eleven bits mentioned
above, nothing is done to the spectral planes following the
forward DCT and before the inverse DCT.  However,
additional processing, such as high-frequency
requantization and entropy compression, followed by
decompression could be inserted at that point in the
program for demonstration purposes.

This program runs significantly slower than ImgMod35,
which sub-divides the image into 8x8-pixel subplanes and
processes the subplanes separately.

The class is designed to be driven by the class named
ImgMod02a.

Enter the following at the command line to run this
program:

java ImgMod02a ImgMod34 ImageFileName

where ImageFileName is the name of a .gif or .jpg file,
including the extension.


When you click the Replot button, the process will be
repeated and the results will be re-displayed.  Because
there is no opportunity for user input after the program is
started, the Replot button is of little value to this
program.

This program requires access to the following class files
plus some inner classes that are defined inside the
following classes:

ImgIntfc02.class
ImgMod02a.class
ImgMod34.class
InverseDCT01.class
ForwardDCT01.class

Tested using J2SE 5.0 and WinXP.  J2SE 5.0 or later is
required due to the use of static imports.
**********************************************************/

import static java.lang.Math.round;

class ImgMod34 {//implements ImgIntfc02{

    //This method is required by ImgIntfc02.  It is called at
    // the beginning of the run and each time thereafter that
    // the user clicks the Replot button on the Frame
    // contaning the images.
    public int[][][] processImg(int[][][] threeDPix,
                                int imgRows,
                                int imgCols) {

        //Create an empty output array of the same size as the
        // incoming array.
        int[][][] output = new int[imgRows][imgCols][4];

        //Make a working copy of the 3D pixel array as type
        // double to avoid making permanent changes to the
        // original image data.  Also, all processing will be
        // performed as type double.
        double[][][] working3D = copyToDouble(threeDPix);

        //The following code can be enabled to set any of the
        // three colors to black, thus removing them from the
        // output.
        for (int row = 0; row < imgRows; row++) {
            for (int col = 0; col < imgCols; col++) {
//        working3D[row][col][1] = 0;
//        working3D[row][col][2] = 0;
//        working3D[row][col][3] = 0;
            }//end inner loop
        }//end outer loop

        //Extract and process the red plane
        double[][] redPlane = extractPlane(working3D, 1);
        processPlane(redPlane);
        //Insert the plane back into the 3D array
        insertPlane(working3D, redPlane, 1);

        //Extract and process the green plane
        double[][] greenPlane = extractPlane(working3D, 2);
        processPlane(greenPlane);
        //Insert the plane back into the 3D array
        insertPlane(working3D, greenPlane, 2);

        //Extract and process the blue plane
        double[][] bluePlane = extractPlane(working3D, 3);
        processPlane(bluePlane);
        //Insert the plane back into the 3D array
        insertPlane(working3D, bluePlane, 3);

        //Convert the image color planes to type int and return
        // the array of pixel data to the calling method.
        output = copyToInt(working3D);
        //Return a reference to the output array.
        return output;

    }//end processImg method
    //-----------------------------------------------------//

    //The purpose of this method is to extract a specified
    // row from a double 2D plane and to return it as a one-
    // dimensional array of type double.
    double[] extractRow(double[][] colorPlane, int row) {
        int numCols = colorPlane[0].length;
        double[] output = new double[numCols];
        for (int col = 0; col < numCols; col++) {
            output[col] = colorPlane[row][col];
        }//end outer loop
        return output;
    }//end extractRow
    //-----------------------------------------------------//

    //The purpose of this method is to insert a specified
    // row of double data into a double 2D plane.
    void insertRow(double[][] colorPlane,
                   double[] theRow,
                   int row) {
        int numCols = colorPlane[0].length;
        double[] output = new double[numCols];
        for (int col = 0; col < numCols; col++) {
            colorPlane[row][col] = theRow[col];
        }//end outer loop
    }//end insertRow
    //-----------------------------------------------------//

    //The purpose of this method is to extract a specified
    // col from a double 2D plane and to return it as a one-
    // dimensional array of type double.
    double[] extractCol(double[][] colorPlane, int col) {
        int numRows = colorPlane.length;
        double[] output = new double[numRows];
        for (int row = 0; row < numRows; row++) {
            output[row] = colorPlane[row][col];
        }//end outer loop
        return output;
    }//end extractCol
    //-----------------------------------------------------//

    //The purpose of this method is to insert a specified
    // col of double data into a double 2D color plane.
    void insertCol(double[][] colorPlane,
                   double[] theCol,
                   int col) {
        int numRows = colorPlane.length;
        double[] output = new double[numRows];
        for (int row = 0; row < numRows; row++) {
            colorPlane[row][col] = theCol[row];
        }//end outer loop
    }//end insertCol
    //-----------------------------------------------------//

    //The purpose of this method is to extract a color plane
    // from the double version of an image and to return it
    // as a 2D array of type double.
    public double[][] extractPlane(
            double[][][] threeDPixDouble,
            int plane) {

        int numImgRows = threeDPixDouble.length;
        int numImgCols = threeDPixDouble[0].length;

        //Create an empty output array of the same
        // size as a single plane in the incoming array of
        // pixels.
        double[][] output = new double[numImgRows][numImgCols];

        //Copy the values from the specified plane to the
        // double array.
        for (int row = 0; row < numImgRows; row++) {
            for (int col = 0; col < numImgCols; col++) {
                output[row][col] =
                        threeDPixDouble[row][col][plane];
            }//end loop on col
        }//end loop on row
        return output;
    }//end extractPlane
    //-----------------------------------------------------//

    //The purpose of this method is to insert a double 2D
    // plane into the double 3D array that represents an
    // image.  This method also trims off any extra rows and
    // columns in the double 2D plane.
    public void insertPlane(
            double[][][] threeDPixDouble,
            double[][] colorPlane,
            int plane) {

        int numImgRows = threeDPixDouble.length;
        int numImgCols = threeDPixDouble[0].length;

        //Copy the values from the incoming color plane to the
        // specified plane in the 3D array.
        for (int row = 0; row < numImgRows; row++) {
            for (int col = 0; col < numImgCols; col++) {
                threeDPixDouble[row][col][plane] =
                        colorPlane[row][col];
            }//end loop on col
        }//end loop on row
    }//end insertPlane
    //-----------------------------------------------------//

    //This method copies an int version of a 3D pixel array
    // to an new pixel array of type double.
    double[][][] copyToDouble(int[][][] threeDPix) {
        int imgRows = threeDPix.length;
        int imgCols = threeDPix[0].length;

        double[][][] new3D = new double[imgRows][imgCols][4];
        for (int row = 0; row < imgRows; row++) {
            for (int col = 0; col < imgCols; col++) {
                new3D[row][col][0] = threeDPix[row][col][0];
                new3D[row][col][1] = threeDPix[row][col][1];
                new3D[row][col][2] = threeDPix[row][col][2];
                new3D[row][col][3] = threeDPix[row][col][3];
            }//end inner loop
        }//end outer loop
        return new3D;
    }//end copyToDouble
    //-----------------------------------------------------//

    //This method copies double version of a 3D pixel array
    // to a new pixel array of type int.
    int[][][] copyToInt(double[][][] threeDPixDouble) {
        int imgRows = threeDPixDouble.length;
        int imgCols = threeDPixDouble[0].length;

        int[][][] new3D = new int[imgRows][imgCols][4];
        for (int row = 0; row < imgRows; row++) {
            for (int col = 0; col < imgCols; col++) {
                new3D[row][col][0] =
                        (int) threeDPixDouble[row][col][0];
                new3D[row][col][1] =
                        (int) threeDPixDouble[row][col][1];
                new3D[row][col][2] =
                        (int) threeDPixDouble[row][col][2];
                new3D[row][col][3] =
                        (int) threeDPixDouble[row][col][3];
            }//end inner loop
        }//end outer loop
        return new3D;
    }//end copyToInt
    //-----------------------------------------------------//

    //The purpose of this method is to clip all negative
    // color values in a double color plane to a value of 0.
    void clipToZero(double[][] colorPlane) {
        int numImgRows = colorPlane.length;
        int numImgCols = colorPlane[0].length;
        //Do the clip
        for (int row = 0; row < numImgRows; row++) {
            for (int col = 0; col < numImgCols; col++) {
                if (colorPlane[row][col] < 0) {
                    colorPlane[row][col] = 0;
                }//end if
            }//end inner loop
        }//end outer loop
    }//end clipToZero

    //-----------------------------------------------------//
    //The purpose of this method is to clip all color values
    // in a double color plane that are greater than 255 to
    // a value of 255.
    void clipTo255(double[][] colorPlane) {
        int numImgRows = colorPlane.length;
        int numImgCols = colorPlane[0].length;
        //Do the clip
        for (int row = 0; row < numImgRows; row++) {
            for (int col = 0; col < numImgCols; col++) {
                if (colorPlane[row][col] > 255) {
                    colorPlane[row][col] = 255;
                }//end if
            }//end inner loop
        }//end outer loop
    }//end clipTo255
    //-----------------------------------------------------//

    //This method processes a color plane received as an
    // incoming parameter.  First it performs a 2D-DCT on
    // the color plane producing spectral results.  Then it
    // performs an inverse DCT on the spectral plane
    // producing an image color plane.
    void processPlane(double[][] colorPlane) {

        int imgRows = colorPlane.length;
        int imgCols = colorPlane[0].length;

        //Extract each row from the color plane and perform a
        // forward DCT on the row.  Then insert it back into
        // the color plane.
        for (int row = 0; row < imgRows; row++) {
            double[] theRow = extractRow(colorPlane, row);

            double[] theXform = DCT.forward(theRow);

            //Insert the transformed row into the color plane.
            // The row now contains spectral data.
            insertRow(colorPlane, theXform, row);
        }//end for loop

        //Extract each col from the color plane and perform a
        // forward DCT on the column.  Then insert it back into
        // the color plane.
        for (int col = 0; col < imgCols; col++) {
            double[] theCol = extractCol(colorPlane, col);

            double[] theXform = DCT.forward(theCol);

            insertCol(colorPlane, theXform, col);
        }//end for loop

        //To approximate the behavior of JPEG, I need to
        // re-quantize the data such that it would fit into
        // eleven bits (-1024 to +1023) as an integer type.
        // This is probably not exactly how it is done in
        // JPEG, but hopefully it is a good approximation.
        // I am assuming that the maximum value for this
        // plane can be saved along with the spectral data
        // until time comes to perform the inverse transform.
        //Note that in this program, even though the spectral
        // data is requantized so that it will fit into
        // an eleven-bit integer format, the data is never
        // actually stored in an eleven-bit integer format.
        // Rather, immediately after being requantized, each
        // value is converted back to type double for storage
        // in the array of type double[][].

        //Get, save, and display the max value.
        double max = getMax(colorPlane);
        System.out.println(max);
        requanToElevenBits(colorPlane, max / 1023);
        //Display requantized max value. (Should be 1023.)
        System.out.println(getMax(colorPlane));

        //At this point, the image has been transformed from
        // image or space data to spectral data in both
        // dimensions. In addition, the spectral data has been
        // requantized so that could be converted to an
        // eleven-bit integer format and stored in that format
        // if there were a need to do so.

        //Now convert the spectral data back into image data.

        //First restore the magnitude of the spectral data
        // that has been requantized to the range -1024 to
        // +1023.  This is necessary so that the relative
        // magnitudes among the spectra for the three color
        // planes will be correct.
        //Note that the spectral data may have been corrupted
        // by quantization noise as a result of having
        // been requantized.
        restoreSpectralMagnitude(colorPlane, max / 1023);
        //Display restored max value.
        System.out.println(getMax(colorPlane));

        //Extract each col from the spectral plane and perform
        // an inverse DCT on the column.  Then insert it back
        // into the color plane.
        for (int col = 0; col < imgCols; col++) {
            double[] theXform = extractCol(colorPlane, col);

            double[] theCol = DCT.inverse(theXform);

            //Insert it back into the color plane.
            insertCol(colorPlane, theCol, col);
        }//end for loop

        //Extract each row from the plane and perform an
        // inverse DCT on the row. Then insert it back into the
        // color plane.
        for (int row = 0; row < imgRows; row++) {
            double[] theXform = extractRow(colorPlane, row);

            double[] theRow = DCT.inverse(theXform);

            //Insert it back in
            insertRow(colorPlane, theRow, row);
        }//end for loop
        //End inverse transform code

        //At this point, the spectral data has been converted
        // back into image color data.  Ultimately it will be
        // necessary to convert it to 8-bit unsigned pixel
        // color format in order to display it as an image.
        //  Clip to zero and 255.
        clipToZero(colorPlane);
        clipTo255(colorPlane);

    }//end processPlane
    //-----------------------------------------------------//

    //Purpose: to find and return the maximum value
    double getMax(double[][] plane) {
        int rows = plane.length;
        int cols = plane[0].length;
        double max = Double.MIN_VALUE;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                double value = plane[row][col];
                if (value < 0) {
                    value = -value;
                }//end if
                if (value > max) {
                    max = value;
                }//end if
            }//end inner loop
        }//end outer loop
        return max;
    }//end getMax
    //-----------------------------------------------------//

    //Purpose:  To requantize the spectral data such that it
    // would fit into eleven bits (-1024 to 1023).  Note
    // that even though the data is rounded to type int in
    // this method, it is immediately converted back to type
    // double when it is stored in the array referred to by
    // plane.  Thus, it is never actually stored in an
    // integer format.
    void requanToElevenBits(double[][] plane, double divisor) {
        int rows = plane.length;
        int cols = plane[0].length;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                plane[row][col] = round(plane[row][col] / divisor);
            }//end inner loop
        }//end outer loop
    }//end requanToElevenBits
    //-----------------------------------------------------//

    //Purpose:  To restore the magnitude of spectral data
    // that has been requantized to the range from -1024 to
    // +1023.  This is necessary so that the relative
    // magnitude among the spectra for the three color planes
    // will be correct.
    void restoreSpectralMagnitude(
            double[][] plane, double factor) {
        int rows = plane.length;
        int cols = plane[0].length;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                plane[row][col] = factor * plane[row][col];
            }//end inner loop
        }//end outer loop
    }//end restoreSpectralMagnitude
    //-----------------------------------------------------//
}//end class ImgMod34