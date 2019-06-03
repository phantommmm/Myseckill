package com.phantom.seckill.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = {isPhoneValidator.class}
)
public @interface isPhone {
    // required 和 message 是自定义参数，groups 和 payload 是两个必须包含的参数。
    boolean required() default true;

    String message() default "手机号码格式错误！"; // 校验失败时的错误信息

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
