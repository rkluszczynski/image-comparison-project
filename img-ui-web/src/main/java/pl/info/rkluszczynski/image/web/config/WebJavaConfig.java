package pl.info.rkluszczynski.image.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import pl.info.rkluszczynski.image.engine.config.EngineJavaConfig;
import pl.info.rkluszczynski.image.tmp.logger.LoggerPostProcessor;

import static pl.info.rkluszczynski.image.web.config.WebConstants.WEB_APPLICATION_BASE_PACKAGE;


@Configuration
@ComponentScan(basePackages = { WEB_APPLICATION_BASE_PACKAGE })
@Import(value = { EngineJavaConfig.class })
@PropertySource(value = "classpath:properties/image-web.properties")
public class WebJavaConfig {

    @Autowired
    private Environment env;

    @Bean
    public ThymeleafViewResolver thymeleafViewResolver() {
        ServletContextTemplateResolver servletContextTemplateResolver = new ServletContextTemplateResolver();
        servletContextTemplateResolver.setPrefix("/WEB-INF/views/");
        servletContextTemplateResolver.setSuffix(".html");
        servletContextTemplateResolver.setTemplateMode("HTML5");
        servletContextTemplateResolver.setCharacterEncoding("UTF-8");

        SpringTemplateEngine springTemplateEngine = new SpringTemplateEngine();
        springTemplateEngine.setTemplateResolver(servletContextTemplateResolver);

        ThymeleafViewResolver thymeleafViewResolver = new ThymeleafViewResolver();
        thymeleafViewResolver.setTemplateEngine(springTemplateEngine);
        thymeleafViewResolver.setCharacterEncoding("UTF-8");
        thymeleafViewResolver.setOrder(1);
        return thymeleafViewResolver;
    }

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("/resources/messages");
        return messageSource;
    }

    @Bean
    public LoggerPostProcessor loggerPostProcessor() {
        return new LoggerPostProcessor();
    }
}
