package pl.info.rkluszczynski.image.standalone;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pl.info.rkluszczynski.image.engine.config.EngineJavaConfig;
import pl.info.rkluszczynski.image.standalone.config.ApplicationJavaConfig;
import pl.info.rkluszczynski.image.standalone.runner.StandaloneRunner;

public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(ApplicationJavaConfig.class);
        context.register(EngineJavaConfig.class);
        context.refresh();

        StandaloneRunner mainRunner = (StandaloneRunner) context.getBean("mainRunner");
        mainRunner.run();

        context.close();
    }
}
