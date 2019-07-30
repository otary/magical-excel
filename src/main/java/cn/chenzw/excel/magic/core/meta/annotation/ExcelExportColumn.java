package cn.chenzw.excel.magic.core.meta.annotation;

import cn.chenzw.excel.magic.core.meta.style.DefaultDataCellStyleBuilder;
import cn.chenzw.excel.magic.core.meta.style.DefaultTitleCellStyleBuilder;
import org.apache.poi.ss.usermodel.CellType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel列
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelExportColumn {


    /**
     * 标题
     * @return
     */
    String title();

    /**
     * 列索引（从1开始）
     * @return
     */
    int colIndex();

    /**
     * 列类型
     * @return
     */
    CellType cellType() default CellType.STRING;

    /**
     * 数据格式
     * @return
     */
    ExcelDataFormat dataFormat() default @ExcelDataFormat;

    /**
     * 数据样式
     * @return
     */
    Class<?> dataCellStyleBuilder() default DefaultDataCellStyleBuilder.class;

    /**
     * 标题样式
     * @return
     */
    Class<?> titleCellStyleBuilder() default DefaultTitleCellStyleBuilder.class;

    /**
     * 是否自动调整宽度
     * @return
     */
    boolean autoWidth() default false;

    /**
     * 自定义cell宽度
     * @return
     */
    int colWidth() default 16;

}
