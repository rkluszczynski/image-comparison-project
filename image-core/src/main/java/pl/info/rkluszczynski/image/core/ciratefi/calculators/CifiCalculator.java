package pl.info.rkluszczynski.image.core.ciratefi.calculators;

import pl.info.rkluszczynski.image.core.compare.SampleCorrelation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CifiCalculator {
    public static void cifi() {
    }

    public static void cifi(GsImageWrapper a, GsImageWrapper[] t) {
        double[] radii = {1.5, 3, 4.5, 6, 7.5, 9};

        double[][][] ca = new double[a.getWidth()][a.getHeight()][radii.length];
        for (int iw = 0; iw < a.getWidth(); ++iw) {
            for (int ih = 0; ih < a.getHeight(); ++ih) {
                for (int k = 0; k < radii.length; ++k) {
                    ca[iw][ih][k] = getRadiusCifiValue(a, radii[k], iw, ih);
                }
            }
        }

        double[][] ct = new double[t.length][radii.length];
        for (int i = 0; i < t.length; ++i) {
            int wCenter = t[i].getWidth() / 2;
            int hCenter = t[i].getHeight() / 2;
            for (int k = 0; k < radii.length; ++k) {
                ct[i][k] = getRadiusCifiValue(t[i], radii[k], wCenter, hCenter);
            }
        }

        int[][] scales = new int[a.getWidth()][a.getHeight()];
        double threshold1 = 0.95;

        for (int iw = 0; iw < a.getWidth(); ++iw) {
            for (int ih = 0; ih < a.getHeight(); ++ih) {
                double cisCorr = SampleCorrelation.calculate(ct[0], ca[iw][ih]);
                scales[iw][ih] = (cisCorr > threshold1) ? 0 : -1;

                for (int i = 1; i < t.length; ++i) {
                    double corr = SampleCorrelation.calculate(ct[i], ca[iw][ih]);
                    if (corr > cisCorr && corr > threshold1) {
                        cisCorr = corr;
                        scales[iw][ih] = i;
                    }
                }
            }
        }

        System.out.println("THRESHOLD = " + threshold1);
        for (int iw = 0; iw < a.getWidth(); ++iw) {
            for (int ih = 0; ih < a.getHeight(); ++ih) {
                if (scales[iw][ih] >= 0) {
                    System.out.println("found at (" + iw + ", " + ih + ") with scale " + scales[iw][ih]);
                }
            }
        }

        // ==============================================================
        double[] angles = new double[16];
        for (int dt = 0; dt < angles.length; ++dt) {
            angles[dt] = dt * (2. * Math.PI / angles.length);
        }

        double[][][] ra = new double[a.getWidth()][a.getHeight()][radii.length];
        for (int iw = 0; iw < a.getWidth(); ++iw) {
            for (int ih = 0; ih < a.getHeight(); ++ih) {
                for (int j = 0; j < angles.length; ++j) {
                    // FIXME: add scale ratio
                    double lambda = radii[radii.length - 1];

                    double sum = 0.;
                    for (int dl = 0; dl < lambda; ++dl) {
                        int x = (int) (iw + dl * Math.cos(angles[j]));
                        int y = (int) (ih + dl * Math.sin(angles[j]));

                        if (x < 0) {
                            x = 0;
                        }
                        if (x >= a.getWidth()) {
                            x = a.getWidth() - 1;
                        }
                        if (y < 0) {
                            y = 0;
                        }
                        if (y >= a.getHeight()) {
                            y = a.getHeight() - 1;
                        }
                        sum += a.getValue(x, y);
                    }
                    ra[iw][ih][j] = sum / lambda;
                }
            }
        }


    }

    private static double getRadiusCifiValue(GsImageWrapper a, double radius, int iw, int ih) {
        double Pk = Math.round(2. * Math.PI * radius);
        double sum = 0;
        for (int theta = 0; theta < (int) Pk; ++theta) {
            int x = (int) (iw + radius * Math.cos((2. * Math.PI * theta) / Pk));
            int y = (int) (ih + radius * Math.sin((2. * Math.PI * theta) / Pk));

            if (x < 0) {
                x = 0;
            }
            if (x >= a.getWidth()) {
                x = a.getWidth() - 1;
            }
            if (y < 0) {
                y = 0;
            }
            if (y >= a.getHeight()) {
                y = a.getHeight() - 1;
            }

            sum += a.getValue(x, y);
        }
        return sum / Pk;
    }

    public static void main(String[] args) throws IOException {
        String imgPath = "image-core/src/test/resources/image-data/";

        BufferedImage imageA = ImageIO.read(new File(imgPath + "zubr-image.png"));
        GsImageWrapper A = new GsImageWrapper(imageA);

        BufferedImage imageT = ImageIO.read(new File(imgPath + "zubr-pattern1-poster.png"));
        GsImageWrapper T = new GsImageWrapper(imageA);

        CifiCalculator.cifi(A, new GsImageWrapper[]{T});

        System.out.println("DONE");
    }
}
