package com.sunchp.artery.springsupport.annotation;

import org.springframework.stereotype.Component;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Component
public @interface ArteryService {
    Class<?> interfaceClass() default void.class;
}
