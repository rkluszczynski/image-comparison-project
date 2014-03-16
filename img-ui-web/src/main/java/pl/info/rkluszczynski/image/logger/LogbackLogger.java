package pl.info.rkluszczynski.image.logger;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface LogbackLogger {
}
