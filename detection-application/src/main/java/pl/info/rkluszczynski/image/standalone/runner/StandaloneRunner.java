package pl.info.rkluszczynski.image.standalone.runner;

import pl.info.rkluszczynski.image.standalone.exception.StandaloneApplicationException;

public interface StandaloneRunner {

    void run() throws StandaloneApplicationException;
}
