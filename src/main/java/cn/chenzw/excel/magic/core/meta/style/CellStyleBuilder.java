package cn.chenzw.excel.magic.core.meta.style;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

public interface CellStyleBuilder {

    CellStyle build(Workbook workbook, Cell cell);

}
