package pl.info.rkluszczynski.image.engine.utils;

import java.awt.*;

/**
 * Created by Rafal on 2014-07-04.
 */
public interface ColorPredicate<E extends Color> {

    boolean apply(E input, int iw, int ih);

}
