package kr.hhplus.be.server.common.annotation;


import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExceptionRecordable {
    boolean retryable() default false;
}
