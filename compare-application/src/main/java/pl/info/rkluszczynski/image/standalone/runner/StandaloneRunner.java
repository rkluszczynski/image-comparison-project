package pl.info.rkluszczynski.image.standalone.runner;

import org.springframework.stereotype.Component;

/**
 * Created by Rafal on 2014-05-29.
 */
@Component(value = "mainRunner")
public class StandaloneRunner {

    public void run() {
        System.out.println("Test");
    }
}
