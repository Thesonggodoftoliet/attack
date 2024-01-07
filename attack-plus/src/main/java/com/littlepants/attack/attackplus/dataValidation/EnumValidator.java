package com.littlepants.attack.attackplus.dataValidation;

import com.littlepants.attack.attackplus.annotation.ValidateEnum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/11/18
 * @description
 */
public class EnumValidator implements ConstraintValidator<ValidateEnum,String> {
    private Set<String> set = new HashSet<>();
    @Override
    public void initialize(ValidateEnum constraintAnnotation) {
//        ConstraintValidator.super.initialize(constraintAnnotation);
        String[] value = constraintAnnotation.value();
        for (String i:value)
            set.add(i);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        boolean tag = set.contains(s);
        return tag;
    }
}
