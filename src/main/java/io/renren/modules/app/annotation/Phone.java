package io.renren.modules.app.annotation;

import org.hibernate.validator.constraints.CompositionType;
import org.hibernate.validator.constraints.ConstraintComposition;
import org.hibernate.validator.constraints.Length;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import java.lang.annotation.*;

/**
 * @author 50519
 * @description
 * @Date 2020/2/25 10:15
 */
@ConstraintComposition(CompositionType.AND)
//@Pattern(regexp = "1[3|4|5|7|8][0-9]\\d{8}")
@Pattern(regexp = "1[0-9]{10}")
@NotNull(message = "手机号不能为空")
@Length(min = 11, max = 11, message = "手机号位数不正确")
@Documented
@Constraint(validatedBy = {})
@Target({ElementType.METHOD,ElementType.FIELD,ElementType.CONSTRUCTOR,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
public @interface Phone {
    String message() default "手机号校验错误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
