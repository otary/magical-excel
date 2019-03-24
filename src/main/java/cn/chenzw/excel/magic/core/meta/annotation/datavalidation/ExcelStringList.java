package cn.chenzw.excel.magic.core.meta.annotation.datavalidation;


import cn.chenzw.excel.magic.core.support.dataconstraint.ExcelStringListDataValidationConstaint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@ExcelDataValidation(dataConstraint = ExcelStringListDataValidationConstaint.class)
public @interface ExcelStringList {

    String[] value() default {};
}
