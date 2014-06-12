package pl.info.rkluszczynski.image.dsp;

/*File ImgMod34a.java
Copyright 2006, R.G.Baldwin

This program is a modification of ImgMod34.  The purpose of
this program is to compute and to display the wave-number
spectrum of an image using a Discrete Cosine Transform.

This program performs a forward DCT on each color plane
belonging to an image producing a wave-number spectrum that
describes each color plane in the image.

Then it converts the wave-number spectrum to decibels and
normalizes the result to cover the range from 0 to 255.
This makes it suitable for being displayed as an image.

Then it returns the wave-number spectrum for each color
plane in an image format.

When displayed as an image, the result is the composite of
the normalized wave number spectra of all three color
planes.

The capability is provided to enable statements that will
effectively eliminate one, two, or all three of the color
planes from the computation.  This requires modification
of the source code and recompilation of the program.

The class is designed to be driven by the class named
ImgMod02a.

Enter the following at the command line to run this
program:

java ImgMod02a ImgMod34a ImageFileName

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
ImgMod34a.class
ForwardDCT01.class

Tested using J2SE 5.0 and WinXP.  J2SE 5.0 or later is
required due to the use of static imports.
**********************************************************/

import static java.lang.Math.log10;

class ImgMod34a implements ImgIntfc02 {

    //Note that many of the comments in this source code are
    // left over from the class named ImgMod34, which was the
    // class from which this class was created.

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
    static double[] extractRow(double[][] colorPlane, int row) {
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
    static void insertRow(double[][] colorPlane,
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
    static double[] extractCol(double[][] colorPlane, int col) {
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
    static void insertCol(double[][] colorPlane,
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
    public static double[][] extractPlane(
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
    public static void insertPlane(
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
    static double[][][] copyToDouble(int[][][] threeDPix) {
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
    static int[][][] copyToInt(double[][][] threeDPixDouble) {
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

    //This method processes a color plane received as an
    // incoming parameter.  First it performs a 2D-DCT on
    // the color plane producing spectral results.  Then it
    // normalizes the spectral values in the plane to make
    // them compatible with being displayed as an image.  In
    // so doing, it converts the spectral data to decibels in
    // order to preserve the plotting dynamic range.

    static void processPlane(double[][] colorPlane) {

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

        //At this point, the image has been transformed from
        // image or space data to spectral data in both
        // dimensions.

        //Normalize the spectral values to the range 0 - 255.
        normalize(colorPlane);
    }//end processPlane

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
    //-----------------------------------------------------//

}//end class ImgMod34a
