package cn.chenzw.excel.magic.core.meta.style;

import cn.chenzw.excel.magic.core.meta.model.ExcelCellStyleDefinition;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * @author chenzw
 */
public interface CellStyleBuilder {

    CellStyle build(Workbook workbook, ExcelCellStyleDefinition cellStyleDefinition, Cell cell);
}
