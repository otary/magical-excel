package cn.chenzw.excel.magic.core.meta.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelImportColumn {

    /**
     * 列索引(从1开始)
     * @return
     */
    int colIndex() default -1;

    /**
     * 是否允许空值
     * @return
     */
    boolean allowBlank() default true;

    /**
     * 日期格式
     * @return
     */
    String dateFormat() default "";
}
