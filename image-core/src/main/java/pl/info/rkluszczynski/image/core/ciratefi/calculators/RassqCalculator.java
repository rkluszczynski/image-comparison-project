package pl.info.rkluszczynski.image.core.ciratefi.calculators;

import pl.info.rkluszczynski.image.core.ciratefi.CiratefiProperties;

/**
 * Created by Rafal on 2014-04-18.
 */
public class RassqCalculator {
    public static void rassq() {
        System.out.println("<<Radial projections of Q>>");

        int nang = CiratefiProperties.nang;
//
//        IMGDBL m (1, nang, 0.0);
//        for (int c = 0; c < nang; c++)
//            m(0, c) = G2D(rsample(gq, gq.lc(), gq.cc(), dangrad * c, rqunit));
//        imp(m, rassq_out);
//
//        if (rassq_verbose != "nul") {
//            IMGCOR b = qu;
//            for (int c = 0; c < nang; c++) {
//                reta(b, b.lc(), b.cc(), b.lc() - arredonda(sin(dangrad * c) * rqunit), b.cc() + arredonda(cos(dangrad * c) * rqunit), COR(0, 0, 255));
//            }
//            imp(b, rassq_verbose);
//
//


        // Acrescentei depois, para imprimir projecoes radiais
    /*
    VETOR<double> f(nang);
    IMGBIN d(240,320,branco);
    for (int c=0; c<nang; c++) {
      f(c)=G2D(rsample(gq,gq.lc(),gq.cc(),dangrad*c,rqunit));
    }
    funcao2imgbin(f,d,preto,true,-infinito,infinito,1.0);
    imp(d,"ras.bmp");
    */

//        }

    }
}
