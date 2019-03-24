package cn.chenzw.excel.magic.core.meta.annotation.validation;


import cn.chenzw.excel.magic.core.support.validator.ExcelRegexValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@ConstraintValidator(validator = ExcelRegexValidator.class)
public @interface ExcelRegexValue {

    String regex();

    String message() default "";
}
