package pl.info.rkluszczynski.image.engine.config

import org.springframework.context.annotation.AnnotationConfigApplicationContext
import spock.lang.Specification

class EngineJavaConfigTest extends Specification {

    def "using java config property source"() {
        given:
        def ctx = new AnnotationConfigApplicationContext(EngineJavaConfig.class);
        when:
        String testPropertyValue = ctx.environment.getProperty("testKey")
        then:
        testPropertyValue.equals("testValue")
    }

    def "taskExecutor not null"() {
        given:
        def ctx = new AnnotationConfigApplicationContext(EngineJavaConfig.class);
        def taskExecutor = ctx.getBean("taskExecutor")
        expect:
        taskExecutor != null
    }
}
