package com.littlepants.attack.attackplus.annotation;


import com.littlepants.attack.attackplus.dataValidation.EnumValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/11/18
 * @description
 */
@Documented
@Target({ElementType.FIELD})
@Constraint(validatedBy = {EnumValidator.class})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateEnum {
    String message() default "不在可用的参数内\n";
    Class<?>[] groups() default {};
    Class<?extends Payload>[] payload() default {};
    String[] value() default {};
}
