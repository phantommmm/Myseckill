package com.phantom.seckill.validator;

import com.phantom.seckill.utils.ValidationUtil;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class isPhoneValidator implements ConstraintValidator<isPhone,String> {

    private boolean required = true;

    /**
     * 初始化
     * @param constraintAnnotation
     */
    @Override
    public void initialize(isPhone constraintAnnotation) {
        required=constraintAnnotation.required();
    }

    /**
     * 校验方法
     * @param s
     * @param constraintValidatorContext
     * @return
     */
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(required|| !StringUtils.isEmpty(s)){
            return ValidationUtil.isPhone(s);
        }
        return true;
    }
}
