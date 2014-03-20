package pl.info.rkluszczynski.image.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import pl.info.rkluszczynski.image.tmp.validator.ImageFileValidator;

import java.io.IOException;

@EnableWebMvc
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    /*
        Configuring multipartResolver responsible for file upload.
     */
    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver commonsMultipartResolver() {
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
        commonsMultipartResolver.setMaxUploadSize(2L * ONE_MB_IN_BYTES);
        commonsMultipartResolver.setMaxInMemorySize(32 * ONE_MB_IN_BYTES);
        try {
            commonsMultipartResolver.setUploadTempDir(fileSystemResource());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return commonsMultipartResolver;
    }

    @Bean(name = "imageFileValidator")
    public ImageFileValidator imageFileValidator() {
        return new ImageFileValidator();
    }

    @Bean
    public FileSystemResource fileSystemResource() {
        return new FileSystemResource("/tmp");
    }

    private static final int ONE_MB_IN_BYTES = 1024 * 1024;
}
