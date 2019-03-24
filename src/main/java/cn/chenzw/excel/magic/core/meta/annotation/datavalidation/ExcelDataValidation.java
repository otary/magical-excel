package cn.chenzw.excel.magic.core.meta.annotation.datavalidation;


import cn.chenzw.excel.magic.core.support.dataconstraint.ExcelDataValidationConstraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelDataValidation {

    Class<? extends ExcelDataValidationConstraint> dataConstraint();

}
