package com.sandmor.library_system.common.annotations;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@RestController
@RequestMapping("/api")
public @interface ApiRestController {
    /**
     * Alias for {@link RequestMapping#value}.
     * Defines the specific path for this controller, which will be appended to the
     * "/api" base path.
     */
    @AliasFor(annotation = RequestMapping.class, attribute = "value")
    String[] value() default {};
}