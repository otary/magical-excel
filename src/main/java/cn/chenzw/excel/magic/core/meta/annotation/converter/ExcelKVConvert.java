package cn.chenzw.excel.magic.core.meta.annotation.converter;

import cn.chenzw.excel.magic.core.support.converter.ExcelKVConverter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@ExcelConverter(convertBy = ExcelKVConverter.class)
public @interface ExcelKVConvert {

    /**
     * 键值对集合，格式: key=value
     * @return
     */
    String[] kvmap() default {};

    /**
     * 是否允许未匹配的值
     * @return
     */
    boolean allowMissHit() default false;
}
