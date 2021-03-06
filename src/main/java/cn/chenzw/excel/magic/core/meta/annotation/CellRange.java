package cn.chenzw.excel.magic.core.meta.annotation;


import cn.chenzw.excel.magic.core.meta.style.DefaultTitleCellStyleBuilder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CellRange {

    int firstRow();

    int lastRow();

    int firstCol();

    int lastCol();

    String title();

    int height() default 20;

    Class<?> cellStyleBuilder() default DefaultTitleCellStyleBuilder.class;

}
