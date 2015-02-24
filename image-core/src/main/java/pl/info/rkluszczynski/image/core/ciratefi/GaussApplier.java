package pl.info.rkluszczynski.image.core.ciratefi;

import java.awt.image.BufferedImage;

/**
 * Created by Rafal on 2014-04-18.
 */
public class GaussApplier {
    public static void gauss(BufferedImage analyzeImage, BufferedImage queryImage) {
        System.out.println("<<Gauss>>");

//            if (adesv>0.0) ga=ConvGaussHV(IMGFLT(an),adesv); else ga=an;
        if (CiratefiProperties.adesv > 0.0) {
            applyConvGaussHV(analyzeImage, CiratefiProperties.adesv);
        }

//            if (gaussa!="nul") imp(ga,gaussa);

//            if (qdesv>0.0) gq=ConvGaussHV(IMGFLT(qu),qdesv); else gq=qu;
        if (CiratefiProperties.qdesv > 0.0) {
            applyConvGaussHV(queryImage, CiratefiProperties.qdesv);
        }

//            if (gaussq!="nul") imp(gq,gaussq);

        // Wczytanie pliku z wynikiem z gaussa:

//            if (actualq!="" && actualq!="nul") {
//                IMGGRY qupintado=pintapreto(qu);
//                imp(qupintado,actualq);
//            }

    }

    private static void applyConvGaussHV(BufferedImage image, double desv) {
        // TODO: apply OpenCV->ConvGaussHV(image, desv)
    }
}
