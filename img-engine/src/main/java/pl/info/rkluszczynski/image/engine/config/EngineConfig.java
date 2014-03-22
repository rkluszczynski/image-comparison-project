package pl.info.rkluszczynski.image.engine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.info.rkluszczynski.image.engine.cache.SessionCacheScheduler;

import static pl.info.rkluszczynski.image.engine.config.ConfigConstants.ENGINE_LIBRARY_BASE_PACKAGE;

@Configuration
@ComponentScan(basePackages = ENGINE_LIBRARY_BASE_PACKAGE)
@EnableScheduling
public class EngineConfig {

    @Bean
    public SessionCacheScheduler sessionCacheScheduler() {
        return new SessionCacheScheduler();
    }
}
