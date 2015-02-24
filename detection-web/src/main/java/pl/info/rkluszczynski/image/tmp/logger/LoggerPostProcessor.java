package pl.info.rkluszczynski.image.tmp.logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * LoggerPostProcessor => Custom Spring BeanPostProcessor *
 */
public class LoggerPostProcessor implements BeanPostProcessor {

    public Object postProcessAfterInitialization(Object bean, String beanName) throws
            BeansException {
        return bean;
    }

    public Object postProcessBeforeInitialization(final Object bean, String beanName)
            throws BeansException {
        ReflectionUtils.doWithFields(bean.getClass(), new ReflectionUtils.FieldCallback() {
            @SuppressWarnings("unchecked")
            public void doWith(Field field) throws IllegalArgumentException,
                    IllegalAccessException {
                ReflectionUtils.makeAccessible(field);

                //Check if the field is annoted with @LogBackLogger
                if (field.getAnnotation(LogBackLoggerFielder.class) != null) {
                    LogBackLoggerFielder logbackLoggerAnnotation = field.getAnnotation(LogBackLoggerFielder.class);
                    org.slf4j.Logger logger = LoggerFactory.getLogger(bean.getClass());
                    field.set(bean, logger);
                }
            }
        });
        return bean;
    }
}
