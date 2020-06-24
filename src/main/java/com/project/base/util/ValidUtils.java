package com.project.base.util;

import com.project.base.annotation.NotBlank;
import com.project.base.annotation.NotEmpty;
import com.project.base.annotation.NotNull;
import com.project.base.annotation.NotNullObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * @author yinshaobo at 2020/6/24 10:06
 * 校验数据，如非空等
 */
public class ValidUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidUtils.class);

    public static String validObject(Object object) {
        String message = null;
        Class<?> objectClass = object.getClass();

        Field[] fields = objectClass.getDeclaredFields();

        //是否类注解了非空
        boolean notNullObject = objectClass.isAnnotationPresent(NotNullObject.class);

        for (Field field : fields) {
            //打开私有访问
            field.setAccessible(true);
            //获取属性名
            String name = field.getName();
            //获取属性值
            Object value;

            try {
                value = field.get(object);
            } catch (IllegalAccessException e) {
                LOGGER.error(e.getMessage(), e);
                message = "数据处理异常！" + e.getMessage();
                break;
            }

            //注解校验
            message = validFieldWithAnnotation(field, value);

            if (StringUtils.isNotBlank(message)) {
                break;
            } else {
                if (notNullObject) {
                    //需要校验全字段
                    message = validValue(name, value);
                    if (message != null) {
                        break;
                    }
                }
            }
        }

        return message;
    }

    private static String validValue(String name, Object value) {
        String message = null;
        if (value == null) {
            message = name + " Not Allowed Null";
        } else {
            //根据类型判断非空
            if ((value instanceof String) && ((String) value).trim().length() == 0) {
                message = name + " Not Allowed Null";
            } else if ((value instanceof Collection) && ((Collection) value).isEmpty()) {
                message = name + " Not Allowed Empty";
            }
        }
        return message;
    }

    private static String validFieldWithAnnotation(Field field, Object value) {
        String message = null;
        NotNull notNull = field.getAnnotation(NotNull.class);
        NotBlank notBlank = field.getAnnotation(NotBlank.class);
        NotEmpty notEmpty = field.getAnnotation(NotEmpty.class);
        if (notNull != null && value == null) {
            message = notNull.message();
        } else if (notBlank != null && StringUtils.isBlank((String) value)) {
            message = notBlank.message();
        } else if (notEmpty != null) {
            Collection collection = (Collection) value;
            if (collection == null || collection.isEmpty()) {
                message = notEmpty.message();
            }
        }
        return message;
    }
}
