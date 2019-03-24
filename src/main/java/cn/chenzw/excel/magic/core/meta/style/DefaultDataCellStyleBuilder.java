package cn.chenzw.excel.magic.core.meta.style;

import org.apache.poi.ss.usermodel.*;

public class DefaultDataCellStyleBuilder implements CellStyleBuilder {


    @Override
    public CellStyle build(Workbook workbook, Cell cell) {
        CellStyle cellStyle = workbook.createCellStyle();

        // 设置对齐方式
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        return cellStyle;
    }
}
