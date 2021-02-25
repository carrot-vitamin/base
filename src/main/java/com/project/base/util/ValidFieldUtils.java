package com.project.base.util;

import com.project.base.model.ValidatorResult;
import com.project.base.model.annotation.NotBlank;
import com.project.base.model.annotation.NotEmpty;
import com.project.base.model.annotation.NotNull;
import com.project.base.model.annotation.NotNullObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Set;

/**
 * @author yinshaobo at 2020/6/24 10:06
 * 校验数据，如非空等
 */
public class ValidFieldUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidFieldUtils.class);

    /**
     * 校验对象字段
     * @param object 包含自定义注解的对象
     * @return 校验结果
     */
    public static ValidatorResult validObject(Object object) {
        ValidatorResult result = new ValidatorResult();
        try {
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
                Object value = field.get(object);

                //注解校验
                String message = validFieldWithAnnotation(field, value);

                if (StringUtils.isNotBlank(message)) {
                    result.setHasError(true);
                    result.addErrorMessage(message);
                } else {
                    if (notNullObject) {
                        //需要校验全字段
                        message = validValue(name, value);
                        if (StringUtils.isNotBlank(message)) {
                            result.setHasError(true);
                            result.addErrorMessage(message);
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("对象校验异常！", e);
            result.setHasError(true);
            result.addErrorMessage(e.getMessage());
        }
        return result;
    }

    /**
     * 使用validation-api工具校验
     * @param object 待校验对象
     * @return 校验结果
     */
    public static ValidatorResult validObjectWithApi(Object object) {
        ValidatorResult result = new ValidatorResult();
        try {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Object>> validations = validator.validate(object);

            if (validations != null && !validations.isEmpty()) {
                result.setHasError(true);
                for (ConstraintViolation<Object> constraintViolation : validations) {
                    result.addErrorMessage(constraintViolation.getMessage());
                }
            }
        } catch (Exception e) {
            LOGGER.error("对象校验异常！", e);
            result.setHasError(true);
            result.addErrorMessage(e.getMessage());
        }
        return result;
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
