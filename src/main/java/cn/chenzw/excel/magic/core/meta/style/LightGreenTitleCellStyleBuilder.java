package cn.chenzw.excel.magic.core.meta.style;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

/**
 * 浅绿色
 */
public class LightGreenTitleCellStyleBuilder implements CellStyleBuilder {

    @Override
    public CellStyle build(Workbook workbook, Cell cell) {
        XSSFCellStyle cellStyle = (XSSFCellStyle) workbook.createCellStyle();

        // 前景色
        cellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(112, 173, 71)));
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // 对齐方式
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // 黑体
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        cellStyle.setFont(font);

        return cellStyle;
    }
}
