package com.admin.web.swagger.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Api {

    String tag() default "";

    String description() default "";
}
