package pl.info.rkluszczynski.image.ciratefi;

import java.util.Properties;

/**
 * Created by Rafal on 2014-04-18.
 */
public class CiratefiProperties extends Properties {

// v 1.05 for one query instance
// The input file names are not specified. They are assumed to be the same as the output file names of the previous stage.
// If a verbose file name is "nul", the verbose log file is not generated.
// If names of files analyze, query and circ_out are specified in the command line,
// they overrides the specifications here

    // Global parameters
    public static double adesv = 1.0; // Standard deviation of the gaussian filter applied to a.tga. 0.0 means not applying the filter
    public static double qdesv = 1.0; // Standard deviation of the gaussian filter applied to q.tga. 0.0 means not applying the filter

    public static int nesc = 5;      // Number of scales. The more scales, the slower but the more accurate
    public static double escinic = 0.5; // Initial scale (scale 0)
    public static double escfim = 1.0;  // Final scale (scale nesc). Usually, escfim=2*escinic

    public static int nang = 36; // Number of angles

    public static boolean otimiza1 = true; // false: uses t1. true: uses (qtd_cand_1f or pct_cand_1f) and dist_pixel_1f
    public static double t1 = 0.95;       // threshold for the first grade candidates. Not used if otimiza1=true.
    public static double qtd_cand_1f = 0;  // Number of the first grade candidates. 0 indicates to use pct_cand_1f instead
    public static double pct_cand_1f = 3.0;        // Percentage of the first grade candidates in relation to the total number of pixels of A
    public static double dist_pixel_1f = 0;         // Minimal distance between two first grade candidate pixels

    public static boolean otimiza2 = true;  // false: uses t2. true: uses (qtd_cand_2f or pct_cand_2f) and dist_pixel_2f
    public static double t2 = 0.80;        // threshold for the second grade candidates. Not used if otimiza2=true
    public static double qtd_cand_2f = 0;          // Number of second grade candidates. 0 indicates to use pct_cand_2f
    public static double pct_cand_2f = 1.5;          // Percentage of second grade candidates in relation to the total number of pixels of A
    public static double dist_pixel_2f = 0;         // Minimal distance between two second grade candidates

    public static boolean otimiza3 = true;  // false: uses t3. true: uses qtd_cand_3f e dist_pixel_3f
    public static double t3 = 0.90;  // threshold for the third grade candidates (matches). Not used if otimiza3=true.
    public static double qtd_cand_3f = 1;    // The number of objects to be detected
    public static double dist_pixel_3f = 0;  // The minimal distance between two matching pixels

    public static double ssalpha = 0.01; // Weight of brightness.
    public static double ssbeta = 0.01;  // Weight of contrast. Do not set to zero.
    public static double ssgama = 0.98;  // Weight of "structure" or correlation

    public static boolean absoluto = false; // Absolute=true: Negative instances can match

    public static double ncirc = 16; // Number of circles. The more circles, the slower but the more accurate.
    public static double rinic = 0; // Initial radius in pixels
    public static double rfim = -1; // Final radius in pixels
    // -1 means that this parameter is chosen automatically according to Q and escfim.

    public static double tol = 1; // Tolerance in the best scale and radius. Used in Tefi.

    // Gaussian filtering
    public static boolean gauss_exec = true; // true=executes gaussian filterings

    public static boolean cissa_exec = true;         // true=executes circular sampling in ga.tga

    public static boolean cissq_exec = true;         // true=executes circular sampling in gq.tga

    public static boolean cifi_exec = true;          // true=executes Cifi

    public static boolean rassq_exec = true;         // true=executes radial sampling in gq.tga

    public static boolean rafi_exec = true;        // true=executes Rafi

    public static boolean tefi_exec = true;        // true=executes Tefi

    public static boolean circ_exec = true;        // true=highlights the matchings with a circle and "watch-hand"

    public static boolean txt_exec = true;          // true=outputs a text file

}
