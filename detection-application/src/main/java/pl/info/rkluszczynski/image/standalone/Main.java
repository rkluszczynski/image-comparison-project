package pl.info.rkluszczynski.image.standalone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pl.info.rkluszczynski.image.engine.config.EngineJavaConfig;
import pl.info.rkluszczynski.image.standalone.config.ApplicationJavaConfig;
import pl.info.rkluszczynski.image.standalone.runner.StandaloneRunner;
import pl.info.rkluszczynski.image.standalone.runner.StatisticsCalculatorRunner;

import java.io.IOException;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(ApplicationJavaConfig.class);
        context.register(EngineJavaConfig.class);
        context.refresh();

        StatisticsCalculatorRunner statisticsRunner = (StatisticsCalculatorRunner) context.getBean("statisticsRunner");
        StandaloneRunner standaloneRunner = (StandaloneRunner) context.getBean("mainRunner");
        try {
//            statisticsRunner.run();
            standaloneRunner.run();
        } catch (IOException e) {
            logger.error("Problem during execution", e);
        }
        context.close();
    }
}
