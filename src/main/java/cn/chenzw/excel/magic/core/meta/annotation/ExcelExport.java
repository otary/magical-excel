package cn.chenzw.excel.magic.core.meta.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelExport {

    /**
     * 顺序（值越小，越靠前）
     * @return
     */
    int order() default 0;

    /**
     * Sheet名称
     * @return
     */
    String sheetName() default "数据";

    /**
     * 每个Sheet页允许的最大条数（用于分页）
     * @return
     */
    int maxRowsPerSheet() default Integer.MAX_VALUE;

    /**
     * 是否开启条纹
     * @return
     */
    boolean rowStriped() default true;

    /**
     * 条纹颜色
     * @return
     */
    String rowStripeColor() default "E2EFDA";

    /**
     * 标题行高度
     * @return
     */
    int titleRowHeight() default 20;

    /**
     * 数据行高度
     * @return
     */
    int dataRowHeight() default 20;

}
