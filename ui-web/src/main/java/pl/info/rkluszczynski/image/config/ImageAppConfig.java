package pl.info.rkluszczynski.image.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import pl.info.rkluszczynski.image.logger.LoggerPostProcessor;

@Configuration
@ComponentScan(basePackages = "pl.info.rkluszczynski.image")
public class ImageAppConfig {

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
    public LoggerPostProcessor loggerPostProcessor() {
        return new LoggerPostProcessor();
    }
}
