package pl.info.rkluszczynski.image.engine.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import static pl.info.rkluszczynski.image.engine.config.EngineConstants.ENGINE_LIBRARY_BASE_PACKAGE;

@Configuration
@ComponentScan(basePackages = ENGINE_LIBRARY_BASE_PACKAGE)
@EnableScheduling
@PropertySource(value = "classpath:compare-engine.properties")
public class EngineJavaConfig {

    @Autowired
    private Environment env;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean(name = "taskExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(5);
        threadPoolTaskExecutor.setMaxPoolSize(15);
//        threadPoolTaskExecutor.setQueueCapacity();
        return threadPoolTaskExecutor;
    }
}
