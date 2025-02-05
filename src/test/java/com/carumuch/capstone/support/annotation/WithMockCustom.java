package com.carumuch.capstone.support.annotation;

import com.carumuch.capstone.support.security.MockCustomUserSecurityContextFactory;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@WithSecurityContext(factory = MockCustomUserSecurityContextFactory.class)
public @interface WithMockCustom {
    long id() default 1L;
    String role();
}
