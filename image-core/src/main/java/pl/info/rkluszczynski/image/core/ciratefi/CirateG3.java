package pl.info.rkluszczynski.image.core.ciratefi;

import pl.info.rkluszczynski.image.core.ciratefi.calculators.CifiCalculator;
import pl.info.rkluszczynski.image.core.ciratefi.calculators.RafiCalculator;
import pl.info.rkluszczynski.image.core.ciratefi.calculators.RassqCalculator;
import pl.info.rkluszczynski.image.core.ciratefi.calculators.TefiCalculator;
import pl.info.rkluszczynski.image.core.utils.ImageHelper;

import java.awt.image.BufferedImage;
import java.util.Properties;

/**
 * @author Rafal
 */
public class CirateG3 {

    BufferedImage analyzeGaussImage;
    BufferedImage queryGaussImage;


    public BufferedImage run(
            Properties properties,
            BufferedImage analyzeImage,
            BufferedImage queryImage
    ) {
        long timeMillis = System.currentTimeMillis();

        analyzeGaussImage = ImageHelper.deepCopy(analyzeImage);
        queryGaussImage = ImageHelper.deepCopy(queryImage);

//        { IMGCOR ancor;
//            le(ancor,analyze);
//            converte(ancor,anori);
//        }
//        piramide(anori,anori,v); // acrescentado
//        an=anori;
//
//        { IMGCOR qucor;
//            le(qucor,query);
//            converte(qucor,qu);
//        }
//        qu=quadradaimpar(qu);
//        autoparam();

//        if (gauss_exec) gauss(); else { le(ga,gaussa); le(gq,gaussq); }
        if (CiratefiProperties.gauss_exec) {
            GaussApplier.gauss(
                    analyzeGaussImage,
                    queryGaussImage
            );
        }

//        if (cissa_exec) cissa();
        if (CiratefiProperties.cissa_exec)
            CircularProjections.cissa(analyzeImage);
//        if (cissq_exec) cissq();
        if (CiratefiProperties.cissq_exec)
            CircularProjections.cissq(queryImage);

//        if (cifi_exec)  cifi();
        if (CiratefiProperties.cifi_exec)
            CifiCalculator.cifi();
//        if (rassq_exec) rassq();
        if (CiratefiProperties.rassq_exec)
            RassqCalculator.rassq();
//        if (rafi_exec)  rafi();
        if (CiratefiProperties.rafi_exec)
            RafiCalculator.rafi();
//        if (tefi_exec)  tefi();
        if (CiratefiProperties.tefi_exec)
            TefiCalculator.tefi();

        BufferedImage resultImage = analyzeImage;
//        if (circ_exec)  circ();
        if (CiratefiProperties.circ_exec)
            ResultPresenter.circ(resultImage);
//        if (txt_exec)   txt();
        if (CiratefiProperties.txt_exec)
            ResultPresenter.txt();

        timeMillis = System.currentTimeMillis() - timeMillis;
        System.out.printf("Processing time: %d.%03d second(s)\n", timeMillis / 1000, timeMillis % 1000);
        return null;
    }
}
