package cn.chenzw.excel.magic.core.meta.style;

import org.apache.poi.ss.usermodel.*;

/**
 * @author chenzw
 */
public class DefaultTitleCellStyleBuilder implements CellStyleBuilder {

    @Override
    public CellStyle build(Workbook workbook, Cell cell) {
        CellStyle cellStyle = workbook.createCellStyle();

        Font font = workbook.createFont();
        font.setBold(true);
        cellStyle.setFont(font);

        // 设置对齐方式
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return cellStyle;
    }
}
