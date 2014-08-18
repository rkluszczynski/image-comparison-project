package pl.info.rkluszczynski.image.standalone.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import static pl.info.rkluszczynski.image.standalone.config.ApplicationConstants.COMPARE_APPLICATION_BASE_PACKAGE;

@Configuration
@ComponentScan(basePackages = COMPARE_APPLICATION_BASE_PACKAGE)
@PropertySource(value = "classpath:detection-application.properties")
public class ApplicationJavaConfig {

    @Autowired
    private Environment env;

}
