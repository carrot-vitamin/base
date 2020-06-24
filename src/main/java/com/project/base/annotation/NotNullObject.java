package com.project.base.annotation;

import java.lang.annotation.*;

/**
 * @author yinshaobo at 2020/6/24 9:57
 * 对象下的所有字段非空
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNullObject {
}
