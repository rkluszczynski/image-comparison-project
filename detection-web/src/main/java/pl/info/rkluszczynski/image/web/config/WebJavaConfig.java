package pl.info.rkluszczynski.image.web.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@ComponentScan(basePackages = {WEB_APPLICATION_BASE_PACKAGE})
@Import(value = {EngineJavaConfig.class})
@PropertySource(value = "classpath:compare-web.properties")
class WebJavaConfig {
    private static Logger logger = LoggerFactory.getLogger(WebJavaConfig.class);

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

//    @Bean(name = "imagePatternsResources")
//    public ArrayList<Resource> imagePatternsResources() {
//        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//        try {
//            Resource[] resources = resolver.getResources("classpath:image-data/patterns/**/*.png");
//            ArrayList<Resource> arrayList = Lists.newArrayList();
//            for (Resource resource : resources) {
//                arrayList.add(resource);
//                logger.info("Resource: {}", resource.getFilename());
//            }
//            return arrayList;
//        } catch (IOException e) {
//            logger.warn("Problem with reading image resources!", e);
//        }
//        return Lists.newArrayList();
//    }
//
//    @Bean(name = "imagePatterns")
//    public List<BufferedImage> bufferedImageResourcePatterns() {
//        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//        List<BufferedImage> bufferedImages = Lists.newArrayList();
//        try {
//            Resource[] resources = resolver.getResources("classpath:image-data/patterns/**/*.png");
//            for (Resource resource : resources) {
//                BufferedImage bufferedImage = ImageIO.read(resource.getInputStream());
//                bufferedImages.add(bufferedImage);
//            }
//        } catch (IOException e) {
//            logger.warn("Problem with reading image patterns!", e);
//        }
//        return  bufferedImages;
//    }

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
