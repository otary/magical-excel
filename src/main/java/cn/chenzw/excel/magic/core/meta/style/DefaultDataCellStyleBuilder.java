package cn.chenzw.excel.magic.core.meta.style;

import cn.chenzw.excel.magic.core.meta.model.ExcelCellStyleDefinition;
import org.apache.poi.ss.usermodel.*;

public class DefaultDataCellStyleBuilder implements CellStyleBuilder {

    @Override
    public CellStyle build(Workbook workbook, ExcelCellStyleDefinition cellStyleDefinition, Cell cell) {
        CellStyle cellStyle = cellStyleDefinition.getCellStyle();

        // 设置对齐方式
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        return cellStyle;
    }
}
