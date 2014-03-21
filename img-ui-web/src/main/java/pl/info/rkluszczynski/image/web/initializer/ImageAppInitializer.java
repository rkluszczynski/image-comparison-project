package pl.info.rkluszczynski.image.web.initializer;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import static pl.info.rkluszczynski.image.engine.config.EngineConstants.ENGINE_LIBRARY_BASE_PACKAGE;
import static pl.info.rkluszczynski.image.web.config.WebConstants.WEB_APPLICATION_BASE_PACKAGE;

public class ImageAppInitializer implements WebApplicationInitializer {

    private static final String[] CONFIG_LOCATIONS = {
            WEB_APPLICATION_BASE_PACKAGE + ".config",
            ENGINE_LIBRARY_BASE_PACKAGE + ".config"
    };
    private static final String MAPPING_URL = "/*";

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        WebApplicationContext context = getContext();
        servletContext.addListener(new ContextLoaderListener(context));
        DispatcherServlet dispatcherServlet = new DispatcherServlet(context);
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("imageAppDispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping(MAPPING_URL);
    }

    private AnnotationConfigWebApplicationContext getContext() {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        for (String configLocation : CONFIG_LOCATIONS) {
            context.setConfigLocation(configLocation);
        }
        return context;
    }
}
