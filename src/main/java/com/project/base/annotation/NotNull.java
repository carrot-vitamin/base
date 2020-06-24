package com.project.base.annotation;

import java.lang.annotation.*;

/**
 * @author yinshaobo at 2020/6/24 9:57
 * 校验非空属性
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNull {

    String message() default "Not Allowed Null";
}
