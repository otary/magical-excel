package cn.chenzw.excel.magic.core.meta.annotation.converter;


import cn.chenzw.excel.magic.core.support.converter.AbstractExcelColumnConverter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ExcelConverter {

    Class<? extends AbstractExcelColumnConverter> convertBy();

}
