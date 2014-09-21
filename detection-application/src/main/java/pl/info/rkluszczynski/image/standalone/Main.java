package pl.info.rkluszczynski.image.standalone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pl.info.rkluszczynski.image.engine.config.EngineJavaConfig;
import pl.info.rkluszczynski.image.standalone.config.ApplicationJavaConfig;
import pl.info.rkluszczynski.image.standalone.runner.StandaloneRunner;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    //    private static final String STANDALONE_RUNNER = "imagePatternDetector";
    private static final String STANDALONE_RUNNER = "scenePatternEvaluator";

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(EngineJavaConfig.class);
        context.register(ApplicationJavaConfig.class);
        context.refresh();

        try {
            StandaloneRunner runner = (StandaloneRunner) context.getBean(STANDALONE_RUNNER);
            runner.run();
        } catch (Exception e) {
            logger.error("Error during StandaloneRunner execution!", e);
            e.printStackTrace(System.err);
        } finally {
            context.close();
        }
    }
}
