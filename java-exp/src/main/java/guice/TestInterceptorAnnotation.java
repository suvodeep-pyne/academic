package guice;
/*
 * Created by IntelliJ IDEA.
 * User: spyne
 * Date: 1/15/16
 * Time: 10:40 AM
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface TestInterceptorAnnotation {
}
